package com.benio.demoproject.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.benio.demoproject.R;

import java.util.Random;

public class MessengerClientActivity extends AppCompatActivity {
    private static final int MSG_SUM = 0x110;
    private TextView mStateView;
    private TextView mMessageView;
    private Button mAddButton;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_SUM) {
                mMessageView.append(msg.obj.toString());
                mMessageView.append("\n");
                mAddButton.setEnabled(true);
            }
            return true;
        }
    });
    private Messenger mLocalMessenger = new Messenger(mHandler);
    private Messenger mRemoteMessenger;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteMessenger = new Messenger(service);
            mStateView.setText("connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteMessenger = null;
            mStateView.setText("disConnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_client);
        mStateView = findViewById(R.id.tv_state);
        mMessageView = findViewById(R.id.tv_message);
        mAddButton = findViewById(R.id.btn_add);
        mStateView.setText("disConnected");
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Random random = new Random();
                int a = random.nextInt(10);
                int b = random.nextInt(20);
                mMessageView.append(a + " + " + b + " = ");

                Message msgFromClient = Message.obtain();
                msgFromClient.what = MSG_SUM;
                msgFromClient.arg1 = a;
                msgFromClient.arg2 = b;
                msgFromClient.replyTo = mLocalMessenger;
                if (mRemoteMessenger != null) {
                    try {
                        mRemoteMessenger.send(msgFromClient);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MessengerService.class);
                bindService(intent, mConnection, BIND_AUTO_CREATE);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRemoteMessenger != null) {
            unbindService(mConnection);
        }
    }
}
