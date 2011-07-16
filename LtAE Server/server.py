import pygtk
pygtk.require('2.0')
import gtk, wnck

class WindowLister:
    def on_btn_click(self, widget, data=None):
        window_list = wnck.screen_get_default()

        while gtk.events_pending():
        		gtk.main_iteration_do(False)

        window_list = window_list.get_windows()

        if len(window_list) == 0:
        		print "No Windows Found"
        for win in window_list:
            print win.get_name()

    def __init__(self):
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)

        self.button = gtk.Button("List Windows")
        self.button.connect("clicked", self.on_btn_click, None)

        self.window.add(self.button)
        self.window.show_all()

    def main(self):
        gtk.main()

if __name__ == "__main__":
    lister = WindowLister()
    lister.main()

