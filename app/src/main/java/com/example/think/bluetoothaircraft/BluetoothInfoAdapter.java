package com.example.think.bluetoothaircraft;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class BluetoothInfoAdapter extends RecyclerView.Adapter<BluetoothInfoAdapter.ViewHolder> implements View.OnClickListener{
    private ArrayList<BluetoothDevice> bluetoothInfoList;
    private OnItemClickListener itemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
    public BluetoothInfoAdapter(ArrayList<BluetoothDevice> bluetoothInfoList){
        this.bluetoothInfoList = bluetoothInfoList;
    }
    @NonNull
    @Override
    public BluetoothInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bluetooth,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothInfoAdapter.ViewHolder viewHolder, int i) {
        String string = bluetoothInfoList.get(i).getName() + "   " + bluetoothInfoList.get(i).getAddress();
        viewHolder.textView.setText(string);
        viewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return bluetoothInfoList.size();
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener != null){
            itemClickListener.onItemClick((Integer)v.getTag());
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
