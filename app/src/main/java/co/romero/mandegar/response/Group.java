package co.romero.mandegar.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Group implements Serializable {
    public String id, name, pic;
    @SerializedName("last_message")
    public Message message;

    public Group() {
    }


}
