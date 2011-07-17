package com.philipbjorge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.philipbjorge.LtAEClient.R;

public class WindowList extends Activity {
	String ip;
	SocketChannel sChannel;
	String windows;
	CharsetDecoder decoder = Charset.forName("ISO-8859-1").newDecoder();
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ip = getIntent().getStringExtra("ip");
        // test
        setContentView(R.layout.windowlist);
        
    	boolean connected = false;
    	while (!connected) {
	    	try {
		    	sChannel = SocketChannel.open();
		        sChannel.configureBlocking(false);
		        sChannel.connect(new InetSocketAddress(InetAddress.getByName(ip), 55555));
		        
		        while (!sChannel.finishConnect()) {
		        }
		        
		        connected = true;
	    	} catch (Exception e) {}
    	}
    	// Socket is good to go.
    	while(connected) {
    		// reading
    		try {
				if(getWindows()) {
					// Clear buttons
					// for windows.split("|");
						// Add button with handler to send
				}
			} catch (IOException e) {}
    	}
    }
    
    private boolean getWindows() throws IOException {
    	ByteBuffer buf = ByteBuffer.allocate(48);
    	int bytesread = sChannel.read(buf);
    	if(bytesread == 0)
    	{
    		return false;
    	}
    	buf.flip();
    	String s = decoder.decode(buf).toString();
    	//windows = s.split("|");
    	windows = s;
    	buf.clear();
    	return true;
    }
}
