#######################################################
# Program to test the VoxBridge server.
#
# Pass p as parameter to use the primary server port
#	or s to use the secondary.
#######################################################

import socket
import sys

PRIMARY_SERVER_PORT = 23418
SECONDARY_SERVER_PORT = 23421

# Connect
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# get parameter from user
port = -1
par = sys.argv[1]
if par == 'p':
	port = PRIMARY_SERVER_PORT
elif par == 's':
	port = SECONDARY_SERVER_PORT
else:
	print('Error, argument not correct')
	sys.exit()

client.connect(('127.0.0.1', port))

# Welcome message
print('Connected on port ', port, '\n')
msg = client.recv(1024)
print(msg)

# get data from socket
while True:
	try:
		# ...it's terrible, I know...
		client.settimeout(0.001)
		msg = client.recv(1024)
		if len(msg) > 0:
			print(msg)
		
	except socket.timeout:
		continue

