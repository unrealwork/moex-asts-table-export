#!/bin/sh

#!/bin/bash 
# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(dirname "$SCRIPT")

APPPATH="$SCRIPTPATH/.."

cd "$APPPATH"

java -cp config:lib/*:. com.axibase.asts.TableExporter "$@"
