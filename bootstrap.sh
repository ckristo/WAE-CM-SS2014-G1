#!/usr/bin/bash

# Sets up our Vagrant VM
# NOTICE:
# - This script doesn't work as Vagrant provision script
#   because it needs active user input!

# install utility programs
echo
echo "* Installing utility programs ..."
echo
sleep 3

sudo apt-get update -y \
&& sudo apt-get install unzip -y

# install JDK 7
echo
echo "* Installing Oracle JDK 7 (user input required) ..."
echo
sleep 3

sudo add-apt-repository ppa:webupd8team/java -y \
&& sudo apt-get update -y \
&& sudo apt-get install oracle-java7-installer -y

# download and extract Play framework
echo
echo "* Installing Play Framework ..."
echo
sleep 3

PLAY_VERSION=2.2.3
cd /home/vagrant
wget http://downloads.typesafe.com/play/$PLAY_VERSION/play-$PLAY_VERSION.zip \
&& unzip play-$PLAY_VERSION.zip \
&& rm -f play-$PLAY_VERSION.zip

# export new PATH
export PATH=$PATH:/home/vagrant/play-$PLAY_VERSION:/home/vagrant/play-$PLAY_VERSION/framework

# add new PATH to .bashrc
echo >> .bashrc
echo -e "# Add Play executables to PATH" >> .bashrc
echo -e "export PATH=\$PATH:/home/vagrant/play-$PLAY_VERSION:/home/vagrant/play-$PLAY_VERSION/framework" >> .bashrc
source .bashrc

play help
