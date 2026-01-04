#!/bin/bash

timestamp=$(date +"%Y%m%d")

echo "Create backup log as $LOG_FILENAME.$timestamp"

cp "$LOG_HOME/$LOG_FILENAME" "$LOG_HOME/$LOG_FILENAME.$timestamp"

> "$LOG_HOME/$LOG_FILENAME"

# Clear outdated log files
expired_logs=$(find "$LOG_HOME" -type f -name "$LOG_FILENAME.*" -ctime "+$LOG_RETENTION")
for expired in $expired_logs; do
	echo "Remove outdated backup log $expired"
	rm "$expired"
done
