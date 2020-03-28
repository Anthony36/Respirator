package com.example.myapplication2

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.felhr.usbserial.UsbSerialInterface.UsbReadCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var m_usbManager:UsbManager
    var m_device: UsbDevice? = null
    var m_serial: UsbSerialDevice? = null
    var m_connection: UsbDeviceConnection? = null

    val ACTION_USB_PERMISSION = "permission"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val filter = IntentFilter()
        filter.addAction(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        registerReceiver(broadcastReceiver, filter)

/*        val mCallback = UsbReadCallback { data: ByteArray? ->
            val dataStr = String(data!!, charset("UTF-8"))
            Log.i("serial", "Data received: $dataStr")
        }
        */
 //        on.setOnClickListener{sendData("o")}
//        off.setOnClickListener{sendData("x")}
        //sendData("0x38")


        //serialPort.syncWrite(0x38,0)
       // m_serial?.syncWrite("0x38".toByteArray(), 60)
        on.setOnClickListener{sendData("o")}
        off.setOnClickListener{sendData("x")}
        disconnect.setOnClickListener{disconnect()}
        connect.setOnClickListener{startUsbConnecting()}
    }

    private fun startUsbConnecting(){
        val usbDevices:HashMap<String,UsbDevice>? = m_usbManager.deviceList
        if (!usbDevices?.isEmpty()!!){
            var keep = true
            usbDevices.forEach { entry ->
                m_device = entry.value
                val deviceVendorId: Int? = m_device?.vendorId
                Log.i("serial", "vendorId: " + deviceVendorId)
                if (deviceVendorId == 9025) { //needs to match arduino vendor id
                    val intent: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
                    m_usbManager.requestPermission(m_device, intent)
                    keep = false
                    Log.i("serial", "connection successful")
                } else {
                    m_connection = null
                    m_device = null
                    Log.i("serial", "unable to connect to device, unknown device id: " + deviceVendorId)
                }
                if (!keep) {
                    return
                }
            }
        }else{
            Log.i("serial", "no usb device connected")
        }
    }

    private fun sendData(input: String){
        m_serial?.write(input.toByteArray())
        Log.i("serial","sending data: " + input.toByteArray())
    }

    private fun disconnect(){
        m_serial?.close()
    }

    private val broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action!! == ACTION_USB_PERMISSION){
                val granted: Boolean = intent.extras!!.getBoolean((UsbManager.EXTRA_PERMISSION_GRANTED))
                if (granted){
                    m_connection = m_usbManager.openDevice((m_device));
                    m_serial = UsbSerialDevice.createUsbSerialDevice(m_device, m_connection)
                    if (m_serial != null){
                        if(m_serial!!.open()){
                            m_serial!!.setBaudRate(9600)
                            m_serial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                            m_serial!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                            m_serial!!.setParity(UsbSerialInterface.PARITY_NONE)
                            m_serial!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)

                        }
                        else {
                            Log.i("Serial", "port not open")
                        }
                    }
                    else {
                        Log.i("Serial", "port is null")
                    }
                }else{
                    Log.i("Serial", "permission not granted")
                }
            }else if(intent.action == UsbManager.ACTION_USB_ACCESSORY_ATTACHED){
                startUsbConnecting()
            }else if(intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED){
                disconnect()
            }
        }
    }
}
