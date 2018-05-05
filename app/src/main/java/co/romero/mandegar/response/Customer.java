package co.romero.mandegar.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Customer implements Serializable {
    private String id, name, mobile, email;

    @SerializedName("profiles")
    public List<Profile> profiles;


    public Customer() {
    }

    public Customer(String id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
