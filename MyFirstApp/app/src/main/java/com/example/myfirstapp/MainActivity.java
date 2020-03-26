package com.example.myfirstapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String ACTION_USB_PERMISSION = "com.example.myfirstapp.USB_PERMISSION";
    public static final String ACTION_USB_DEVICE_ATTACHED = "com.example.myfirstapp.ACTION_USB_DEVICE_ATTACHED";

    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbDeviceConnection usbConnection;
    private UsbSerialDevice usbSerialDevice;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            usbConnection = usbManager.openDevice(usbDevice);
            usbSerialDevice = UsbSerialDevice.createUsbSerialDevice(usbDevice, usbConnection);
            if (usbSerialDevice != null) {
                if (usbSerialDevice.open()) { //Set Serial Connection Parameters.
                    usbSerialDevice.setBaudRate(9600);
                    usbSerialDevice.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    usbSerialDevice.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    usbSerialDevice.setParity(UsbSerialInterface.PARITY_NONE);
                    usbSerialDevice.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                    usbSerialDevice.read(mCallback);
                    updateTextView("Serial Connection Opened!\n");
                }
            }
        }

        UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
            //Defining a Callback which triggers whenever data is read.
            @Override
            public void onReceivedData(byte[] arg0) {
                String data = null;
                try {
                    data = new String(arg0, "UTF-8");
                    data.concat("/n");
                    updateTextView(data);
                } catch (UnsupportedEncodingException e) {
                    // Oops
                }
            }
        };
    };
    private boolean connected = false;

    public void updateTextView(String data) {
        TextView dataStream = findViewById(R.id.dataStream);
        String existingData = dataStream.getText().toString();
        existingData.concat("/n");
        existingData.concat(data);
        dataStream.setText(existingData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        IntentFilter filter = new IntentFilter(ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(broadcastReceiver, filter);
    }

    private void connectUsbDevice() {
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        for (Map.Entry<String, UsbDevice>entry: usbDevices.entrySet()) {
            usbDevice = entry.getValue();
            int vendorId = usbDevice.getVendorId();
            if (vendorId != 0) {
                PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(usbDevice, pi);
                Button connectButton = findViewById(R.id.connectButton);
                connectButton.setText(getResources().getString(R.string.button_disconnect));
                connected = true;
            }
        }
    }

    private void disconnectUsbDevice() {
        if (usbSerialDevice != null) {
            usbSerialDevice.close();
        }
        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setText(getResources().getString(R.string.button_connect));
        connected = false;
    }

    /** Called when the user toggles the USB connection on or off */
    public void onToggleConnect(View view) {
        if (!connected) {
            // Perform actual USB connection if there are devices connected
            HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
            if (!usbDevices.isEmpty()) {
                connectUsbDevice();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.dialog_no_connection_title))
                        .setMessage(getResources().getString(R.string.dialog_no_connection_message))
                        .setNeutralButton(getResources().getString(R.string.button_ok), null)
                        .show();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.dialog_disconnect_title))
                    .setMessage(getResources().getString(R.string.dialog_disconnect_message))
                    .setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            disconnectUsbDevice();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.button_no), null)
                    .show();
        }
    }
}
