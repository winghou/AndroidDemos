package com.benio.demoproject.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MessengerService extends Service {
    private static final int MSG_SUM = 0x110;
    private Messenger mMessenger;
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Message to = Message.obtain(msg);
            if (msg.what == MSG_SUM) {
                try {
                    // 模拟延时
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求和
                to.obj = msg.arg1 + msg.arg2;
                try {
                    // 发送给客户端
                    msg.replyTo.send(to);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("MessengerService");
        thread.start();
        Looper looper = thread.getLooper();
        Handler handler = new Handler(looper, mCallback);
        mMessenger = new Messenger(handler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
