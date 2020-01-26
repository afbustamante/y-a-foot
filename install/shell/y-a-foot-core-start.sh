#!/bin/bash

#JAVA_HOME="/opt/java/jdk-11"
#export PATH=$PATH:$JAVA_HOME/bin

APP_LOG_DIR="/var/log/y-a-foot"

cd /opt/apps/y-a-foot

java -jar y-a-foot-core.jar -server -Xms1024m -Xmx1024m > $APP_LOG_DIR/y-a-foot-core.log &

echo $! > y-a-foot-core.pid
