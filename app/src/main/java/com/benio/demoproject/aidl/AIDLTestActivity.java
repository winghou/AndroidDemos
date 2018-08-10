package com.benio.demoproject.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.benio.demoproject.R;

import java.util.List;

public class AIDLTestActivity extends AppCompatActivity {
    private IMyAidlInterface mInterface;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mInterface = null;
        }
    };
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidltest);
        mTextView = findViewById(R.id.tv_content);

        Intent intent = new Intent(this, AIDLTestService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterface == null) {
                    return;
                }
                Student student = new Student();
                student.age = 1;
                student.name = "stu haha " + student.hashCode();
                try {
                    mInterface.addStudent(student);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterface == null) {
                    return;
                }
                try {
                    List<Student> students = mInterface.getStudent();
                    mTextView.setText(students.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
