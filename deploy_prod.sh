#!/bin/bash
./gradlew build -x test
jar=$(find build/libs/ -name "*SNAPSHOT.jar")

scp $jar gprod:~/apps/goolbitg.jar
ssh gprod "source ~/.bashrc && /home/mk2/scripts/restart.sh"
