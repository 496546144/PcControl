package com.hbung.pccontrol;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/2/17　16:13
 * 邮箱　　：49654614@qq.com
 * <p>
 * 功能介绍：
 */

public class SuperSocket {
    Socket socket;
    String ip = "192.168.2.55";
    private int sotimeout = 3000;
    private int port = 1867;
    public boolean isLinkSuccess = false;
    OnCallback onCallback = null;
    Context context;
    Handler handler;

    public interface OnCallback {
        void onLinkComplete(boolean isSuccess);
    }

    public SuperSocket(Context context) {
        handler = new MainHander(context.getMainLooper());
    }

    public void setOnCallback(OnCallback onCallback) {
        this.onCallback = onCallback;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                init();
            }
        }.start();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void init() {
        try {
            isLinkSuccess = false;
            ip = scanIp(getIps());
            if (ip == null) return;
            socket = new Socket(InetAddress.getByName(ip), port);
            socket.setKeepAlive(true);//开启保持活动状态的套接字
            socket.setSoTimeout(sotimeout);//设置超时时间
            isLinkSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handler.sendEmptyMessage(0x10);
        }
    }

    private void linkResult(boolean isSuccess) {
        isLinkSuccess = isSuccess;
        handler.sendEmptyMessage(0x10);
    }

    /**
     * 发送数据，发送失败返回false,发送成功返回true
     *
     * @param message
     * @return
     */
    public Boolean send(String message) {
        if (socket == null) {
            return false;
        }
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            return true;
        } catch (Exception se) {
            se.printStackTrace();
            socket = null;
            linkResult(false);
            return false;
        }
    }

    /**
     * 读取数据，返回字符串类型
     *
     * @return
     */
    public String read() {
        try {
            StringBuilder sb = new StringBuilder();
            socket.setSoTimeout(sotimeout);
            InputStream input = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            char[] sn = new char[1024];
            int len = -1;
            while ((len = in.read(sn)) > 0) {
                sb.append(sn, 0, len);
            }
            return sb.toString();
        } catch (IOException se) {
            socket = null;
            linkResult(false);
            return null;
        }
    }

    //扫描ip
    public String scanIp(List<String> ips) {
        String res = null;
        for (String ip : ips) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 300);
                if (send("scan")) {
                    res = ip;
                    break;
                }
            } catch (Exception ex) {
            } finally {
                try {
                    if (null != socket)
                        socket.close();
                } catch (Exception ex) {
                }
            }
        }
        return res;
    }

    //获取本段的ip列表
    public List<String> getIps() throws UnknownHostException, NumberFormatException {
        List<String> ips = new ArrayList<>();
        InetAddress address = getLocalIpAddress();
        byte[] ipStr = address.getAddress();
        if (ipStr != null && ipStr.length == 4) {
            int end = ipStr[3] & 0xff;
            String ipStart = (ipStr[0] & 0xff) + "." + (ipStr[1] & 0xff) + "." + (ipStr[2] & 0xff);
            for (int i = 2; i <= 0xff; i++) {
                if (i != end) {
                    String resIp = ipStart + "." + i;
                    ips.add(resIp);
                }
            }
        }
        return ips;
    }


    //获取本机ip
    public InetAddress getLocalIpAddress() {

        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        return ia;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return null;

    }

    class MainHander extends Handler {
        public MainHander(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x10) {
                if (onCallback != null) {
                    onCallback.onLinkComplete(isLinkSuccess);
                }
            }
        }
    }
}
