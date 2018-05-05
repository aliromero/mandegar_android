package co.romero.mandegar.response;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Message implements Serializable {
    public String id, text, created_at,updated_at,deleted_at;
    Customer user;

    public Message() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }
}
