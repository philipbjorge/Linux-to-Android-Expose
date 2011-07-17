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
clientsock.setblocking(0)
print 'Connected by', clientaddr

running = True

clientfile = clientsock.makefile('rw', 0)

screen = wnck.screen_get_default()

while gtk.events_pending():
		gtk.main_iteration_do(False)

window_list = screen.get_windows()

while running:
		while gtk.events_pending():
				gtk.main_iteration_do(False)

		window_list_new = screen.get_windows()
		if(len(set(window_list_new) ^ set(window_list)) != 0):
				window_list = window_list_new
				if len(window_list) != 0:
						s = ""
						for win in window_list:
								s += str(win.get_xid())+"|"
						try:
								clientfile.write(s+'|'+'\n')
						except Exception:
								pass
				else:
						clientfile.write("No windows open.")

		try:
			line = clientfile.readline().strip()
			w = wnck.window_get(int(line))
			w.activate(1)
		except Exception:
			pass

clientfile.close()
clientsock.close()

