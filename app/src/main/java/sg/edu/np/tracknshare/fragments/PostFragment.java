package sg.edu.np.tracknshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Field;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.PostFragmentsAdapter;

public class PostFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public PostFragment() {
        // Required empty public constructor
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
        Log.d("FRAGMANAGER", "FRAGMANAGER");
        super.onViewCreated(view, savedInstanceState);
        PostFragmentsAdapter postFragmentsAdapter = new PostFragmentsAdapter(getChildFragmentManager(),getLifecycle());
        ViewPager2 viewPager2 = view.findViewById(R.id.post_pager);
        viewPager2.setAdapter(postFragmentsAdapter);
        viewPager2.setSaveEnabled(false);

        Field mFling;
        try {
            Field rvField = ViewPager2.class.getDeclaredField("mFling");
            rvField.setAccessible(true);
            rvField.set(viewPager2, 30);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        TabLayout tabLayout = view.findViewById(R.id.feedTabLayout);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) { }

        }).attach();
        tabLayout.getTabAt(0).setText("General");
        tabLayout.getTabAt(1).setText("Friends");

    }


}