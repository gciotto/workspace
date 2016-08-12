#!/bin/bash

############## ARGUMENTS ###################
# $1 = gpsd.service
# $2 = ntp.conf location
# $3 = ntp install dir
# $4 = ntp.service dir
# $5 = DTS file 
############################################

############### BUILD GPSD #################
# Checks if GPSD is already installed in 
# the machine
############################################

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

if [[ $# -ne 5 ]]; then
 echo "Format: build_btp.sh arg1 (Path to gpsd.service) arg2 (Path to ntp.conf) arg3 (ntp install dir) arg4 (Path to ntp.service) arg5 (DTS file)"
 exit 1
fi

echo "############################### Building GPSD ###############################"

# Check if gspd packages are installed
hasGPSD=true
command -v gpsd > /dev/null 2>&1 || { hasGPSD=false;}

if [ "$hasGPSD" = false ]; then

	echo -n "Installing GPSD (apt-get install gpsd)... "

	apt-get install gpsd > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }

	printf "${GREEN}Ok!${NC}\n"
	
	echo -n "Installing GPSD Clients (apt-get install gpsd-clients)... "

	apt-get install gpsd-clients  > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }

	printf "${GREEN}Ok!${NC}\n"

else
	printf "GPSD already installed... ${GREEN}Ok!${NC}\n"
fi


# Configuring gpsd.service
if [ ! -f /etc/systemd/system/gpsd.service ]; then

	if [ ! -f "$1" ]; then

		echo -n "Can't copy gpsd.service because '$1' does not exist. Exiting... "
		printf "${RED}Failed!${NC}\n"

		exit 1;

	else

		echo -n "/etc/systemd/system/gpsd.service not found! Copying from file '$1'... "

		cp $1 /etc/systemd/system/ > /dev/null

		printf "${GREEN}Ok!${NC}\n"
	fi

else 
	echo -n  "/etc/systemd/system/gpsd.service found! Would you like to replace it (y/n)? "

	read answer
	
	if [ $answer = "y" ]; then

		if [ ! -f "$1" ]; then

			echo "Can't copy gpsd.service because $1 does not exist. Exiting..."

			exit 1;

		else

			echo -n "Replacing /etc/systemd/system/gpsd.service with '$1'... "

			cp $1 /etc/systemd/system/ > /dev/null

			printf "${GREEN}Ok!${NC}\n"

		fi
	else

		printf "Keeping /etc/systemd/system/gpsd.service... ${GREEN}Ok!${NC}\n"

	fi

fi

echo "############################### Building NTP ###############################"

if [ ! -d "$3" ]; then
	echo -n "Directory '$3' does not exist. Creating..."
	mkdir $3
	printf "${GREEN}Ok!${NC}\n"
fi

echo -n "Downloading NTP packages... "
wget http://www.eecis.udel.edu/~ntp/ntp_spool/ntp4/ntp-4.2/ntp-4.2.8p7.tar.gz > /dev/null 2>&1
printf "${GREEN}Ok!${NC}\n"

echo -n "Extracting NTP packages... "
tar -zxf ntp-4.2.8p7.tar.gz
rm ntp-4.2.8p7.tar.gz
printf "${GREEN}Ok!${NC}\n"

echo "Configuring NTP..."
( cd ntp-4.2.8p7/ && ./configure --enable-ATOM --prefix="$3" --enable-linuxcaps && make  install )

echo -n "Removing NTP source files... "
rm -R ntp-4.2.8p7/
printf "${GREEN}Ok!${NC}\n"

echo -n "Copying ntp.conf to directory /etc/ntp.conf... "

if [ ! -f "$2" ]; then

	echo -n "Can't copy ntp.conf because $2 does not exist. Exiting... "
	printf "${RED}Failed!${NC}\n"
	exit 1;

fi

cp $2 /etc/ 
printf "${GREEN}Ok!${NC}\n"

echo -n "Copying ntp.service to directory /etc/init.d/... "

if [ ! -f "$4" ]; then

	echo -n "Can't copy ntp.service because $3 does not exist. Exiting... "
	printf "${RED}Failed!${NC}\n"
	exit 1;

fi

cat > temp <<-_EOF_
#!/bin/sh

### BEGIN INIT INFO
# Provides:        ntp
# Required-Start:  $network $remote_fs $syslog
# Required-Stop:   $network $remote_fs $syslog
# Default-Start:   2 3 4 5
# Default-Stop:    1
# Short-Description: Start NTP daemon
### END INIT INFO

PATH=/sbin:/bin:$3sbin:$3bin::/usr/sbin:/usr/bin

. /lib/lsb/init-functions

DAEMON=$3/bin/ntpd
PIDFILE=/var/run/ntpd.pid
_EOF_

cat temp $4 > temp2
rm temp

cp temp2 /etc/init.d/
mv /etc/init.d/temp2 /etc/init.d/ntp

rm temp2

printf "${GREEN}Ok!${NC}\n"

echo "############################### Building DTS ###############################"
echo -n "Compiling DTS... "

if [ ! -f "$5" ]; then

	echo -n "Can't compile DTS because $5 does not exist. Exiting... "
	printf "${RED}Failed!${NC}\n"
	exit 1;

fi

dtc -O dtb -o overlay_GPS-00A0.dtbo -b 0 -@ $5
printf "${GREEN}Ok!${NC}\n"

echo "Copying files to /lib/firware... "
cp overlay_GPS-00A0.dtbo /lib/firmware
rm overlay_GPS-00A0.dtbo
printf "${GREEN}Ok!${NC}\n"

echo "Enabling DTS... "
echo overlay_GPS > $SLOTS
printf "${GREEN}Ok!${NC}\n"

# Enable all services
echo "############################# Enabling services #############################"
echo -n "Enabling gpsd.service..."
systemctl restart gpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n" && exit 1; }
printf "${GREEN}Ok!${NC}\n"

echo -n "Enabling ntp.service..."
systemctl restart ntp.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n" && exit 1; }
printf "${GREEN}Ok!${NC}\n"

