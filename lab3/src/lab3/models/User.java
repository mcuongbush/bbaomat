package lab3.models;

import java.io.Serializable;
import java.math.BigInteger;

public class User implements Serializable {
    String name;
    String address;
    String phone;
    String password;


    public User(String name, String address, String phone, String password) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
