package sg.edu.np.tracknshare.fragments;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.np.tracknshare.R;

import androidx.annotation.RequiresApi;

// * A simple {@link Fragment} subclass.
// * Use the {@link CreatePostFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class CreatePostFragment extends DialogFragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private Toolbar toolbar;
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
        toolbar = view.findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.menu_post);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("New Post");
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });
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
