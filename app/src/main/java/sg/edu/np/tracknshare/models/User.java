package sg.edu.np.tracknshare.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class User {
    private String Id;
    private String UserName;
    private String Email;
    private String AccountCreationDate;
    private String Mass;
    private String Height;
    private HashMap<String, Boolean> FriendsId;

    public String getAccountCreationDate() {
        return AccountCreationDate;
    }

    public void setAccountCreationDate(String accountCreationDate) {
        AccountCreationDate = accountCreationDate;
    }

    public HashMap<String, Boolean> getFriendsId() {
        return FriendsId;
    }

    public void setFriendsId(HashMap<String, Boolean> friendsId) {
        FriendsId = friendsId;
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

    public String getMass() {
        return Mass;
    }

    public void setMass(String mass) {
        Mass = mass;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public User(String id, String userName, String email, String accountCreationDate, String mass, String height, HashMap<String, Boolean> friendsId) {
        Id = id;
        UserName = userName;
        Email = email;
        AccountCreationDate = accountCreationDate;
        Mass = mass;
        Height = height;
        FriendsId = friendsId;
    }
}
