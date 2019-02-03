#!/usr/bin/env bash
mvn clean install
scp ../target/jukeBox-1.0-SNAPSHOT.jar pi@95.26.184.207:/home/pi/jukeboxApp
ssh -l pi 95.26.184.207 'bash -s' < remote.sh