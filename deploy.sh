#!/bin/sh

#server IP/hostname
server="localhost"
#username of deploy-user on the server
user="vagrant"

#the script file remotecommands gets executed on the server
ssh "$user@$server" < remotecommands > /dev/null
