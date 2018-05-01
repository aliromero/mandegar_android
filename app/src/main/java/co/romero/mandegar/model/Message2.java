package co.romero.mandegar.model;
import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Message2 implements Serializable {
    String id, message, createdAt;
    Customer user;

    public Message2() {
    }

    public Message2(String id, String message, String createdAt, Customer user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }
}
