package co.romero.mandegar.model;

import com.orm.SugarRecord;

import java.util.List;

import co.romero.mandegar.response.Profile;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Customer extends SugarRecord {
    private String id, name,description,createdAt, updatedAt;
    private  int chatroom_id;
    private List<Profile> profiles;

    public Customer() {
    }

   
   


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }



    public int getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(int chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }
}
