#!/bin/bash

# Instead of creating new service files, it generates symbolic links
# to these files, which must be in the same directory this script is.
#
# Author: Gustavo CIOTTO PINTON

############## ARGUMENTS ###########################
# $1 = DTS file 
############## REQUIREMENTS ##############################
# ntpd.service ntpdate.service gpsd.service pvgpsd.service
# ntp.conf and ntpdate.conf in the same directory
##########################################################

############### BUILD GPSD #########################
# Checks if GPSD is already installed in the machine
####################################################

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

SYSTEM_MD_PATH=/lib/systemd/system

if [[ $# -ne 1 ]]; then
 echo "Format: build_ntp.sh arg1 (DTS file)"
 exit 1
fi

############################################################################################ GPSD
echo "(i) Building GPSD"

# Check if gspd packages are installed
hasGPSD=true
command -v gpsd > /dev/null 2>&1 || { hasGPSD=false;}

if [ "$hasGPSD" = false ]; then

	echo -n "  - Installing GPSD (apt-get install gpsd)... "

	apt-get install gpsd -y > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }

	printf "${GREEN}Ok!${NC}\n"
	
	echo -n "  - Installing GPSD Clients (apt-get install gpsd-clients)... "

	apt-get install gpsd-clients -y > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }

	printf "${GREEN}Ok!${NC}\n"

else
	printf "  - GPSD already installed... ${GREEN}Ok!${NC}\n"
fi


# Configuring gpsd.service
if [ ! -f ${SYSTEM_MD_PATH}/gpsd.service ]; then

	echo -n "  - /etc/systemd/system/gpsd.service not found! Adding symbolic link to '${SYSTEM_MD_PATH}'... "

	ln -s -f ${PWD}/gpsd.service ${SYSTEM_MD_PATH}/gpsd.service

	printf "${GREEN}Ok!${NC}\n"
		
else 
	echo -n  "  - /etc/systemd/system/gpsd.service found! Would you like to replace it (y/n)? "

	read answer
	
	if [ $answer = "y" ]; then

		echo -n "  - Replacing /etc/systemd/system/gpsd.service... "

		ln -s -f ${PWD}/gpsd.service ${SYSTEM_MD_PATH}/gpsd.service

		printf "${GREEN}Ok!${NC}\n"

	else

		printf "  - Keeping /etc/systemd/system/gpsd.service... ${GREEN}Ok!${NC}\n"

	fi

fi

############################################################################################ NTP
echo "(ii) Building NTP"


echo -n "  - Downloading NTP packages... "
wget http://www.eecis.udel.edu/~ntp/ntp_spool/ntp4/ntp-4.2/ntp-4.2.8p7.tar.gz > /dev/null 2>&1
printf "${GREEN}Ok!${NC}\n"

echo -n "  - Extracting NTP packages... "
tar -zxf ntp-4.2.8p7.tar.gz
rm ntp-4.2.8p7.tar.gz
printf "${GREEN}Ok!${NC}\n"

echo -n "  - Building dependences... "
apt-get install libcap-dev -y > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }
printf "${GREEN}Ok!${NC}\n"

echo -n "  - Configuring NTP './configure --enable-ATOM --prefix=/usr/local --enable-linuxcaps'..."
( cd ntp-4.2.8p7/ && ./configure --enable-ATOM --prefix=/usr/local --enable-linuxcaps &> ../config_ntp.log && make install &> ../make_ntp.log )

echo -n "  - Removing NTP source files... "
rm -R ntp-4.2.8p7/
printf "${GREEN}Ok!${NC}\n"

echo -n "  - Creating symbolic link to ntpd.service in directory '${SYSTEM_MD_PATH}'... "

############################################################################################ NTPD.SERVICE 
ln -s -f ${PWD}/ntpd.service ${SYSTEM_MD_PATH}/ntpd.service
############################################################################################ NTPD.SERVICE

printf "${GREEN}Ok!${NC}\n"

echo -n "  - Creating symbolic link to ntpdate.service in directory '${SYSTEM_MD_PATH}'... "

############################################################################################ NTPDATE.SERVICE
ln -s -f ${PWD}/ntpdate.service ${SYSTEM_MD_PATH}/ntpdate.service
############################################################################################ NTPDATE.SERVICE 

printf "${GREEN}Ok!${NC}\n"

echo -n "  - Creating symbolic link to ntp.conf in directory '/etc/'... "

############################################################################################ NTP.CONF
ln -s -f ${PWD}/ntp.conf /etc/ntp.conf
############################################################################################ NTP.CONF

############################################################################################ NTPDATE.CONF
ln -s -f ${PWD}/ntpdate.conf /etc/ntpdate.conf
############################################################################################ NTPDATE.CONF

printf "${GREEN}Ok!${NC}\n"

echo -n "  - Installing 'ntplib' and 'gps' python modules"

wget https://pypi.python.org/packages/29/8b/85a86e01c510665b0790d3a9fd4532ad98aba9e185a676113a0ae3879350/ntplib-0.3.3.tar.gz#md5=c7cc8e9b09f40c84819859d70b7784ca
tar -zxf ntplib-0.3.3.tar.gz
rm ntplib-0.3.3.tar.gz

( cd ntplib-0.3.3/ &&  python setup.py install )

rm -R ntplib-0.3.3/

apt-get install python-gps -y > /dev/null || { printf "${RED}Failed!${NC}\n"; }

printf "${GREEN}Ok!${NC}\n"

echo "(iii) Building PPS tools"

echo -n "  - Installing PPS Tools (apt-get install pps-tools)... "

apt-get install pps-tools -y > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }

printf "${GREEN}Ok!${NC}\n"

echo "(iv) Building DTS"

echo -n "  - Compiling DTS... "

if [ ! -f "$1" ]; then

	echo -n "  - Can't compile DTS because $1 does not exist. Exiting... "
	printf "${RED}Failed!${NC}\n"
	exit 1;

fi

dtc -O dtb -o overlay_GPS-00A0.dtbo -b 0 -@ $1
printf "${GREEN}Ok!${NC}\n"

echo "  - Copying files to /lib/firware... "
cp overlay_GPS-00A0.dtbo /lib/firmware
rm overlay_GPS-00A0.dtbo
printf "${GREEN}Ok!${NC}\n"

echo "  - Enabling DTS... "
echo overlay_GPS > $SLOTS
printf "${GREEN}Ok!${NC}\n"

# Enable all services
echo "(v) Enabling overlay loading on startup"

echo -n "  - Editing '/boot/uEnv.txt'"
echo cape_enable=capemgr.enable_portno=overlay_GPS >> /boot/uEnv.txt
printf "${GREEN}Ok!${NC}\n"

# Enable all services
echo "(vi) Enabling services"

echo -n "  - Creating symbolic link to  pvgpsd.service in directory '${SYSTEM_MD_PATH}'... "

############################################################################################ PVGPSD.SERVICE
ln -s -f ${PWD}/pvgpsd.service ${SYSTEM_MD_PATH}/pvgpsd.service
############################################################################################ PVGPSD.SERVICE 

printf "${GREEN}Ok!${NC}\n"

gpsd_success=true

echo -n "  - Starting gpsd.service..."
systemctl restart gpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n" && gpsd_success=false; }
if ["$gpsd_success" = true] ; then
	printf "${GREEN}Ok!${NC}\n"
	
	echo -n "  - Enabling gpsd.service..."
	systemctl enable gpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
	printf "${GREEN}Ok!${NC}\n"
fi

ntpdate_success=true

echo -n "  - Starting ntpdate.service..."
systemctl restart ntpdate.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n" && ntpdate_success=false; }

if ["$ntpdate_success" = true] ; then
	printf "${GREEN}Ok!${NC}\n"

	echo -n "  - Enabling ntpdate.service..."
	systemctl enable ntpdate.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
	printf "${GREEN}Ok!${NC}\n"
fi

ntpd_success=true

echo -n "  - Starting ntpd.service..."
systemctl restart ntpd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n" && ntpd_success=false; }

if ["$ntpd_success" = true] ; then
	printf "${GREEN}Ok!${NC}\n"
	
	echo -n "  - Enabling ntpd.service..."
	systemctl enable ntpd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
	printf "${GREEN}Ok!${NC}\n"
fi

pvgpsd_success=true

echo -n "  - Starting pvgpsd.service..."
systemctl restart pvgpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n" && pvgpsd_success=false; }
if ["$pvgpsd_success" = true] ; then
	printf "${GREEN}Ok!${NC}\n"
	
	echo -n "  - Enabling pvgpsd.service..."
	systemctl enable pvgpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
	printf "${GREEN}Ok!${NC}\n"
fi
