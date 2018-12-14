package com.example.think.bluetoothaircraft;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SendMessageThread extends Thread {
    private BluetoothSocket mSocket;
    private String order;
    public SendMessageThread(BluetoothSocket mSocket,String order){
        this.mSocket = mSocket;
        this.order = order;
    }
    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        try {
            InputStream inputStream = mSocket.getInputStream();
            OutputStream outputStream = mSocket.getOutputStream();
            outputStream.write(order.getBytes("utf-8"));
            Log.e("Boomerr---test", "write ");
            int len;
            len = inputStream.read(buffer);
            String content = new String(buffer, 0, len);
            Log.e("Boomerr---test", "receive " + content);
            Log.e("Boomerr--test","5");
        } catch (IOException e) {
        }
    }
}
