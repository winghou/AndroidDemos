// IMyAidlInterface.aidl
package com.benio.demoproject.aidl;
import com.benio.demoproject.aidl.Student;
// Declare any non-default types here with import statements

interface IMyAidlInterface {

    List<Student> getStudent();

    void addStudent(in Student stu);
}
