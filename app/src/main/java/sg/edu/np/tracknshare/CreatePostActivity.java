package sg.edu.np.tracknshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreatePostActivity extends AppCompatActivity {
    Dialog createPostDialog;
    EditText caption;
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPostDialog =  new Dialog(this,R.style.Widget_MaterialComponents_Toolbar);
        createPostDialog.setContentView(R.layout.activity_create_post);
        createPostDialog.show();
/*
        caption = findViewById(R.id.caption_editText);
        post = findViewById(R.id.create_post);
        if(caption.getText().toString().isEmpty()){
            post.setClickable(false); //disable post if there's no caption
        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HELLO","YO");
            }
        });
*/
    }

    public void showClosingAlert(View view) { //onclick callback referenced in xml markup
        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Warning!");
        builder.setMessage("Cancel New Post?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CreatePostActivity.this,BaseActivity.class);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}