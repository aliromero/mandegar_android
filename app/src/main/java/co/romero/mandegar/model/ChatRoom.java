package co.romero.mandegar.model;

import com.orm.SugarRecord;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ChatRoom extends SugarRecord {
    private String name, pic,createdAt, updatedAt,last_message,time_last_message;
    private int chatroomid;

    public ChatRoom() {
    }

   
   



    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }





    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }



    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getTime_last_message() {
        return time_last_message;
    }

    public void setTime_last_message(String time_last_message) {
        this.time_last_message = time_last_message;
    }


    public int getChatroomid() {
        return chatroomid;
    }

    public void setChatroomid(int chatroomid) {
        this.chatroomid = chatroomid;
    }
}
