package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.edu.np.tracknshare.BaseActivity;
import sg.edu.np.tracknshare.models.User;

import static android.provider.Settings.Global.getString;



public class AuthHandler {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private Context context;

    public AuthHandler(Context c){
        mAuth = FirebaseAuth.getInstance();
        context = c;
    }
    public boolean isSignedIn(){
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        if (currentUser == null){
            return false;
        }else{
            return true;
        }
    }
    public FirebaseUser getCurrentUser(){
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        return currentUser;
    }
    public void createEmailPasswordAccount(String password, User u){
        // Creates an account with email and password
        mAuth.createUserWithEmailAndPassword(u.getEmail(), password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser =  mAuth.getCurrentUser();
                            UserDBHandler db = new UserDBHandler(context);
                            u.setId(currentUser.getUid());
                            u.setAccountCreationDate(new SimpleDateFormat("dd MMM yyyy").format(Calendar.getInstance().getTime()));
                            db.AddUser(u); // Stores data into database

                            Intent intent = new Intent(context, BaseActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        } else {
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
    }
    public void signInWithEmailPassword(String email, String password, TextView errorText){
        // Sign in using Email password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Success
                            Intent intent = new Intent(context, BaseActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent); // Change to main page
                            ((Activity) context).finish();
                        } else {
                            // Fail
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                errorText.setText("Error - Invalid password or email");
                            }catch (FirebaseAuthInvalidUserException e){
                                errorText.setText("Error - email not registered in System");
                            }
                            catch (FirebaseNetworkException e){
                                errorText.setText("Error - network error)");
                            } catch (Exception e){
                                errorText.setText("Unknown Error Occured");
                            }

                        }
                    }
                });
    }
    public void sendResetEmail(){
        // Sends reset email
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        String emailAddress = currentUser.getEmail() ;
        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) { }
                });
    }
    public void signOut(){
        mAuth.signOut();
    }
}
