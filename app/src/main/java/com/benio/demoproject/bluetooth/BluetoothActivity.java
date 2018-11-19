package com.benio.demoproject.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.benio.demoproject.R;
import com.benio.demoproject.bluetooth.printer.PrinterWriter;
import com.benio.demoproject.bluetooth.printer.PrinterWriter58mm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn
 */
public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0x111;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mBluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }
        Button connectBtn = findViewById(R.id.btn_printer);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    return;
                } else if (mBluetoothService == null) {
                    initBluetoothService();
                }
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        // Add the name and address to an array adapter to show in a ListView
                        Log.d("xxxxxx", device.getName() + "," + device.getAddress());
                        mBluetoothService.connect(device);
                    }
                }
            }
        });
    }

    private void initBluetoothService() {
        mBluetoothService = new BluetoothService();
        mBluetoothService.setOnStateChangedListener(new BluetoothService.OnStateChangedListener() {
            @Override
            public void onStateChanged(int state) {
                if (state == BluetoothService.STATE_CONNECTED) {
                    TestPrintDataMaker printOrderDataMaker = new TestPrintDataMaker(PrinterWriter58mm.TYPE_58, PrinterWriter.HEIGHT_PARTING_DEFAULT);
                    List<byte[]> printData = printOrderDataMaker.getPrintData(PrinterWriter58mm.TYPE_58);
                    for (byte[] bytes : printData) {
                        mBluetoothService.write(bytes);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null) {
            mBluetoothService.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            initBluetoothService();
        }
    }
}
