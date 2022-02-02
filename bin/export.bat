@echo off
pushd %~dp0
cd ..

echo %cd%

@echo on
java -cp config;lib/*;. com.axibase.asts.TableExporter %*
@echo off
popd
