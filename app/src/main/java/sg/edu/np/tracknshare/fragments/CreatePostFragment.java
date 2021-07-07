package sg.edu.np.tracknshare.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.np.tracknshare.R;

import androidx.annotation.RequiresApi;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

// * A simple {@link Fragment} subclass.
// * Use the {@link CreatePostFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class CreatePostFragment extends DialogFragment {


    private EditText captionText;
    private Toolbar toolbar;
    private ExtendedFloatingActionButton createButton;
    public static CreatePostFragment display(FragmentManager fragmentManager){
        CreatePostFragment createPostFragment = new CreatePostFragment();
        createPostFragment.show(fragmentManager,"DIALOG");
        return createPostFragment;
    }
    public CreatePostFragment () {
        // Required empty public constructor
        //CreatePostFragment createPostFragment = new CreatePostFragment();
    }



@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        captionText = view.findViewById(R.id.caption_text);
        toolbar = view.findViewById(R.id.toolbar);
        createButton = view.findViewById(R.id.floatingActionButton);
        createButton.hide();
        toolbar.setNavigationOnClickListener(v ->{
            boolean result = leaveDialog();
            if(result) {//close full screen dialog only if yes is 'clicked'
                dismiss();
            }
        });
        toolbar.setTitle("Create New Post");

        captionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){ //set toast msg and limit for caption > 255 characters
                    if(s.length() == 255){
                        String limitMessage = "Caption cannot exceed 255 letters.";
                        Toast.makeText(getContext(),limitMessage,Toast.LENGTH_LONG).show();
                    }
                    createButton.show();
                    createButton.setClickable(true);
                    createButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendPost();
                            saveToSharedPref();

                        }
                    });

                }
                else{
                    createButton.hide(); //disable post function if caption is empty
                }
            }

            private void saveToSharedPref() {

                    //save data for Sharesheet options
                    SharedPreferences.Editor editor = getContext().getSharedPreferences
                            ("postCaptions", Context.MODE_PRIVATE).edit();
                    editor.putString("caption",captionText.getText().toString());
                    editor.apply();

                    //get data for Sharesheet options
                    SharedPreferences pref = getContext().getSharedPreferences("postCaption",Context.MODE_PRIVATE);
                    String caption = pref.getString("caption","");


            }

            private void sendPost() {
                //TODO :function to add post to firebase
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean leaveDialog() {
        boolean[] result = new boolean[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cancel new Post");
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                result[0] = true;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                result[0] = false;
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
        return result[0];
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            dialog.getWindow().setLayout(1,1); //Dialog MATCH PARENT for both width and height
        }
    }
}
