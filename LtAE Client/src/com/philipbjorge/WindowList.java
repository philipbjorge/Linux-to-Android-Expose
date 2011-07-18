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
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.philipbjorge.LtAEClient.R;

public class WindowList extends Activity {
	String ip;
	SocketChannel sChannel;
	String windows;
	CharsetDecoder decoder = Charset.forName("ISO-8859-1").newDecoder();
	CharsetEncoder enc = Charset.forName("US-ASCII").newEncoder();  
	ArrayList<String> lvContents;
	ArrayAdapter<String> myListAdapter;
	ListView lv;
	TCP tcpTask;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windowlist);
        
        myListAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        
        lv = (ListView)findViewById(R.id.listView1);
        lv.setAdapter(myListAdapter);
        
        ip = getIntent().getStringExtra("ip");   
        tcpTask = new TCP();
        tcpTask.execute(ip);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
              // When clicked, send Textview text to server
            	try {
            		String command = ((TextView) view).getText().toString();
            		command += "\n";
            		
            		ByteBuffer buf = ByteBuffer.allocate(24);
            		buf.clear();
            		buf.put(command.getBytes());
            	    // Prepare the buffer for reading by the socket
            	    buf.flip();
            		
            	    // Write bytes
            		while(buf.hasRemaining()) {
            			tcpTask.sChannel.write(buf);
            		}
            		
            	} catch (Exception e) {}
            }
          });
        //Thread thread = new Thread(new TCP(ip, myListAdapter));
        //thread.start();
    }
    
    
    //
    // TCP AsyncTask
    //
    protected class TCP extends AsyncTask<String,String,Void> {
    	String ip;
    	public SocketChannel sChannel;
    	String windows;
    	CharsetDecoder decoder = Charset.forName("ISO-8859-1").newDecoder();
    	
    	@Override
    	protected void onPreExecute() {
    		windows = "";
    		myListAdapter.setNotifyOnChange(true);
            myListAdapter.add("Connecting...");
    	}
    	
    	@Override
    	protected Void doInBackground(String... params) {
    		
    		ip = params[0];
    		
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
        		// writing handled in button event
        		// reading
        		try {
    				if(getWindows()) {
    					// TODO: Publish message
    					// TODO: on reciept of message, clear, and add exploded
    					publishProgress(windows);
    				}
    			} catch (IOException e) {}
        	}
    		
    		return null;
    	}
        
        // -- called from the publish progress 
        // -- notice that the datatype of the second param gets passed to this method
        @Override
        protected void onProgressUpdate(String... values) 
        {
        	myListAdapter.clear();
        	String[] exploded = values[0].split("\\|");
        	for(String id : exploded) {
        		myListAdapter.add(id);
        	}
        }
    	
        private boolean getWindows() throws IOException {
        	ByteBuffer buf = ByteBuffer.allocate(1024);
        	int bytesread = sChannel.read(buf);
        	if(bytesread == 0)
        	{
        		return false;
        	}
        	buf.flip();
        	String s = decoder.decode(buf).toString();
        	windows = s;
        	buf.clear();
        	return true;
        }
    }
}
