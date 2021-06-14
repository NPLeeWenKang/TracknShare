package sg.edu.np.tracknshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.provider.Settings.Global.getString;



public class AuthHandler {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private String key;
    private static final int RC_SIGN_IN = 9001;

    private Context context;

    public AuthHandler(Context c, String s){
        mAuth = FirebaseAuth.getInstance();
        context = c;
        key = s;
    }
    public boolean IsSignedIn(){
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        if (currentUser == null){
            return false;
        }else{
            return true;
        }
    }
    public void SignIn(){
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(key)
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        context.startActivity(signInIntent);

        mAuth.createUserWithEmailAndPassword("poppy123@gmail.com", "memememe");

    }
    public void CreateEmailPasswordAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH" , "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
    }


    public void SignOut(){
        mAuth.signOut();
    }
}
