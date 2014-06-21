#!/bin/bash

# Sets up our Vagrant VM
# ----------------------
# NOTICE: 
# This script doesn't work as Vagrant provision script because it needs 
# active user input for Oracle Java installation.
#

# fix perl locale warning
cat /etc/environment | grep 'LC_ALL'
if [ "$?" -ne 0 ]
then
	echo 'LC_ALL="en_US.UTF-8"' | sudo tee -a /etc/environment > /dev/null
fi

# install utility programs
echo
echo "* Installing utility programs ..."
echo
sleep 1

sudo apt-get update -y \
&& sudo apt-get install -y unzip python-software-properties dos2unix git

if [ "$?" -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# install JDK 7
echo
echo "* Installing Oracle JDK 7 ..."
echo
sleep 1

sudo add-apt-repository ppa:webupd8team/java -y \
&& sudo apt-get update -y \
&& echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections \
&& echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections \
&& sudo apt-get install -y oracle-java7-installer

if [ "$?" -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# install MySQL
echo
echo "* Installing MySQL Server 5.5 ..."
echo
sleep 1

# set root password
sudo debconf-set-selections <<< 'mysql-server-5.5 mysql-server/root_password password waecm_2014' \
&& sudo debconf-set-selections <<< 'mysql-server-5.5 mysql-server/root_password_again password waecm_2014' \
&& sudo apt-get install -y mysql-server-5.5 \
&& sudo update-rc.d mysql defaults \
&& echo "CREATE DATABASE waecm_2014 CHARACTER SET utf8 COLLATE utf8_general_ci;" | mysql -uroot -pwaecm_2014

if [ "$?" -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# install NodeJS + npm + gulp.js
echo
echo "* Installing NodeJS + npm + gulp.js ..."
echo
sleep 1

sudo apt-get install -y nodejs npm \
&& sudo ln -s /usr/bin/nodejs /usr/bin/node \
&& sudo npm install -g gulp

if [ "$?" -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# install Ruby + SASS gem
echo
echo "* Installing Ruby + SASS ..."
echo
sleep 1

sudo apt-get install -y ruby-full \
&& sudo gem install sass rb-inotify

if [ "$?" -ne 0 ]
then
	echo "failed."
	exit 1;
fi

# download and extract Play framework
echo
echo "* Installing Play Framework ..."
echo
sleep 1

cd /vagrant
PLAY_VERSION=2.2.3
if [ -d "play-$PLAY_VERSION" ]
then
	echo "skipped."
else
	wget -nv "http://downloads.typesafe.com/play/$PLAY_VERSION/play-$PLAY_VERSION.zip" \
	&& unzip "play-$PLAY_VERSION.zip" \
	&& rm -f "play-$PLAY_VERSION.zip"

	if [ $? -ne 0 ]
	then
		echo "failed."
		rm -rf "play-$PLAY_VERSION"
		rm -f "play-$PLAY_VERSION.zip"
		exit 1;
	fi
fi

# add play commands to PATH
echo "$PATH" | grep "play-$PLAY_VERSION"
if [ "$?" -ne 0 ]
then
	PATH="$PATH:/vagrant/play-$PLAY_VERSION:/vagrant/play-$PLAY_VERSION/framework"
	export PATH

	# add new PATH to .bashrc
	cd /home/vagrant
	echo >> .bashrc
	echo -e "export PATH=\$PATH:/vagrant/play-$PLAY_VERSION:/vagrant/play-$PLAY_VERSION/framework" >> .bashrc
	. .bashrc
fi

# install npm dependencies for our project
echo
echo "* Installing NPM dependencies for project ..."
echo
sleep 1

cd /vagrant/csdr-g1
npm update

if [ "$?" -ne 0 ]
then
	echo "failed."
	exit 1;
fi

echo
echo "success."
echo

exit 0;
