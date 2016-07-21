#!/bin/bash
DATE=$(date +%Y-%m-%d_%H:%M)
echo "System date:"$DATE
echo "进入任务目录:/data/ExceptionServer"
cd /data/ExceptionServer
echo "启动服务"
java -jar ExceptionServer.jar start 6080 6081 &
