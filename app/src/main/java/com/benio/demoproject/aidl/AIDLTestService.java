package com.benio.demoproject.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLTestService extends Service {

    private final List<Student> mStudents = new ArrayList<>();

    private IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public List<Student> getStudent() throws RemoteException {
            synchronized (mStudents) {
                return mStudents;
            }
        }

        @Override
        public void addStudent(Student stu) throws RemoteException {
            synchronized (mStudents) {
                if (!mStudents.contains(stu)) {
                    mStudents.add(stu);
                }
            }
        }
    };

    public AIDLTestService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thr = new Thread(null, new ServiceWorker(), "BackgroundService");
        thr.start();

        synchronized (mStudents) {
            for (int i = 1; i < 6; i++) {
                Student student = new Student();
                student.name = "student#" + i;
                student.age = i * 5;
                mStudents.add(student);
            }

        }
    }

    @Override
    public void onDestroy() {
        mDestroyed = true;
        super.onDestroy();
    }

    private boolean mDestroyed;

    class ServiceWorker implements Runnable {
        long counter = 0;

        @Override
        public void run() {
            // do background processing here.....
            while (!mDestroyed) {
                Log.d("aidl", "" + counter);
                counter++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
