#!/bin/bash
binDir=`dirname ${0}`
. ${binDir}/variables

case "$1" in
        start)
	     #-Dlog4j.configuration=$WCT_HOME/conf/log4j.properties
            /usr/bin/nohup $JAVA_HOME/bin/java -noverify -Xmx256m -Dlogback.configurationFile=$WCT_HOME/conf/logback.xml -DnexradOutputPath=$RADAR_DATA -Dradar_home=$WCT_HOME -Dl2refNexradConfig=$WCT_HOME/conf/wct_l2ref.xml -Dl2bvNexradConfig=$WCT_HOME/conf/wct_l2bv.xml -Dl2ccNexradConfig=$WCT_HOME/conf/wct_l2cc.xml -Dl2zdrNexradConfig=$WCT_HOME/conf/wct_l2zdr.xml -Dl2kdpNexradConfig=$WCT_HOME/conf/wct_l2kdp.xml -Dl2swNexradConfig=$WCT_HOME/conf/wct_l2sw.xml -DnexradQueueConfig=file:///$WCT_HOME/conf/nexrad.queues -Djava.awt.headless=true -classpath $CLASSPATH org.senia.amazon.nexrad.NexradL2Engine > $WCT_HOME/logs/nexrad.out 2>&1 &
	    NEXRADPID=`ps guaxww | grep org.senia.amazon.nexrad.NexradL2Engine | grep -vi grep | awk '{print $2}';`
	    echo $NEXRADPID > $WCT_HOME/run/nexrad.pid
            ;;
         
        stop)
            NEXRADPID=`cat $WCT_HOME/run/nexrad.pid`
            if [[ "$NEXRADPID" != "" ]] ; then
        	echo "Stopping Nexrad Engine PID = $NEXRADPID"
        	kill -15 $NEXRADPID
		rm $WCT_HOME/run/nexrad.pid
            else 
        	echo "Nexrad Engine stopped"
            fi
            ;;
         
        status)
	    NEXRADPID=`ps guaxww | grep org.senia.amazon.nexrad.NexradL2Engine | grep -vi grep | awk '{print $2}';`
            if [[ "$NEXRADPID" != "" ]] ; then
            	echo "Nexrad Engine Running PID = $NEXRADPID"
            else
                echo "Nexrad Engine Not Running"
            fi
            ;;
        restart)
            stop
            sleep 5
            start
            ;;
         
        *)
            echo $"Usage: $0 {start|stop|restart|status}"
            exit 1
 
esac