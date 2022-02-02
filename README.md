# moex-asts-table-export

### Usage

* Download [release](https://github.com/unrealwork/moex-asts-table-export/releases/download/0.0.1/moex-asts-table-export-0.0.1-bin.zip) and unpack it.

```sh
.
├── bin
│   ├── export.bat
│   └── export.sh
├── config
│   ├── asts.config
│   └── log4j2.xml
└── lib
    ├── annotations-23.0.0.jar
    ├── commons-csv-1.9.0.jar
    ├── log4j-api-2.17.0.jar
    ├── log4j-core-2.17.1.jar
    ├── log4j-slf4j-impl-2.17.0.jar
    ├── moex-asts-fond-client-0.0.4-local.jar
    ├── moex-asts-table-export-0.0.1.jar
    ├── native-lib-loader-2.4.0.jar
    └── slf4j-api-1.7.30.jar

```

* Configure connection to ASTS bridge via `config/asts.config` file

* Run export via binaries from `bin` directory. Specify tables as arguments. By default it will export all tables.

```sh
❯ ./bin/export.sh ACCOUNT_BALANCE
00:14:04.816 [main] INFO  com.axibase.asts.client.MoexAstsClientImpl - Native libraries are successfully loaded
00:14:04.849 [main] INFO  com.axibase.asts.client.config.Configs - Loaded config from file /mnt/c/Users/unrea/dev/moex-asts-table-export-0.0.1/config/asts.config. Config : MapBasedClientConfig(hosts=[Host(hostName=192.168.1.100, port
=15005)], preferredHost=null, service=null, preferredBroadcast=null, broadcast=[], serverId=UAT_RISKGATEWAY, userCredentials=UserCredentials(username=MU9032400002, password=******), interfaceId=IFCBroker42R, tradingAccount=null, refreshInterval=PT15S, clientCode=null, optionalParams={LANGUAGE=ENGLISH, NEWPASSWORD=******, LOGFOLDER=logs, LOGGING=2,1, FEEDBACK=igor.shmagrinsky@axibase.com})
00:14:04.865 [main] INFO  com.axibase.asts.client.Clients - Default client is created.
00:14:07.933 [main] INFO  com.axibase.asts.client.MoexAstsClientImpl - Client is successfully connected to ASTS Bridge. [Host(hostName=192.168.1.100, port=15005)]
00:14:07.933 [main] INFO  com.axibase.asts.TableExporter - Tables ACCOUNT_BALANCE will be exported
00:14:07.956 [pool-2-thread-1] INFO  com.axibase.asts.TableExporter - Export ACCOUNT_BALANCE to tables/ACCOUNT_BALANCE.csv with headers: ACCOUNT, BANKACCID, CURRENTPOS, DEPACCID, FIRMID, FIRMUSE, LIMIT1, LIMIT1SET, LIMIT2, LIMIT2SET, LIMITSELL, OPENBAL, PLANBAL, PLANNED, PLANNEDCOVERED, PLANNEDPOSBUY, PLANNEDPOSSELL, SECCODE, SETTLEBAL, USQTYB, USQTYS
00:14:08.689 [main] INFO  com.axibase.asts.TableExporter - Export completed
00:14:08.808 [main] INFO  com.axibase.asts.client.MoexAstsClientImpl - Client disconnected from ASTS Bridge
```

* Exported tables are availble in tables dir

```sh
tables
├── ACCOUNT_BALANCE.csv
├── ASSETS.csv
├── BANK.csv
├── BANKACC.csv
├── BANKACC_FULLCOVERED_LIMITS.csv
├── BANKUSE.csv
├── CLEARING_TRADES.csv
├── CURRENCY.csv
├── FIRMS.csv
├── FIRM_HOLDING_TOTAL.csv
├── GCPOOL.csv
├── GCPOOLASSET.csv
├── INSTRUMENTS.csv
├── POSITIONS.csv
├── RESYSTIME.csv
├── RM_FULLCOVERED_RANGE.csv
├── RM_HOLD.csv
├── RM_HOLD_DETL.csv
├── RM_INDICATIVE.csv
├── RM_POSN.csv
├── RM_PRICERANGE.csv
├── RM_PRICERANGE_FIRM.csv
├── RM_TRDACC.csv
├── RM_TRDACC_PRICERANGE.csv
├── SECACCINT.csv
├── SECURITIES.csv
├── STATS.csv
├── TRADES.csv
├── TRANTYPES.csv
├── TRDACC.csv
└── USERS.csv
```
