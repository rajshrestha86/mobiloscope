package com.ioe.mobiloscope;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    public BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void clicked(View v)
    {


        boolean check;
        check=checkBT();
        if(check==true)
        {
            Intent terminal=new Intent(MainActivity.this, Terminal.class);
            startActivity(terminal);
        }

    }

    public void dcmStart(View vW)
    {
       boolean check;
        check=checkBT();
        if(check==true)
        {
            Intent terminal=new Intent(MainActivity.this, DCActivity.class);
            startActivity(terminal);
        }



    }

    public void ohmStart(View vW)
    {
        boolean check;
        check=checkBT();
        if(check==true)
        {
            Intent ohmMeter=new Intent(MainActivity.this, ohmMeter.class);
            startActivity(ohmMeter);
        }



    }

    public void acStart(View vW)
    {
        boolean check;
        check=checkBT();
        if(check==true)
        {
            Intent acM=new Intent(MainActivity.this, ac.class);
            startActivity(acM);
        }



    }


    public boolean checkBT()
    {
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(), "Device does not support Bluetooth.  ", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Checks if Bluetooth has been enabled
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent;
            enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);


        }
        //Toast.makeText(getApplicationContext(), "DC Measurement",Toast.LENGTH_SHORT) .show();
        if(mBluetoothAdapter.isEnabled()){
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Bluetooth not Enabled", Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    public void dcm2Start(View vW)
    {
        //Toast.makeText(getApplicationContext(), "DC Measurement",Toast.LENGTH_SHORT) .show();

        boolean check;
        check=checkBT();
        if(check==true)
        {
            Intent terminal=new Intent(MainActivity.this, DC2Activity.class);
            startActivity(terminal);
        }






    }

    public void startOscilloscope(View v)
    {


        boolean check;
        check=checkBT();
        if(check==true)
        {
            Intent terminal=new Intent(MainActivity.this, oscilloscopeStart.class);
            startActivity(terminal);
        }

    }

    public void dualStart(View v)
    {
        Toast.makeText(getApplicationContext(), "Update Coming Soon", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
