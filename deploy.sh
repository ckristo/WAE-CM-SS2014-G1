#!/bin/sh
server="localhost"
user="vagrant"

ssh "$user@$server" < remotecommands
