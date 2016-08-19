#!/bin/bash

source start-appliances.sh
source stop-appliances.sh

echo "coco"

case "$1" in
   start)
		echo "entrou"
		startAll
   ;;
   stop)
		stopAll
   ;;
   restart)
	    	stopAll
    		startAll
   ;;
   *)
      echo "Usage: $0 {start|stop|restart}"
esac
