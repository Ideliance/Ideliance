#!/bin/sh
#
# Copyright (c) 2009-2012 Luca Garulli
#

echo "           .                                              "
echo "          .\`        \`                                     "
echo "          ,      \`:.                                      "
echo "         \`,\`    ,:\`                                       "
echo "         .,.   :,,                                        "
echo "         .,,  ,,,                                         "
echo "    .    .,.:::::  \`\`\`\`                                   "
echo "    ,\`   .::,,,,::.,,,,,,\`;;                      .:      "
echo "    \`,.  ::,,,,,,,:.,,.\`  \`                       .:      "
echo "     ,,:,:,,,,,,,,::.   \`        \`         \`\`     .:      "
echo "      ,,:.,,,,,,,,,: \`::, ,,   ::,::\`   : :,::\`  ::::     "
echo "       ,:,,,,,,,,,,::,:   ,,  :.    :   ::    :   .:      "
echo "        :,,,,,,,,,,:,::   ,,  :      :  :     :   .:      "
echo "  \`     :,,,,,,,,,,:,::,  ,, .::::::::  :     :   .:      "
echo "  \`,...,,:,,,,,,,,,: .:,. ,, ,,         :     :   .:      "
echo "    .,,,,::,,,,,,,:  \`: , ,,  :     \`   :     :   .:      "
echo "      ...,::,,,,::.. \`:  .,,  :,    :   :     :   .:      "
echo "           ,::::,,,. \`:   ,,   :::::    :     :   .:      "
echo "           ,,:\` \`,,.                                      "
echo "          ,,,    .,\`                                      "
echo "         ,,.     \`,                      S E R V E R        "
echo "       \`\`        \`.                                       "
echo "                 \`\`                                       "
echo "                 \`                                        "

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set ORIENTDB_HOME if not already set
[ -f "$ORIENTDB_HOME"/bin/orient.sh ] || ORIENTDB_HOME=`cd "$PRGDIR/.." ; pwd`
export ORIENTDB_HOME

CONFIG_FILE=$ORIENTDB_HOME/config/orientdb-aserver-config.xml
LOG_FILE=$ORIENTDB_HOME/config/orientdb-server-log.properties
LOG_CONSOLE_LEVEL=info
LOG_FILE_LEVEL=fine
WWW_PATH=$ORIENTDB_HOME/www
#UNCOMMENT BELOW TO DISABLE THE CACHE
ORIENTDB_SETTINGS=-Dcache.level1.enabled=false
JAVA_OPTS_SCRIPT=-XX:+HeapDumpOnOutOfMemoryError

java -server $JAVA_OPTS $JAVA_OPTS_SCRIPT $ORIENTDB_SETTINGS -Djava.util.logging.config.file="$LOG_FILE" -Dorientdb.config.file="$CONFIG_FILE" -Dorientdb.www.path="$WWW_PATH" -Dorientdb.build.number="12659" -cp "$ORIENTDB_HOME/lib/orientdb-server-1.2.0.jar:$ORIENTDB_HOME/lib/*" com.orientechnologies.orient.server.OServerMain
