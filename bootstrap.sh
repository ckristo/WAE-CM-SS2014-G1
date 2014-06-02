#!/bin/bash

# Sets up our Vagrant VM
# ----------------------
# NOTICE: 
# This script doesn't work as Vagrant provision script because it needs 
# active user input for Oracle Java installation.
#

# install utility programs
echo
echo "* Installing utility programs ..."
echo
sleep 3

sudo apt-get update -y \
&& sudo apt-get install -y unzip python-software-properties

if [ $? -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# install JDK 7
echo
echo "* Installing Oracle JDK 7 (user input required) ..."
echo
sleep 3

sudo add-apt-repository ppa:webupd8team/java -y \
&& sudo apt-get update -y \
&& sudo apt-get install -y oracle-java7-installer

if [ $? -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# download and extract Play framework
echo
echo "* Installing Play Framework ..."
echo
sleep 3

PLAY_VERSION=2.2.3
if [ -d play-$PLAY_VERSION ]
then
	echo "skipped."
else
	cd /vagrant
	wget http://downloads.typesafe.com/play/$PLAY_VERSION/play-$PLAY_VERSION.zip \
	&& unzip play-$PLAY_VERSION.zip \
	&& rm -f play-$PLAY_VERSION.zip

	if [ $? -ne 0 ]
	then
		echo "failed."
		rm -rf play-$PLAY_VERSION
		rm -f play-$PLAY_VERSION.zip
		exit 1;
	fi
	
	# export new PATH
	PATH=$PATH:/vagrant/play-$PLAY_VERSION:/vagrant/play-$PLAY_VERSION/framework
	export PATH
	
	# add new PATH to .bashrc
	cd /home/vagrant
	echo >> .bashrc
	echo -e "export PATH=\$PATH:/vagrant/play-$PLAY_VERSION:/vagrant/play-$PLAY_VERSION/framework" >> .bashrc
	
	# show play help
	play help
fi

echo
echo "success."

exit 0;
