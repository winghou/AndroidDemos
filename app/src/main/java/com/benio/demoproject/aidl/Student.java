package com.benio.demoproject.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AS环境下，AIDL自定义类型时，
 * 不要将Student放在与AIDL文件同样的目录，应改放在java文件夹下与包名一致的目录
 * https://blog.csdn.net/baidu_30164869/article/details/51036405
 * Created by zhangzhibin on 2018/8/7.
 */
public class Student implements Parcelable {
    public static final int SEX_MALE = 1;
    public static final int SEX_FEMALE = 2;

    public int sno;
    public String name;
    public int sex;
    public int age;

    public Student() {
    }

    public static final Parcelable.Creator<Student> CREATOR = new
            Parcelable.Creator<Student>() {

                public Student createFromParcel(Parcel in) {
                    return new Student(in);
                }

                public Student[] newArray(int size) {
                    return new Student[size];
                }

            };

    private Student(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sno);
        dest.writeString(name);
        dest.writeInt(sex);
        dest.writeInt(age);
    }

    public void readFromParcel(Parcel in) {
        sno = in.readInt();
        name = in.readString();
        sex = in.readInt();
        age = in.readInt();
    }

    @Override
    public String toString() {
        return "Student{" +
                "sno=" + sno +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
