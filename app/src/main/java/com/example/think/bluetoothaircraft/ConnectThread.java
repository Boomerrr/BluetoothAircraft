package com.example.think.bluetoothaircraft;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectThread extends Thread {

    public BluetoothSocket mSocket;
    public static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private final BluetoothDevice mDevice;
    private BluetoothAdapter bluetoothAdapter;
    public Handler handler;
    public ConnectThread(BluetoothAdapter bluetoothAdapter, BluetoothDevice device ){
        Log.e("Boomerr---test","connectThread");
        BluetoothSocket tmp = null;
        mDevice = device;
        this.bluetoothAdapter = bluetoothAdapter;
        try{
            tmp = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            Log.e("Boomerr---test", String.valueOf(tmp));
        }catch(IOException e){}
        mSocket = tmp;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String order = bundle.getString("order");
                if(order != null){
                    Log.e("Boomerr---order",order);

                }
            }
        };
    }

    @Override
    public void run() {

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        try{
            Log.e("Boomerr---test","connect");
            mSocket.connect();
        }catch (IOException connectException){
            Log.e("Boomerr--test","1");
            try{
                mSocket.close();
            }catch (IOException closeException){Log.e("Boomerr--test","2");}
            return ;
        }
       // Log.e("Boomerr---test","4");
    }

}
