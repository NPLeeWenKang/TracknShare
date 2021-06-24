package sg.edu.np.tracknshare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.tracknshare.BaseActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.PostFragmentsAdapter;
import sg.edu.np.tracknshare.adapters.ViewPagerAdapter;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.viewholders.PostViewHolder;

public class PostFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PostFragmentsAdapter postFragmentsAdapter;
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_post, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm =getActivity().getSupportFragmentManager();
        postFragmentsAdapter = new PostFragmentsAdapter(fm,getLifecycle());
        viewPager2 = view.findViewById(R.id.post_pager);
        viewPager2.setAdapter(postFragmentsAdapter);
        tabLayout = view.findViewById(R.id.feedTabLayout);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
/*
                switch (position){
                    case 0:
                        tab.setText("general");
                    case 1:
                        tab.setText("friends");
                }
*/
            }
        }).attach();

    }


}