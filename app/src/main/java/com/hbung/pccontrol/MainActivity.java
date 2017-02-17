package com.hbung.pccontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    PrintWriter printWriter = null;
    private Button button;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (printWriter != null) {
                    printWriter.println("aaaaaaaaaaaaaaaaaaaa");
                    printWriter.flush();
                }

            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();
                linkService();
            }
        }.start();

    }


    private void linkService() {

        OutputStream os = null;
        InputStream is = null;
        try {
            socket = new Socket(InetAddress.getByName("192.168.2.55"), 1867);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            printWriter = new PrintWriter(os);
            os.write("aaaaaaaaaa".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
    /**
     * 作者　　: 李坤
     * 创建时间: 2017/2/17 16:57
     *  try {
     if (is != null)
     is.close();
     if (os != null)
     os.close();
     if (printWriter != null) {
     printWriter.close();
     printWriter = null;
     }
     } catch (IOException e) {
     e.printStackTrace();
     }
     * 方法功能：
     */

}
