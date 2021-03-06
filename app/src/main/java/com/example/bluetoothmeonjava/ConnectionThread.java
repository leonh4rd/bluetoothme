package com.example.bluetoothmeonjava;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

public class ConnectionThread extends Thread {

    BluetoothDevice remoteDevice;
    BluetoothSocket socket = null;
    BluetoothServerSocket serverSocket = null;
    InputStream input = null;
    OutputStream output = null;
    String remoteDeviceAddress = null;
    String Uuid = "00001101-0000-1000-8000-00805F9B34FB";
    boolean server;
    boolean running = false;
    boolean isConnected = false;

    public Handler UIHandler;

    public ConnectionThread(Handler handler){
        UIHandler = handler;
        server = true;
    }

    public ConnectionThread(Handler handler, String address){
        this(handler);
        server = false;
        remoteDeviceAddress = address;
    }

    public void run(){
        running = true;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(server){
            try{
               serverSocket = adapter.listenUsingRfcommWithServiceRecord("SuperBluetooth", UUID.fromString(Uuid));
               socket = serverSocket.accept();
               if(socket != null){
                    serverSocket.close();
               }
            }catch(IOException e){
                e.printStackTrace();
                //toMainActivity("N".getBytes());
            }
        }else{
            try{
                remoteDevice = adapter.getRemoteDevice(remoteDeviceAddress);
                socket = remoteDevice.createRfcommSocketToServiceRecord(UUID.fromString(Uuid));
                adapter.cancelDiscovery();
                if(socket != null) {
                    socket.connect();
                }
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("CONNECT", "Connection established");
                bundle.putString("deviceName", remoteDevice.getName());
                bundle.putString("deviceAddress", remoteDevice.getAddress());
                msg.setData(bundle);
                UIHandler.sendMessage(msg);
            }catch(IOException e){
                //e.printStackTrace();
                //toMainActivity("N".getBytes());
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("ERROR", "Could not connect");
                msg.setData(bundle);
                UIHandler.sendMessage(msg);
            }
        }

        if(socket != null){
            isConnected = true;
            try{
                input = socket.getInputStream();
                output = socket.getOutputStream();
                while(running){
                    byte[] buffer = new byte[1024];
                    int bytes;
                    int bytesRead = -1;
                    bytesRead = input.read(buffer);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("CONTROL", buffer);
                    msg.setData(bundle);
                    UIHandler.sendMessage(msg);
                    //toMainActivity(Arrays.copyOfRange(buffer, 0, bytesRead));
                }
            }catch(IOException e){
                //e.printStackTrace();
                //toMainActivity("N".getBytes());
                isConnected = false;
            }
        }
    }

    public void write(byte[] data){
        if(output != null){
            try{
                output.write(data);
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("ERROR ON WRITING", "Could not send command");
            msg.setData(bundle);
            UIHandler.sendMessage(msg);
        }
    }

    private void toMainActivity(byte[] data){
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", data);
        message.setData(bundle);
        MainActivity.handler.sendMessage(message);
    }

    public void cancel(){
        try{
            running = false;
            serverSocket.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        running = false;
    }
}
