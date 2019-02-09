#!/usr/bin/env bash
sudo pkill -f 'java -jar'
echo 'wait a sec...'
sleep 5
sudo java -jar ~/jukeboxApp/jukeBox-1.0-SNAPSHOT.jar