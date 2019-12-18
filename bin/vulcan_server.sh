#!/usr/bin/env bash

if [ -z "${JAVA_HOME}" ]; then
  echo "JAVA_HOME not found!"
  exit -1
fi

CURRENT_PATH="$(cd "`dirname "$0"`"/; pwd)"
cd ${CURRENT_PATH}
cd ../

JAR_NAME="Vulcan_Server"  #如果是jar方式启动，必须与jar文件名相同

# 变量定义规则，如果没有可删除或将变量值设置为""
# java参数
# JAVA_OPTS="-Xmx3550m"
JAVA_OPTS=""
# gc参数
# CUR_DATE=`date +%Y%m%d`
# GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=800 -Xloggc:./logs/${JAR_NAME}_${CUR_DATE}_gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+HeapDumpBeforeFullGC -XX:+PrintClassHistogramBeforeFullGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/${JAR_NAME}_${CUR_DATE}_oom_dump.log"
GC_OPTS=""

case $1 in
    start)
        if [ `jps | grep ${JAR_NAME}.jar  | wc -l` -gt 0 ];then
            echo "WARN: Vulcan_Server service is already running, please stop it first!"
            exit -1
        fi
        #启动程序
        nohup java -jar ${JAVA_OPTS} ${GC_OPTS} lib/${JAR_NAME}.jar \
        $@  \
        >>/dev/null 2>&1 & \
        if [ `jps | grep ${JAR_NAME}.jar  | wc -l` -le 0 ];then
             echo "WARN: Vulcan_Server service start failed, please check!"
             exit -1
        else
              echo "Vulcan_Server service has started."
        fi
    ;;
    stop)
        jps | awk '{if($2=="'$JAR_NAME'.jar") print $1}' | xargs kill -15
        echo "Vulcan_Server service has stopped."
    ;;
    status)
        if [ `jps | grep ${JAR_NAME}.jar | wc -l` -le 0 ];then
             echo "Vulcan_Server service hasn't running."
             exit -1
        else
              echo "Vulcan_Server service has running."
        fi
    ;;
    restart)
        sh ${CURRENT_PATH}/$0 stop
        sleep 3
        sh ${CURRENT_PATH}/$0 start
    ;;
    *)
        echo "Usage: sh Vulcan_Server.sh {start|stop|status|restart}"
    ;;
esac