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

clientsock, clientaddr = myServerSocket.accept()
print 'Connected by', clientaddr

running = True

clientfile = clientsock.makefile('rw', 0)
screen = wnck.screen_get_default()

while gtk.events_pending():
		gtk.main_iteration_do(False)

while running:
		clientfile.write("Welcome, " + str(clientaddr) + "\n")
		window_list = screen.get_windows()

		if len(window_list) == 0:
				clientfile.write("No Windows Found")
		for win in window_list:
				clientfile.write(str(win.get_xid())+",")

		clientfile.write("Please enter a window: ")
		line = clientfile.readline().strip()

		w = wnck.window_get(int(line))
		w.activate(1)

clientfile.close()
clientsock.close()

