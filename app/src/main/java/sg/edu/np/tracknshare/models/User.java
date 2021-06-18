package sg.edu.np.tracknshare.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String Id;
    private String UserName;
    private String Email;
    private java.util.Date AccountCreationDate;
    private ArrayList<String> FollowingId;

    public Date getAccountCreationDate() {
        return AccountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        AccountCreationDate = accountCreationDate;
    }

    public ArrayList<String> getFriendsId() {
        return FollowingId;
    }

    public void setFriendsId(ArrayList<String> friendsId) {
        FollowingId = friendsId;
    }
    public User(){};
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public User(String id, String userName, String email) {
        Id = id;
        UserName = userName;
        Email = email;
    }
}
