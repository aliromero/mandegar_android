package co.romero.mandegar.model;

import com.orm.SugarRecord;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ProfilePic extends SugarRecord {
    private String id, src;
    private Customer customer;

    public ProfilePic() {
    }


    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
