package com.example.think.bluetoothaircraft;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class WorkActivity extends Activity implements View.OnClickListener ,BluetoothInfoAdapter.OnItemClickListener{
    private Button openBluetooth;
    private Button closeBluetooth;
    private Button selectBluetooth;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BluetoothInfoAdapter bluetoothInfoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        permissionRequest();
        initView();
    }

    private void permissionRequest() {
        ActivityCompat.requestPermissions(WorkActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"权限已授予",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        openBluetooth = (Button) findViewById(R.id.openBluetooth);
        closeBluetooth = (Button) findViewById(R.id.closeBluetooth);
        selectBluetooth = (Button) findViewById(R.id.selectBluetooth);
        openBluetooth.setOnClickListener(this);
        closeBluetooth.setOnClickListener(this);
        selectBluetooth.setOnClickListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothInfoAdapter = new BluetoothInfoAdapter(arrayList);
        bluetoothInfoAdapter.setItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bluetoothInfoAdapter);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.openBluetooth:
               openBluetoothFunction();
               break;
            case R.id.closeBluetooth:
                closeBluetoothFunction();
                break;
            case R.id.selectBluetooth:
                selectBluetoothFunction();
                break;
        }
    }

    private void selectBluetoothFunction() {
        arrayList.clear();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                arrayList.add(device);
            }
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver,intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    private void closeBluetoothFunction() {
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();
            Toast.makeText(this,"蓝牙已关闭",Toast.LENGTH_SHORT).show();
        }
    }

    private void openBluetoothFunction() {
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            Toast.makeText(this,"蓝牙已开启",Toast.LENGTH_SHORT).show();
        }
    }
    protected final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("Boomerr---action",action);
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName() != null){
                    Log.e("Boomerr---device",device.getName());
                    if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                        arrayList.add(device);
                        Log.e("Boomerr---test--size", String.valueOf(arrayList.size()));
                        bluetoothInfoAdapter.notifyDataSetChanged();
                    }
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Toast.makeText(WorkActivity.this,"搜索完毕",Toast.LENGTH_SHORT).show();
            }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(WorkActivity.this,"开始搜索",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onItemClick(int position) {
        Log.e("Boomerr--bond",arrayList.get(position).getName());
        try {
            ClsUtils.setPin(arrayList.get(position).getClass(),arrayList.get(position),"1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnectThread connectThread = new ConnectThread(bluetoothAdapter,arrayList.get(position));
        connectThread.start();
    }
}
