package sg.edu.np.tracknshare.handlers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.edu.np.tracknshare.models.User;

public class UserDBHandler {
    private final String dbUrl = "https://testapp-bc30f-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);

    public void AddUser(User u){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).setValue(u);
    }
//    public User GetUser(String id){
//
//    }
}
