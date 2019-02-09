#!/usr/bin/env bash
mvn clean install
scp ./target/jukeBox-1.0-SNAPSHOT.jar pi@jointvibe.ru:/home/pi/jukeboxApp
ssh -l pi jointvibe.ru 'bash -s' < remote.sh