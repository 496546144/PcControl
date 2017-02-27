package com.hbung.pccontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;
    SocketThread socketThread;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketThread.send("aaaaa" + count++);
            }
        });
        socketThread = new SocketThread();
        new Thread() {
            @Override
            public void run() {
                super.run();
                socketThread.init();
            }
        }.start();

    }
}
