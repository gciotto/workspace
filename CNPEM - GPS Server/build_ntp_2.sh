#!/bin/bash

############## ARGUMENTS ###################
# $1 = ntp install dir
# $2 = DTS file 
############################################

############### BUILD GPSD #################
# Checks if GPSD is already installed in 
# the machine
############################################

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

#SYSTEM_MD_PATH=/lib/systemd/system/
SYSTEM_MD_PATH=/home/gciotto/Desktop/


if [[ $# -ne 2 ]]; then
 echo "Format: build_ntp.sh arg1 (ntp install dir) arg2 (DTS file)"
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

else
	printf "GPSD already installed... ${GREEN}Ok!${NC}\n"
fi


# Configuring gpsd.service
if [ ! -f ${SYSTEM_MD_PATH}/gpsd.service ]; then

	echo -n "/etc/systemd/system/gpsd.service not found! Adding file in '${SYSTEM_MD_PATH}'... "

	cat > ${SYSTEM_MD_PATH}/gpsd.service <<- _EOF_
	[Unit]
	Description=GPS (Global Positioning System) Daemon
	Requires=gpsd.socket
	
	[Service]
	ExecStart=/usr/sbin/gpsd -n -N /dev/ttyO4
	
	[Install]
	Also=gpsd.socket
	_EOF_

	printf "${GREEN}Ok!${NC}\n"
		

else 
	echo -n  "/etc/systemd/system/gpsd.service found! Would you like to replace it (y/n)? "

	read answer
	
	if [ $answer = "y" ]; then

		echo -n "Replacing /etc/systemd/system/gpsd.service... "

		cat > ${SYSTEM_MD_PATH}/gpsd.service <<- _EOF_
		[Unit]
		Description=GPS (Global Positioning System) Daemon
		Requires=gpsd.socket
		
		[Service]
		ExecStartPre=echo overlay_GPS > $SLOTS && sleep 2
		ExecStart=/usr/sbin/gpsd -n -N /dev/ttyO4
		
		[Install]
		Also=gpsd.socket
		WantedBy=ntpd.service
		_EOF_

		printf "${GREEN}Ok!${NC}\n"

	else

		printf "Keeping /etc/systemd/system/gpsd.service... ${GREEN}Ok!${NC}\n"

	fi

fi

echo "############################### Building NTP ###############################"

if [ ! -d "$1" ]; then
	echo -n "Directory '$1' does not exist. Creating..."
	mkdir $1
	printf "${GREEN}Ok!${NC}\n"
fi

echo -n "Downloading NTP packages... "
wget http://www.eecis.udel.edu/~ntp/ntp_spool/ntp4/ntp-4.2/ntp-4.2.8p7.tar.gz > /dev/null 2>&1
printf "${GREEN}Ok!${NC}\n"

echo -n "Extracting NTP packages... "
tar -zxf ntp-4.2.8p7.tar.gz
rm ntp-4.2.8p7.tar.gz
printf "${GREEN}Ok!${NC}\n"

echo -n "Building dependences... "
apt-get install libcap-dev > /dev/null || { printf "${RED}Failed!${NC}\n" && exit 1; }
printf "${GREEN}Ok!${NC}\n"

echo "Configuring NTP './configure --enable-ATOM --prefix="$1" --enable-linuxcaps'..."
( cd ntp-4.2.8p7/ && ./configure --enable-ATOM --prefix="$1" --enable-linuxcaps && make install )

echo -n "Removing NTP source files... "
rm -R ntp-4.2.8p7/
printf "${GREEN}Ok!${NC}\n"

echo -n "Creating ntpd.service in directory '${SYSTEM_MD_PATH}'... "

cat > ${SYSTEM_MD_PATH}/ntpd.service <<- _EOF_
[Unit]
Description=Network Time Service
Requires=gpsd.service
After=network.target
 
[Service]
Type=forking
PIDFile=/run/ntpd.pid
ExecStart=/root/gciotto/GPS_server/ntp-install/sbin/ntpd -p /run/ntpd.pid 
 
[Install]
WantedBy=multi-user.target
_EOF_
printf "${GREEN}Ok!${NC}\n"

echo -n "Creating ntpdate.service in directory '${SYSTEM_MD_PATH}'... "

cat > ${SYSTEM_MD_PATH}/ntpdate.service <<- _EOF_
[Unit]
Description=Network Time Service (one-shot ntpdate mode)
Before=ntpd.service
 
[Service]
Type=oneshot
ExecStart=/root/gciotto/GPS_server/ntp-install/sbin/ntpd -q -g -x -c /etc/ntpdate.conf
ExecStart=/sbin/hwclock --systohc
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
_EOF_

printf "${GREEN}Ok!${NC}\n"

echo -n "Creating ntp.conf in directory '/etc/'... "

cat > /etc/ntp.conf <<- _EOF_
# /etc/ntp.conf, configuration for ntpd; see ntp.conf(5) for help

driftfile /var/lib/ntp/ntp.drift


# Enable this if you want statistics to be logged.
#statsdir /var/log/ntpstats/

statistics loopstats peerstats clockstats
filegen loopstats file loopstats type day enable
filegen peerstats file peerstats type day enable
filegen clockstats file clockstats type day enable

# LOCAL CLOCK
server 127.127.1.0
fudge  127.127.1.0 stratum 10

# Data from GPS - Shared Memory - Refer to GPSD man page.
server 127.127.28.0 minpoll 4 maxpoll 4 iburst prefer
fudge 127.127.28.0 refid GPS

# PPS signal - Refer to pps Clock Discipline Page
# pps /dev/pps0 assert hardpps
server 127.127.22.0 minpoll 3 maxpoll 3
fudge 127.127.22.0  refid PPS # PPS Driver

# Access control configuration; see /usr/share/doc/ntp-doc/html/accopt.html for
# details.  The web page <http://support.ntp.org/bin/view/Support/AccessRestrictions>
# might also be helpful.
#
# Note that "restrict" applies to both servers and clients, so a configuration
# that might be intended to block requests from certain clients could also end
# up blocking replies from your own upstream servers.

# By default, exchange time with everybody, but don't allow configuration.
restrict -4 default kod notrap nomodify nopeer noquery
restrict -6 default kod notrap nomodify nopeer noquery

# Local users may interrogate the ntp server more closely.
restrict 127.0.0.1
restrict ::1

# Clients from this (example!) subnet have unlimited access, but only if
# cryptographically authenticated.
#restrict 192.168.123.0 mask 255.255.255.0 notrust


# If you want to provide time to your local subnet, change the next line.
# (Again, the address is an example only.)
#broadcast 192.168.123.255

# If you want to listen to time broadcasts on your local subnet, de-comment the
# next lines.  Please do this only if you trust everybody on the network!
#disable auth
#broadcastclient

_EOF_

cat > /etc/ntpdate.conf <<- _EOF_
# /etc/ntp.conf, configuration for ntpd; see ntp.conf(5) for help

driftfile /var/lib/ntp/ntp.drift


# Enable this if you want statistics to be logged.
#statsdir /var/log/ntpstats/

statistics loopstats peerstats clockstats
filegen loopstats file loopstats type day enable
filegen peerstats file peerstats type day enable
filegen clockstats file clockstats type day enable

# LOCAL CLOCK
server 127.127.1.0
fudge  127.127.1.0 stratum 10

# Data from GPS - Shared Memory - Refer to GPSD man page.
server 127.127.28.0 minpoll 4 maxpoll 4 iburst prefer
fudge 127.127.28.0 refid GPS

# Access control configuration; see /usr/share/doc/ntp-doc/html/accopt.html for
# details.  The web page <http://support.ntp.org/bin/view/Support/AccessRestrictions>
# might also be helpful.
#
# Note that "restrict" applies to both servers and clients, so a configuration
# that might be intended to block requests from certain clients could also end
# up blocking replies from your own upstream servers.

# By default, exchange time with everybody, but don't allow configuration.
restrict -4 default kod notrap nomodify nopeer noquery
restrict -6 default kod notrap nomodify nopeer noquery

# Local users may interrogate the ntp server more closely.
restrict 127.0.0.1
restrict ::1

# Clients from this (example!) subnet have unlimited access, but only if
# cryptographically authenticated.
#restrict 192.168.123.0 mask 255.255.255.0 notrust


# If you want to provide time to your local subnet, change the next line.
# (Again, the address is an example only.)
#broadcast 192.168.123.255

# If you want to listen to time broadcasts on your local subnet, de-comment the
# next lines.  Please do this only if you trust everybody on the network!
#disable auth
#broadcastclient

_EOF_

printf "${GREEN}Ok!${NC}\n"

echo "############################### Building DTS ###############################"
echo -n "Compiling DTS... "

if [ ! -f "$2" ]; then

	echo -n "Can't compile DTS because $2 does not exist. Exiting... "
	printf "${RED}Failed!${NC}\n"
	exit 1;

fi

dtc -O dtb -o overlay_GPS-00A0.dtbo -b 0 -@ $2
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
systemctl restart gpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
systemctl enable gpsd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
printf "${GREEN}Ok!${NC}\n"

echo -n "Enabling ntpdate.service..."
systemctl restart ntpdate.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
systemctl enable ntpdate.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
printf "${GREEN}Ok!${NC}\n"

echo -n "Enabling ntpd.service..."
systemctl restart ntpd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
systemctl enable ntpd.service > /dev/null 2>&1 || { printf "${RED}Failed!${NC}\n"; }
printf "${GREEN}Ok!${NC}\n"