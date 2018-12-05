package com.example.think.bluetoothaircraft;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectThread extends Thread {
    public static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private BluetoothAdapter bluetoothAdapter;
    public ConnectThread(BluetoothAdapter bluetoothAdapter,BluetoothDevice device){
        Log.e("Boomerr---test","connectThread");
        BluetoothSocket tmp = null;
        mDevice = device;
        this.bluetoothAdapter = bluetoothAdapter;
        try{
            tmp = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            Log.e("Boomerr---test", String.valueOf(tmp));
        }catch(IOException e){}
        mSocket = tmp;
    }

    @Override
    public void run() {

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        try{
            Log.e("Boomerr---test","connect");
            mSocket.connect();
            InputStream inputStream = mSocket.getInputStream();
            OutputStream outputStream = mSocket.getOutputStream();
            byte[] buffer = new byte[1024];
            outputStream.write("你好".getBytes("utf-8"));
            Log.e("Boomerr---test","write ");
            int len;
            try{
                while((len = inputStream.read(buffer)) != -1){
                    String conten = new String(buffer,0,len);
                    Log.e("Boomerr---test","receive " + conten);
                }
            }catch (IOException e){}
        }catch (IOException connectException){
            Log.e("Boomerr--test","1");
            try{
                mSocket.close();
            }catch (IOException closeException){Log.e("Boomerr--test","2");}
            return ;
        }
    }

}
