package com.ioe.mobiloscope;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;


public class Terminal extends Activity {

    public BluetoothAdapter mBluetoothAdapter;
    public ConnectThread mConnectThread;
    public ConnectedThread mConnectedThread;
    public BluetoothDevice mDevice;
    public BluetoothSocket mmSocket;
    public String DeviceName;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;
            String writeMessage = new String(writeBuf);
            switch(msg.what) {
                case 1:

                    writeMessage = writeMessage.substring(begin, end);
                    break;


            }

            EditText terminalEdit=(EditText) findViewById(R.id.terminalEdit);
            terminalEdit.append(writeMessage);




            terminalEdit.append("\n");




        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);



        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices=mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            //There are paired devices. Listing them:
            String deviceName="NULL";
            String deviceHardwareAddress="NULL";
            for(BluetoothDevice device: pairedDevices)
            {
                mDevice=device;
                deviceName=device.getName(); //NAME
                deviceHardwareAddress=device.getAddress(); //MAC
            }
             DeviceName=deviceName + "\n " + deviceHardwareAddress;
            Toast.makeText(getApplicationContext(), "Connected to: " + DeviceName, Toast.LENGTH_SHORT).show();

           /* TextView disp= (TextView) findViewById(R.id.dispMsg);
            disp.setText(deviceName + " " + deviceHardwareAddress);*/



        }
    }

//Start Terminal Write
    public void StartTerminal(View v)
    {
        //Toast.makeText(getApplicationContext(), DeviceName, Toast.LENGTH_SHORT).show();
        try
        {
            mConnectThread = new ConnectThread(mDevice);
            mConnectThread.start();
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Can't Start Measurement",Toast.LENGTH_SHORT).show();
        }

    }

    //Stop Terminal Write
    public void StopTerminal(View v)
    {


        try {
            mConnectThread.cancel();
        } catch (Exception e) {

        }
        Toast.makeText(getApplicationContext(), "Terminal Stopped", Toast.LENGTH_SHORT).show();

    }

    //Clear Button
    public void clear_value(View v)
    {
        EditText value=(EditText) findViewById(R.id.terminalEdit);

        value.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terminal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ///Bluetooth Connection
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        String s = "00001101-0000-1000-8000-00805F9B34FB";
        String s2 = s.replace("-", "");

        private final UUID MY_UUID = new UUID(new BigInteger(s2.substring(0, 16), 16).longValue(), new BigInteger(s2.substring(16), 16).longValue());

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
            ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }

    }


    //Data Transfer
    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    bytes += mmInStream.read(buffer,bytes, buffer.length-bytes);
                    for(int i=begin; i<bytes; i++ )
                    {
                        if(buffer[i]=="#".getBytes()[0])
                        {
                            Message msg = mHandler.obtainMessage(1, begin, i, buffer);
                            mHandler.sendMessage(msg);
                            begin=i+1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }




                } catch (IOException e) {
                    break;
                }






            }
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }
        public void cancel() {
            try {
                EmmSocket.close();
            } catch (IOException e) { }
        }
    }


}








