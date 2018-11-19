package com.benio.demoproject.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * This thread runs while attempting to make an outgoing connection
 * with a device. It runs straight through; the connection either
 * succeeds or fails.
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#ConnectingAsAClient
 */
public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
    private OnConnectedListener mOnConnectedListener;

    public interface OnConnectedListener {
        void onConnected(BluetoothDevice device, BluetoothSocket socket);

        void onError(Exception e);
    }

    public ConnectThread(BluetoothAdapter mBluetoothAdapter, BluetoothDevice device, UUID uuid) {
        super(TAG);
        this.mBluetoothAdapter = mBluetoothAdapter;
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "create BluetoothSocket failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            Log.e(TAG, "close() of connect socket failed", connectException);
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "close() of connect socket failed", closeException);
            }
            if (mOnConnectedListener != null) {
                mOnConnectedListener.onError(connectException);
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        if (mOnConnectedListener != null) {
            mOnConnectedListener.onConnected(mmDevice, mmSocket);
        }
    }

    /**
     * Will stop an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public void setOnConnectedListener(OnConnectedListener onConnectedListener) {
        mOnConnectedListener = onConnectedListener;
    }
}