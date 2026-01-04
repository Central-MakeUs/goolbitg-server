#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
source "$SCRIPT_DIR/env.sh"

pid=$(ps -ef | grep "grep.appname=$APP_NAME" | awk 'NR == 1 {print $2}')

if [ -n "$pid" ]; then
    echo "Running process found: $pid, stop process"
    kill -9 $pid
fi

nohup java -Dgrep.appname=$APP_NAME -jar "$APP_HOME/$APP_NAME.jar" --spring.profiles.active=prod  > "$LOG_HOME/$LOG_FILENAME" 2>&1 &
