package com.example.bluetoothmeonjava;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    public static TextView textSpace;

    static TextView statusMessage;

    ConnectionThread connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.txtStatusMessage);
        textSpace = (TextView)findViewById(R.id.txtSpace);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            statusMessage.setText("Bluetooth not found.");
        }else{
            statusMessage.setText("Bluetooth found.");
        }

        if(!adapter.isEnabled()){
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, ENABLE_BLUETOOTH);
            appendOnView(statusMessage, "Requesting bluetooth activation.");
        }else{
            appendOnView(statusMessage, "Bluetooth active.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK){
                appendOnView(statusMessage, "Bluetooth activated.");
            }else{
                appendOnView(statusMessage, "Bluetooth not activated.");
            }
        }else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE){
            if(resultCode == RESULT_OK){
                String devName = data.getStringExtra("btDeviceName");
                String devAddress = data.getStringExtra("btDeviceAddress");
                appendOnView(statusMessage, "You selected\n\t" + devName + "\n\t" + devAddress);
                connection = new ConnectionThread(devAddress);
                connection.start();
            }else{
                appendOnView(statusMessage,"No device was selected.");
            }
        }
    }

    public void searchPairedDevices(View view){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    public void discoveredDevices(View view){
        Intent discoveryIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(discoveryIntent, SELECT_DISCOVERED_DEVICE);
    }

    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);
            if(dataString.equals("N")){
                appendOnView(statusMessage, "Error during connection.");
                //statusMessage.setText("Error during connection.");
            }else if(dataString.equals("S")){
                appendOnView(statusMessage, "Connection established.");
                //statusMessage.setText("Connection established.");
            }else{
                textSpace.append(new String(data));
                //textSpace.setText(new String(data));
            }
        }
    };

    public void sendMessage(View view){
        EditText messageBox = (EditText)findViewById(R.id.edtInput);
        String message = messageBox.getText().toString();
        byte[] data = message.getBytes();
        connection.write(data);
        messageBox.setText("");
    }

    static private void appendOnView(TextView view, String text){
        String output = statusMessage.getText() +
                "\n" + text;
        statusMessage.setText(output);
    }
}
