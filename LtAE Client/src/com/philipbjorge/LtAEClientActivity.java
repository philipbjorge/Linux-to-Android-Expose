package com.philipbjorge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.philipbjorge.LtAEClient.R;

public class LtAEClientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button button = (Button) findViewById(R.id.button1);
        final EditText et = (EditText) findViewById(R.id.editText1);
        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	// Start window list activity with host
            	Intent wl = new Intent(v.getContext(), WindowList.class);
            	wl.putExtra("ip", et.getText().toString());
            	startActivity(wl);
            }
        });
    }
}