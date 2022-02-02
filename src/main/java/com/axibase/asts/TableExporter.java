package com.axibase.asts;

import com.axibase.asts.client.Clients;
import com.axibase.asts.client.Combiner;
import com.axibase.asts.client.MoexAstsClient;
import com.axibase.asts.client.Record;
import com.micex.client.Meta;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
public class TableExporter {
  private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

  public static void main(String[] args) throws Exception {
    try (MoexAstsClient client = Clients.defaultClient()) {
      List<String> tables = args.length == 0 ? tablesFromClient(client) : Arrays.asList(args);
      log.info("Tables {} will be exported", String.join(", ", tables));
      List<CompletableFuture<?>> futures = new LinkedList<>();
      for (String table : tables) {
        futures.add(CompletableFuture.runAsync(() -> exportAsCsv(table, client), EXECUTOR_SERVICE));
      }
      for (CompletableFuture<?> future : futures) {
        future.get();
      }
      log.info("Export completed");
    } finally {
      EXECUTOR_SERVICE.shutdown();
      boolean isTerminated = EXECUTOR_SERVICE.awaitTermination(5, TimeUnit.SECONDS);
      if (!isTerminated) {
        log.error("Failed to terminate executor service");
        System.exit(-2);
      }
    }
  }

  private static List<String> tablesFromClient(MoexAstsClient client) {
    return StreamSupport.stream(client.marketInfo().tables().spliterator(), false)
        .map(t -> t.name)
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private static void exportAsCsv(final String tableName, MoexAstsClient client) {
    final Meta.Market market = client.marketInfo();
    final Meta.Message table = market.tables().find(tableName);
    if (table == null) {
      log.warn("Unable to find table {}. Skip export for table", tableName);
      return;
    }
    Path tableExportPath = Paths.get("tables");
    if (!Files.exists(tableExportPath)) {
      Files.createDirectories(tableExportPath);
    }
    final Path csvPath = tableExportPath.resolve(tableName + ".csv");

    final String[] headers =
        StreamSupport.stream(table.output().spliterator(), false)
            .map(f -> f.name)
            .sorted()
            .toArray(String[]::new);

    CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader(headers).build();
    log.info("Export {} to {} with headers: {}", tableName, csvPath, String.join(", ", headers));
    try (CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(csvPath), format)) {
      client.request(
          tableName, Collections.emptyMap(), new CsvPrinterCombiner(headers, csvPrinter));
    }
  }

  private static class CsvPrinterCombiner implements Combiner<Record, Record> {
    private final String[] headers;
    private final CSVPrinter csvPrinter;

    public CsvPrinterCombiner(String[] headers, CSVPrinter csvPrinter) {
      this.headers = headers;
      this.csvPrinter = csvPrinter;
    }

    @Override
    public Record initValue() {
      return null;
    }

    @Override
    public Record map(Record rec) {
      return rec;
    }

    @Override
    public Record merge(Record rec, Record record2) {
      List<String> values =
          Arrays.stream(headers)
              .map(rec::param)
              .map(p -> p == null ? "" : p.toString())
              .collect(Collectors.toList());
      try {
        csvPrinter.printRecord(values);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
      return rec;
    }

    @Override
    public void done(Record rec) {
      //nothing to do
    }
  }
}
