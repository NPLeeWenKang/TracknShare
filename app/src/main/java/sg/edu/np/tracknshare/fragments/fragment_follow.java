package sg.edu.np.tracknshare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.UserPostViewModel;

public class fragment_follow extends Fragment {
    ArrayList<UserPostViewModel> upList = new ArrayList<>();

    public fragment_follow() {
        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rvPost);
        PostsAdapter adapter = new PostsAdapter(view.getContext(),upList);
        LinearLayoutManager lm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        PostDBHandler postDBHandler = new PostDBHandler(view.getContext());
        AuthHandler authHandler = new AuthHandler(view.getContext());
        postDBHandler.GetFriendsPost(upList, adapter, authHandler.GetCurrentUser().getUid());

    }

    public void SharePost(String caption, Uri imageUri){
        /*this function calls the sharesheet API to share the posts to
        other social messaging apps*/
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
        sendIntent.setType("image/jpeg");

        Intent sharePost = Intent.createChooser(sendIntent,"hello");
        startActivity(sharePost);
    }

}
