#!/bin/sh

#
#  All content copyright (c) 2003-2008 Terracotta, Inc.,
#  except as may otherwise be noted in a separate copyright notice.
#  All rights reserved.
#
FINDNAME=$0
while [ -h $FINDNAME ] ; do FINDNAME=`ls -ld $FINDNAME | awk '{print $NF}'` ; done
SERVER_HOME=`echo $FINDNAME | sed -e 's@/[^/]*$@@'`
SERVER_JAR=`ls ../lib/mdum-servicelogic*.jar`
SERVER_NAME=`hostname -s`
unset FINDNAME

ARGS=($*)
for ((i=0; i<${#ARGS[@]}; i++)); do
  case "${ARGS[$i]}" in
  -D*) export JAVA_OPTS="${JAVA_OPTS} ${ARGS[$i]}" ;;
  -Heap*) HEAP_MEMORY="${ARGS[$i+1]}" ;;
  -Perm*) PERM_MEMORY="${ARGS[$i+1]}" ;;
  -JmxPort*)  JMX_PORT="${ARGS[$i+1]}" ;;
    *) parameters="${parameters} ${ARGS[$i]}" ;;
  esac
done
 SERVER_CONF="-P:zookeeper.servers=192.168.103.47:2181,192.168.101.69:2181,192.168.103.33:2181 -P:zookeeper.node.paths=/mdum/mdum-servicelogic,!/mdum/mdum-servicelogic/${SERVER_NAME} -P:zookeeper.log4j=/mdum/mdum-servicelogic/log4j"


exec "${JAVA_HOME}/bin/java" ${JAVA_OPTS} -server -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -Xms${HEAP_MEMORY} -Xmx${HEAP_MEMORY} -XX:PermSize=${PERM_MEMORY} -XX:MaxPermSize=${PERM_MEMORY} -XX:+HeapDumpOnOutOfMemoryError -Dxmemcached.jmx.enable=true -Duser.dir=${SERVER_HOME} -Dxmemcached.rmi.port=7078 -jar ${SERVER_JAR} ${SERVER_CONF}
