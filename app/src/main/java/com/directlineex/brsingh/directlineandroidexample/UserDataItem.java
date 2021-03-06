package com.directlineex.brsingh.directlineandroidexample;

public class UserDataItem {

    private String  password, name, age, gender, email;

    public UserDataItem(String password, String name, String age, String gender, String email) {
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
