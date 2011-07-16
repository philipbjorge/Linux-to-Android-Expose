import pygtk
pygtk.require('2.0')
import gtk, wnck
import socket

host = ''
port = 55555

myServerSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
myServerSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
myServerSocket.bind((host, port))
myServerSocket.listen(1)

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.connect(("gmail.com",80))
ip = s.getsockname()
s.close()

print "Server is on ip " + str(ip)
print "Server is running on port %d; press Ctrl-C to terminate." % port

while 1:
    clientsock, clientaddr = myServerSocket.accept()
    clientfile = clientsock.makefile('rw', 0)
    clientfile.write("Welcome, " + str(clientaddr) + "\n")
    clientfile.write("Please enter a string: ")
    line = clientfile.readline().strip()
    clientfile.write("You entered %d characters.\n" % len(line))
    clientfile.close()
    clientsock.close()

