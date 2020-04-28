package com.example.bluetoothmeonjava;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class PairedDevices extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ListView listView = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.text_header, listView, false);
        ((TextView)header.findViewById(R.id.textView)).setText("\n"+ getString(R.string.btn_paired_devices) +"\n");
        listView.addHeaderView(header, null, false);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        setListAdapter(arrayAdapter);
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        String item = (String)getListAdapter().getItem(position - 1);
        String deviceName = item.substring(0, item.indexOf("\n"));
        String deviceAddress = item.substring(item.indexOf("\n") + 1, item.length());
        Intent returnIntent = new Intent();
        returnIntent.putExtra("btDeviceName", deviceName);
        returnIntent.putExtra("btDeviceAddress", deviceAddress);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.menu_paired_devices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        /*
        if(id == R.id.action_settings){
            return true;
        }
         */
        return super.onOptionsItemSelected(item);
    }
}
