#!/bin/sh
binDir=`dirname ${0}`
. ${binDir}/../variables
if [ $# -lt 2 ] ; then
	echo "Enter RADAR Site Code"
	echo "Example: ./radar.sh KOKX"
	exit 1
fi
SITEID=$1
RADNCFILE=$2
SITEIDUPPER=`echo $SITEID | tr '[:lower:]' '[:upper:]';`
SITEIDLOWER=`echo $SITEID | tr '[:upper:]' '[:lower:]';`
mkdir -p $OUTPUT
DATE=`date -u +%Y%m%d`
DATE2=`date -u +%Y/%m/%d`
LOCALDATE=`TZ="$LOCALTZ" date +%m/%d/%Y`
LOCALTIME=`TZ="$LOCALTZ" date +%I:%M%p`
cd $GASCRP

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2BM.gs '$SITEIDUPPER' '$RADNCFILE'.ref.nc '$OUTPUT/$SITEIDLOWER.bm.png' '$WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2REF.gs '$SITEIDUPPER' '$RADNCFILE'.ref.nc '$OUTPUT/$SITEIDLOWER.ref.current.png' 'WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME' '$OUTPUT/$SITEIDLOWER.bm.png

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2BV.gs '$SITEIDUPPER' '$RADNCFILE'.bv.nc '$OUTPUT/$SITEIDLOWER.bv.current.png' 'WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME' '$OUTPUT/$SITEIDLOWER.bm.png

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2CC.gs '$SITEIDUPPER' '$RADNCFILE'.cc.nc '$OUTPUT/$SITEIDLOWER.cc.current.png' 'WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME' '$OUTPUT/$SITEIDLOWER.bm.png

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2ZDR.gs '$SITEIDUPPER' '$RADNCFILE'.zdr.nc '$OUTPUT/$SITEIDLOWER.zdr.current.png' 'WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME' '$OUTPUT/$SITEIDLOWER.bm.png

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2KDP.gs '$SITEIDUPPER' '$RADNCFILE'.kdp.nc '$OUTPUT/$SITEIDLOWER.kdp.current.png' 'WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME' '$OUTPUT/$SITEIDLOWER.kdp.bm.png

/usr/local/grads/bin/grads -lbxc 'run RADAR_L2SW.gs '$SITEIDUPPER' '$RADNCFILE'.sw.nc '$OUTPUT/$SITEIDLOWER.sw.current.png' 'WCT_HOME/radar_scripts/gshapes' '$LOCALDATE' '$LOCALTIME' '$OUTPUT/$SITEIDLOWER.bm.png
cd -