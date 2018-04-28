package co.romero.mandegar.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Respo {





    @SerializedName("data")
    private List<Respo> data;



    private boolean status;
    private int id,customerId;
    private boolean exist_customer;
    private String title,name,src;
    private String[] error;
    public String country,city;
    public Respo()
    {

    }


    public List<Respo> getData() {
        return data;
    }


    public String getTitle() {
        return title;
    }


    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getSrc() {
        return src;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public String[] getError() {
        return error;
    }

    public boolean isExist_customer() {
        return exist_customer;
    }

    public int getCustomerId() {
        return customerId;
    }
}