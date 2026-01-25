#!/bin/bash
./gradlew build -x test

if [ $? -ne 0 ]; then
  echo "Failed to build"
  exit 1
fi

jar=$(find build/libs/ -name "*SNAPSHOT.jar")

if [ -z "$jar" ]; then
  echo "Cannot find artifect"
  exit 1
fi

scp $jar gprod:~/apps/goolbitg.jar

if [ $? -ne 0 ]; then
  echo "Failed deploy artifect"
  exit 1
fi

ssh gprod "source ~/.bashrc && /home/mk2/scripts/restart.sh"
