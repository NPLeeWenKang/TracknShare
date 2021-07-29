package sg.edu.np.tracknshare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.models.UserPostViewModel;

public class fragment_follow extends Fragment {
    ArrayList<UserPostViewModel> upList = new ArrayList<>();
    private PostsAdapter adapter;
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
        adapter = new PostsAdapter(view.getContext(),upList);
        LinearLayoutManager lm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        PostDBHandler postDBHandler = new PostDBHandler(getContext());
        AuthHandler authHandler = new AuthHandler(getContext());
        postDBHandler.GetFriendsPost(upList, adapter, authHandler.getCurrentUser().getUid());
    }
}
