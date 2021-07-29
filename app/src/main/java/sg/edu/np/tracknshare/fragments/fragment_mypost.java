package sg.edu.np.tracknshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.ProfilePostsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.models.UserPostViewModel;

public class fragment_mypost extends Fragment {
    private ArrayList<UserPostViewModel> upList = new ArrayList<>();
    public fragment_mypost() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FRAGMENT_MYPFP", "onViewCreated: ");
        RecyclerView recyclerView = view.findViewById(R.id.rv_profile_post);
        ProfilePostsAdapter mAdapter = new ProfilePostsAdapter(view.getContext(), upList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        PostDBHandler postDBHandler = new PostDBHandler(view.getContext());
        AuthHandler auth = new AuthHandler(view.getContext());
        postDBHandler.GetUserPosts(upList, mAdapter, auth.getCurrentUser().getUid());
    }
}