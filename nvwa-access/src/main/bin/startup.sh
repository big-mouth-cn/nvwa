#!/bin/sh

#
#  All content copyright (c) 2003-2008 Terracotta, Inc.,
#  except as may otherwise be noted in a separate copyright notice.
#  All rights reserved.
#

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

exec "${JAVA_HOME}/bin/java" ${JAVA_OPTS} -server -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -Xms${HEAP_MEMORY} -Xmx${HEAP_MEMORY} -XX:PermSize=${PERM_MEMORY} -XX:MaxPermSize=${PERM_MEMORY} -XX:+HeapDumpOnOutOfMemoryError -Dxmemcached.jmx.enable=true -jar ../lib/mpt-access-1.0.0.jar

