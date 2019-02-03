#!/usr/bin/env bash
pkill -f 'java -jar'
echo 'wait a sec...'
sleep 5
java -jar ~/jukeboxApp/jukeBox-1.0-SNAPSHOT.jar