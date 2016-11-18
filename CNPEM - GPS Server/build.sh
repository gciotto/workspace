#!/bin/bash

# Instead of creating new service files, it generates symbolic links
# to these files, which must be in the same directory this script is.
#
# Author: Gustavo CIOTTO PINTON

PROJECT_DIR=${PWD}

SYSTEM_MD_PATH=/lib/systemd/system

NTP_VERSION=ntp-4.2.8p8
NTP_ROOT=/usr/local
NTP_MAKE_LOG=/tmp/ntpd-make.log
NTP_CONFIGURE_LOG=/tmp/ntpd-configure.log
NTP_URL=http://www.eecis.udel.edu/~ntp/ntp_spool/ntp4/ntp-4.2/${NTP_VERSION}.tar.gz
NTP_COMPILE_NTPLIB_C=false
NTP_STREAM_IOC_ROOT=${PWD}/ntp-gps-epics-server-and-opi-interfaces/IOC
NTP_IOC_NAME=ioc
NTP_CONFIGURED=false

STREAM_IOC_ROOT=/root/gciotto/stream-ioc

DTS_FOLDER=/lib/firmware
DEFAULT_SLOTS_FDR=/sys/devices/bone_capemgr.9/slots
OVERLAY_NAME=overlay_GPS

#
#  FUNCTION build_gpsd : it checks if gpsd and gpsd-clients
#  are available in current system. If not, it gets them from
#  Debian default repositories and sets them up. Besides, it
#  copies a custom gpsd.service into systemd lib folder.
#
#  It takes no parameters and produces no returns.

function build_gpsd {

	echo "Building GPSD"

	# Check if gspd packages are installed
	HAS_GPSD=true
	command -v gpsd > /dev/null 2>&1 || { HAS_GPSD=false;}

	if [ "$HAS_GPSD" = false ]; then

		echo -n "  - Installing GPSD (apt-get install gpsd)... "
		apt-get install gpsd -y > /dev/null || { echo "Failed" && exit 1; }
		echo "Ok"

		echo -n "  - Installing GPSD Clients (apt-get install gpsd-clients)... "
		apt-get install gpsd-clients -y > /dev/null || { echo "Failed" && exit 1; }
		echo "Ok"

	else
		printf "  - GPSD already installed... ${GREEN}Ok!${NC}\n"
	fi

	# Configuring gpsd.service
	if [ ! -f ${SYSTEM_MD_PATH}/gpsd.service ]; then

		echo -n "  - ${SYSTEM_MD_PATH}/gpsd.service not found! Copying to '${SYSTEM_MD_PATH}'... "
		cp ${PWD}/gpsd.service ${SYSTEM_MD_PATH}/gpsd.service
		echo "Ok"

	else
		echo -n  "  - ${SYSTEM_MD_PATH}/gpsd.service found! Would you like to replace it (y/n)? "
		read ANSWER

		if [ $ANSWER = "y" ]; then

			echo -n "  - Replacing /etc/systemd/system/gpsd.service... "
			cp ${PWD}/gpsd.service ${SYSTEM_MD_PATH}/gpsd.service
			echo "Ok"

		else
			printf "  - Keeping ${SYSTEM_MD_PATH}/gpsd.service... ${GREEN}Ok!${NC}\n"
		fi

	fi

}


function configure_ntpd {

	if [ "$NTP_CONFIGURED" = "false" ] ; then

		echo -n "  - Configuring NTP './configure --enable-ATOM --prefix=${NTP_ROOT} --enable-linuxcaps'..."

		( cd ${NTP_VERSION}/ && ./configure --enable-ATOM --prefix=${NTP_ROOT} --enable-linuxcaps &> ${NTP_CONFIGURE_LOG} )
		NTP_CONFIGURED=true

		echo "Ok"

	fi
}

function get_ntpd_sources {

	if  [ ! -d "${NTP_VERSION}/" ] ; then

		echo -n "  - Downloading NTP source files... "
		wget ${NTP_URL} > /dev/null 2>&1
		echo "Ok"

		echo -n "  - Extracting NTP source files... "
		tar -zxf ${NTP_VERSION}.tar.gz
		rm ${NTP_VERSION}.tar.gz
		echo "Ok"

	fi
}

function remove_ntpd_sources {

	if  [ -d "${NTP_VERSION}/" ] ; then

		echo -n "  - Removing NTPD sources..."
		rm -R ${NTP_VERSION}/
		echo "Ok"

	fi
}

#
#  FUNCTION build_ntpd : it downloads NTP source files and builds it
#  into ${NTP_ROOT} folder enabling ATOM driver. It also checks for all dependencies and
#  copies *.conf and *.services to /etc and ${SYSTEM_MD_PATH} directories, respectively. 
#
#  ntpd building logs are saved to ${NTP_CONFIGURE_LOG} (for ./configure command) and 
#  ${NTP_MAKE_LOG} (for make).
#
#  It takes no parameters and produces no returns.
#

function build_ntpd {

	echo "Building NTP"

	get_ntpd_sources

	echo -n "  - Building NTP dependencies... "
	apt-get install libcap-dev -y > /dev/null || { echo "Failed" && exit 1; }
	echo "Ok"

	configure_ntpd

	echo -n "  - Installing NTP in ${NTP_ROOT}... "
	( cd ${NTP_VERSION}/ && make install &> ${NTP_MAKE_LOG} )
	echo "Ok"

	echo -n "  - Copying ntpd.service to '${SYSTEM_MD_PATH}'... "
	cp ${PWD}/ntpd.service ${SYSTEM_MD_PATH}/ntpd.service
	echo "Ok"

	echo -n "  - Copying ntpdate.service in directory '${SYSTEM_MD_PATH}'... "
	cp ${PWD}/ntpdate.service ${SYSTEM_MD_PATH}/ntpdate.service
	echo "Ok"

	echo -n "  - Copying ntp.conf to '/etc/'... "
	cp ${PWD}/ntp.conf /etc/ntp.conf
	echo "Ok"

	echo -n "  - Copying ntpdate.conf to '/etc/'... "
	cp ${PWD}/ntpdate.conf /etc/ntpdate.conf
	echo "Ok"

}


function build_ioc {

	echo "Building IOC"

	get_ntpd_sources

	configure_ntpd

	echo -n "  - Cloning GIT repository..."
	git clone http://git.cnpem.br/gustavo.pinton/ntp-gps-epics-server-and-opi-interfaces.git > /dev/null 2>&1
	echo "Ok"

	echo -n "  - Cloning LIBBSMP repository..."
	git clone http://git.cnpem.br/bruno.martins/libbsmp.git > /dev/null 2>&1
	echo "Ok"

	apt-get install libgps-dev -y > /dev/null

	echo -n "  - Building LIBBSMP..."
	( cd libbsmp/ && make install > /dev/null 2>&1 )
	rm -R libbsmp/
	echo "Ok"

	( cd ${NTP_VERSION}/libntp/ && make libntp.a )

	( cd ${NTP_VERSION}/ntpq && make libntpq.a )

	### Compile IOC/ntpd_ioc.c
	gcc -std=c99 -Wall -O3 -I${PWD}/${NTP_VERSION} -I${PWD}/${NTP_VERSION}/include -I${PWD}/${NTP_VERSION}/ntpq -I${PWD}/${NTP_VERSION}/lib/isc/include -I${PWD}/${NTP_VERSION}/lib/isc/pthreads/include -I${PWD}/${NTP_VERSION}/lib/isc/unix/include -I${PWD}/${NTP_VERSION}/sntp/libopts -o ${NTP_STREAM_IOC_ROOT}/ntp_ioc.o -c ${NTP_STREAM_IOC_ROOT}/ntp_ioc.c

	### Compile IOC/gpsd_ioc.c
	gcc -std=c99 -Wall -O3 -o ${NTP_STREAM_IOC_ROOT}/gps_ioc.o -c ${NTP_STREAM_IOC_ROOT}/gps_ioc.c

	### Compile IOC/ioc_main.c
	gcc -std=c99 -Wall -O3 -o ${NTP_STREAM_IOC_ROOT}/ioc_main.o -c ${NTP_STREAM_IOC_ROOT}/ioc_main.c -I${PWD}/${NTP_VERSION} -I${PWD}/${NTP_VERSION}/include -I${PWD}/${NTP_VERSION}/ntpq -I${PWD}/${NTP_VERSION}/lib/isc/include -I${PWD}/${NTP_VERSION}/lib/isc/pthreads/include -I${PWD}/${NTP_VERSION}/lib/isc/unix/include -I${PWD}/${NTP_VERSION}/sntp/libopts

	### Link everything together
	g++ -o ${PWD}/${NTP_IOC_NAME}  ${NTP_STREAM_IOC_ROOT}/gps_ioc.o ${NTP_STREAM_IOC_ROOT}/ioc_main.o ${NTP_STREAM_IOC_ROOT}/ntp_ioc.o ${PWD}/${NTP_VERSION}/ntpq/libntpq.a ${PWD}/${NTP_VERSION}/libntp/libntp.a -lpthread -lbsmp -lm -lgps -lssl

	cp -f "ntp-gps-epics-server-and-opi-interfaces/IOC/Stream IOC/ntpd-gpsd.db"  ${STREAM_IOC_ROOT}/database
	cp -f "ntp-gps-epics-server-and-opi-interfaces/IOC/Stream IOC/ntpd-gpsd.proto"  ${STREAM_IOC_ROOT}/protocol
	cp -f "ntp-gps-epics-server-and-opi-interfaces/IOC/Stream IOC/stStream-IOC.cmd"  ${STREAM_IOC_ROOT}/iocBoot
}

#
#  FUNCTION build_python_libs : it gets and builds all python modules required to the
#  Python version of the EPICS server. 
#
#  It takes no parameters and produces no returns.
#
function build_python_libs {

	echo "Installing 'ntplib' and 'gps' python modules"

	wget -q  https://pypi.python.org/packages/29/8b/85a86e01c510665b0790d3a9fd4532ad98aba9e185a676113a0ae3879350/ntplib-0.3.3.tar.gz#md5=c7cc8e9b09f40c84819859d70b7784ca  || { echo "Failed"; exit 1; }
	tar -zxf ntplib-0.3.3.tar.gz
	rm ntplib-0.3.3.tar.gz
	( cd ntplib-0.3.3/ &&  python setup.py install  )
	rm -R ntplib-0.3.3/

	apt-get install python-gps -y > /dev/null || { echo "Failed"; }
	echo "Ok"
}


#
#  FUNCTION build_pps : it sets pps-tools packages up from Debian official
#  repositories. This package is required to obtain pps headers.
#
#  It takes no parameters and produces no returns.
#
function build_pps {

	echo "Building PPS tools"
	echo -n "  - Installing PPS Tools (apt-get install pps-tools)... "
	apt-get install pps-tools -y > /dev/null || { echo "Failed" && exit 1; }
	echo "Ok"
}

#
#  FUNCTION build_dts : compiles and copies device tree overlay file to ${DTS_FOLDER}.
#  If $SLOTS variable is not defined, it defines it as ${DEFAULT_SLOTS_FDR}.
#
#  It takes the path of a DTS file and produces no returns.
#
function build_dts {

	echo "Building Device Tree Source"

	echo -n "  - Compiling Device Tree Source... "

	if [ ! -f "$1" ]; then
		echo -n "  - Can't compile DTS because $1 does not exist. Exiting... "
		echo "Failed"
		exit 1;
	fi

	dtc -O dtb -o ${OVERLAY_NAME}-00A0.dtbo -b 0 -@ $1
	echo "Ok"

	echo "  - Copying files to ${DTS_FOLDER}... "
	cp ${OVERLAY_NAME}-00A0.dtbo ${DTS_FOLDER}
	rm ${OVERLAY_NAME}-00A0.dtbo
	echo "Ok"

	if [ -z ${SLOTS+x} ]; then
		echo " - SLOTS variable is not defined. Using it as '${DEFAULT_SLOTS_FDR}' instead..."
		SLOTS=${DEFAULT_SLOTS_FDR}
	fi

	echo "  - Enabling DTS... "
	echo ${OVERLAY_NAME} > $SLOTS
	echo "Ok"

	# Enable all services
	echo "Enabling overlay loading on startup"

	echo -n "  - Editing '/boot/uEnv.txt'"
	echo cape_enable=capemgr.enable_portno=${OVERLAY_NAME} >> /boot/uEnv.txt
	echo "Ok"

}

#
#  FUNCTION enable_services : copies and enable all services.
#
#  It takes the path of a DTS file and produces no returns.
#
function enable_services {

	echo "Enabling services"

	STARTED_GPSD=true

	echo -n "  - Starting gpsd.service..."
	systemctl restart gpsd.service > /dev/null 2>&1 || { echo "Failed" && STARTED_GPSD=false; }
	if ["$STARTED_GPSD" = true] ; then

		echo "Ok"
		echo -n "  - Enabling gpsd.service..."
		systemctl enable gpsd.service > /dev/null 2>&1 || { echo "Failed"; }
		echo "Ok"
	fi

	STARTED_NTPDATE=true

	echo -n "  - Starting ntpdate.service..."
	systemctl restart ntpdate.service > /dev/null 2>&1 || { echo "Failed" && STARTED_NTPDATE=false; }

	if ["$STARTED_NTPDATE" = true] ; then

		echo "Ok"
		echo -n "  - Enabling ntpdate.service..."
		systemctl enable ntpdate.service > /dev/null 2>&1 || { echo "Failed"; }
		echo "Ok"
	fi

	STARTED_NTPD=true

	systemctl restart ntpd.service > /dev/null 2>&1 || { echo "Failed" && STARTED_NTPD=false; }

	if ["$STARTED_NTPD" = true] ; then

		echo "Ok"
		echo -n "  - Enabling ntpd.service..."
		systemctl enable ntpd.service > /dev/null 2>&1 || { echo "Failed"; }
		echo "Ok"
	fi

	if [ "$1" == *"ioc"* ] ; then
		USE_IOC_SERVICE=true
	elif [ "$1" == *"python"* ] ; then
		USE_PCASPY_SERVICE=true
	else
		USE_IOC_SERVICE=true
	fi

	if [ "${USE_IOC_SERVICE}" = true ] ; then

		echo -n "  - Copying iocgpsd.service to '${SYSTEM_MD_PATH}'... "
                cp ${PWD}/iocgpsd.service ${SYSTEM_MD_PATH}/iocgpsd.service 
                echo "Ok"

                STARTED_IOCGPSD=true

                echo -n "  - Starting iocgpsd.service..."
                systemctl restart iocgpsd.service > /dev/null 2>&1 || { echo "Failed" && STARTED_IOCGPSD=false; }
                if ["$STARTED_IOCGPSD" = true] ; then

                        echo "Ok"
                        echo -n "  - Enabling iocgpsd.service..."
                        systemctl enable iocgpsd.service > /dev/null 2>&1 || { echo "Failed"; }
                        echo "Ok"
                fi


	fi

	if [ "${USE_PCASPY_SERVICE}" = true ] ; then

		echo -n "  - Copying pvgpsd.service to '${SYSTEM_MD_PATH}'... "
		cp ${PWD}/pvgpsd.service ${SYSTEM_MD_PATH}/pvgpsd.service 
		echo "Ok"

		STARTED_PVGPSD=true

		echo -n "  - Starting pvgpsd.service..."
		systemctl restart pvgpsd.service > /dev/null 2>&1 || { echo "Failed" && STARTED_PVGPSD=false; }
		if ["$STARTED_PVGPSD" = true] ; then

			echo "Ok"
			echo -n "  - Enabling pvgpsd.service..."
			systemctl enable pvgpsd.service > /dev/null 2>&1 || { echo "Failed"; }
			echo "Ok"
		fi
	fi

}


function show_options {

echo "Available options:"
echo " --build-gpsd : Build GPSD packages"
echo " --build-ntpd : Build NTPD source files"
echo " --build-ioc  : Build IOC binaries"
echo " --build-python-lib : Build python libs required to the EPICS python server"
echo " --build-pps : Retrieve pps headers"
echo " --build-dts [PATH] : Build DTS source file in PATH"
echo " --enable-services [ioc|python] : enable ioc or python EPICS server service"
echo " --all : execute all options above"
echo " --help : show options"

}

BUILD_GPS=false
BUILD_NTPD=false
BUILD_PYTHON_LIBS=false
BUILD_DTS=false
BUILD_PPS=false
ENABLE_SERVICES=false
BUILD_STREAM_IOC=false

if [[ $# -lt 1 ]] ; then
	show_options
	exit
fi

while [[ $# -ge 1 ]] ; 	do

	case $1 in
	    --build-gpsd)
	    BUILD_GPS=true
	    ;;
	    --build-ntpd)
	    BUILD_NTPD=true
	    ;;
	    --build-ioc)
	    NTP_COMPILE_NTPLIB_C=true
	    BUILD_STREAM_IOC=true
 	    ;;
	    --build-python-lib)
	    BUILD_PYTHON_LIBS=true
	    ;;
	    --build-pps)
	    BUILD_PPS=true
	    ;;
	    --build-dts)
	    BUILD_DTS=true
	    DTS=$2
	    shift
	    ;;
	    --enable-services)
	    ENABLE_SERVICES=true
            USE_IOC=$2
	    shift
	    ;;
	    --help)
	    show_options
	    exit
	    ;;
	    --all)
	    BUILD_GPS=true
   	    BUILD_NTPD=true
	    BUILD_PYTHON_LIBS=true
	    BUILD_DTS=true
	    BUILD_PPS=true
	    ENABLE_SERVICES=true
            USE_IOC=ioc
	    DTS=${PROJECT_DIR}/overlay_GPS_P9.dts
	    ;;
	    *)
	    show_options
	    exit
	    ;;
	esac
	shift
done

if [ "$BUILD_GPS" = true ] ; then
    build_gpsd
fi

if [ "$BUILD_NTPD" = true ] ; then
	build_ntpd
fi

if [ "$BUILD_PYTHON_LIBS" = true ] ; then
	build_python_libs
fi

if [ "$BUILD_PPS" = true ] ; then
	build_pps
fi

if [ "$BUILD_DTS" = true ] ; then
	build_dts ${DTS}
fi

if [ "$ENABLE_SERVICES" = true ] ; then
	enable_services ${USE_IOC}
fi

if [ "$BUILD_STREAM_IOC" = true ] ; then
	build_ioc
fi

if [ "$BUILD_GPS" = true ] || [ "$NTP_COMPILE_NTPLIB_C" = true ] ; then
	remove_ntpd_sources
fi
