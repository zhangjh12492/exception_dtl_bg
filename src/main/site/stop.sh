#!/bin/bash
DATE=$(date +%Y-%m-%d_%H:%M)
echo "System date:"$DATE
echo "进入任务目录:/data/ExceptionServer"
cd /data/ExceptionServer
echo "停止服务"
java -jar MQServer.jar stop 6081 &
