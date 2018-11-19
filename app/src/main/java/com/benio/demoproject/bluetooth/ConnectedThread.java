package com.benio.demoproject.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 当然，还需要考虑实现细节。首要的是，应该为所有流式传输读取和写入操作使用专门的线程。
 * 这一点很重要，因为 read(byte[]) 和 write(byte[]) 方法都是阻塞调用。
 * read(byte[]) 将会阻塞，直至从流式传输中读取内容。
 * write(byte[]) 通常不会阻塞，但如果远程设备没有足够快地调用 read(byte[])，并且中间缓冲区已满，则其可能会保持阻塞状态以实现流量控制。
 * 因此，线程中的主循环应专门用于读取 InputStream。 可使用线程中单独的公共方法来发起对 OutputStream 的写入操作。
 * https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn#ManagingAConnection
 */
public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private OnReceiveListener mOnReceiveListener;
    private boolean mCanceled;

    public interface OnReceiveListener {
        void onReceive(byte[] buffer, int bytes);

        void onError(Exception e);
    }

    public ConnectedThread(BluetoothSocket socket) {
        super(TAG);
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (!mCanceled && mmSocket.isConnected()) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                if (mOnReceiveListener != null) {
                    mOnReceiveListener.onReceive(buffer, bytes);
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception during read", e);
                if (mOnReceiveListener != null) {
                    mOnReceiveListener.onError(e);
                }
                break;
            }
        }
    }

    /**
     * Call this from the main activity to send data to the remote device
     *
     * @param bytes The bytes to write
     */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

    /**
     * Call this from the main activity to shutdown the connection
     */
    public void cancel() {
        mCanceled = true;
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public void setOnReceiveListener(OnReceiveListener onReceiveListener) {
        mOnReceiveListener = onReceiveListener;
    }
}