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
    public boolean IsSignedIn(){
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        if (currentUser == null){
            return false;
        }else{
            return true;
        }
    }
    public FirebaseUser GetCurrentUser(){
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        return currentUser;
    }
    public void CreateEmailPasswordAccount(String email, String password, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH" , "createUserWithEmail:success");
                            FirebaseUser currentUser =  mAuth.getCurrentUser();
                            UserDBHandler db = new UserDBHandler(context);
                            User u =new User(currentUser.getUid(),username,email,new SimpleDateFormat("dd MMM yyyy").format(Calendar.getInstance().getTime()),null);
                            db.AddUser(u);

                            Intent intent = new Intent(context, BaseActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        } else {
//                            try{
//                                throw task.getException();
//                            } catch (FirebaseAuthInvalidCredentialsException){
//FirebaseAuthInvalidUserException FirebaseNetworkException

//                            }
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
    }
    public void SignInWithEmailPassword(String email, String password, TextView errorText){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "SUCCESS");
                            Intent intent = new Intent(context, BaseActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        } else {
                            Log.d("AUTH", "FAIL"+task.getException());
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
    public void UpdatePassword(String newPassword){
        FirebaseUser currentUser =  mAuth.getCurrentUser();
        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("UPDATEPASSWORD", "User password updated.");
                } else {
                    Log.d("UPDATEPASSWORD", "FAIL "+task.getException());
                }
            }
        });
    }
    public void SignOut(){
        mAuth.signOut();
    }
}
