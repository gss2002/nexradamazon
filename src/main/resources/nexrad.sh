#!/bin/bash

#set java_home
export JAVA_HOME=/usr/local/ibmjdk8/
#export JAVA_HOME=/usr/local/jdk1.8.0_111/

#SET VARIABLES for where the program is located
export RADAR_HOME=/home/grads
export WCT_HOME=$RADAR_HOME/radarv2/
export RADAR_DATA=$RADAR_HOME/radar_data/level2

#SET CLASSPATH
CLASSPATH="$WCT_HOME/dist/wct-4.0.1.jar:$WCT_HOME/dist/wct-MapData-2.0.jar:$WCT_HOME/lib/DJNativeSwing-1.0.1.jar:$WCT_HOME/lib/DJNativeSwing-SWT-1.0.1.jar:$WCT_HOME/lib/OpenSourcePhysics-STEVE.jar:$WCT_HOME/lib/Panoply.jar:$WCT_HOME/lib/accordion-1.2.0-jar-with-dependencies.jar:$WCT_HOME/lib/apache-bzip.jar:$WCT_HOME/lib/avhrr-util-0.2.jar:$WCT_HOME/lib/commons-cli-1.1.jar:$WCT_HOME/lib/commons-codec-1.3.jar:$WCT_HOME/lib/commons-compress-1.3.jar:$WCT_HOME/lib/commons-io-1.4.jar:$WCT_HOME/lib/commons-lang-2.3.jar:$WCT_HOME/lib/commons-lang3-3.1.jar:$WCT_HOME/lib/commons-net-1.4.1.jar:$WCT_HOME/lib/fi-faidon-STEVE.jar:$WCT_HOME/lib/foxtrot-core-4.0.jar:$WCT_HOME/lib/ftpbean.jar:$WCT_HOME/lib/geotiff-iosp-1.2.jar:$WCT_HOME/lib/geotools-2.0.jar:$WCT_HOME/lib/hsqldb-1.8.0.1.jar:$WCT_HOME/lib/icepdf-core.jar:$WCT_HOME/lib/icepdf-viewer.jar:$WCT_HOME/lib/imageio-ext-tiff-1.1.5.jar:$WCT_HOME/lib/imageio-ext-utilities-1.1.5.jar:$WCT_HOME/lib/itext-2.0.0.jar:$WCT_HOME/lib/jai-imageio-core-1.3.0.jar:$WCT_HOME/lib/jai.jar:$WCT_HOME/lib/jai_imageio-1.1-no-jj2000.jar:$WCT_HOME/lib/jcommon-1.0.12.jar:$WCT_HOME/lib/jetprint-1.10.jar:$WCT_HOME/lib/jfree-fontchooser.jar:$WCT_HOME/lib/jhlabs-filters.jar:$WCT_HOME/lib/jide-oss-3.2.3.jar:$WCT_HOME/lib/jna-3.2.4.jar:$WCT_HOME/lib/jna_WindowUtils.jar:$WCT_HOME/lib/jogl.jar:$WCT_HOME/lib/jsch-0.1.33-20070611.jar:$WCT_HOME/lib/jscience.jar:$WCT_HOME/lib/json-simple-1.1.1.jar:$WCT_HOME/lib/jts-old.jar:$WCT_HOME/lib/junit-4.4.jar:$WCT_HOME/lib/looks-2.1.4.jar:$WCT_HOME/lib/monte-cc.jar:$WCT_HOME/lib/ncdc-iosp-1.0.jar:$WCT_HOME/lib/oncrpc-xdr-partial-STEVE.jar:$WCT_HOME/lib/opencsv-2.3.jar:$WCT_HOME/lib/prompt.jar:$WCT_HOME/lib/swing-layout-1.0.3.jar:$WCT_HOME/lib/swingx-0.9.3.jar:$WCT_HOME/lib/toolsUI-4.6.6.jar:$WCT_HOME/lib/units-0.01.jar:$WCT_HOME/lib/vecmath.jar:$WCT_HOME/lib/visad-2.0-20130124.jar:$WCT_HOME/lib/visadCdm-4.6.1-SNAPSHOT.jar:$WCT_HOME/lib/xercesImpl.jar:$WCT_HOME/lib/xstream-1.4.1.jar:$WCT_HOME/dist/wct-4.0.1.jar:$WCT_HOME/dist/wct-MapData-2.0.jar"
AWSCLASSPATH=`ls dependencies/*.jar| xargs | tr ' ' ':';`
export CLASSPATH=$CLASSPATH:$AWSCLASSPATH

case "$1" in
        start)
		#-Dlog4j.configuration=$WCT_HOME/conf/log4j.properties
            /usr/bin/nohup $JAVA_HOME/bin/java -noverify -Xmx256m -Dlogback.configurationFile=$WCT_HOME/conf/logback.xml -DnexradOutputPath=$RADAR_DATA -Dl2refNexradConfig=$WCT_HOME/conf/wct_l2ref.xml -DnexradQueueConfig=file:///$WCT_HOME/conf/nexrad.queues -Djava.awt.headless=true -classpath $CLASSPATH org.senia.amazon.nexrad.NexradL2Engine > $WCT_HOME/logs/nexrad.out 2>&1 &
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