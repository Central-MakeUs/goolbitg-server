#!/bin/bash
./gradlew build -x test
jar=$(find build/libs/ -name "*SNAPSHOT.jar")

scp $jar gdev:~/apps/goolbitg.jar
ssh gdev "source ~/.bashrc && /home/dogchew/scripts/restart.sh"
