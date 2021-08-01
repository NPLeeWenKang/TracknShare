package sg.edu.np.tracknshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.StatisticsFragmentAdapter;
import sg.edu.np.tracknshare.adapters.StatsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.models.Statistics;

public class fragment_statistics extends Fragment {
    ViewPager2 viewPager2;
    TabLayout dotsIndicator;
    RecyclerView stats_rv;

    ArrayList<String> titles= new ArrayList<String>();

    public fragment_statistics() {
        // Required empty public constructor
    }

    public static fragment_statistics newInstance(String param1, String param2) {
        fragment_statistics fragment = new fragment_statistics();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatisticsFragmentAdapter statisticsFragmentAdapter = new StatisticsFragmentAdapter(getChildFragmentManager(),getLifecycle());

        viewPager2 =view.findViewById(R.id.stats_viewPager);
        dotsIndicator = view.findViewById(R.id.tabDots);
        viewPager2.setSaveEnabled(false);
        viewPager2.setAdapter(statisticsFragmentAdapter);

        new TabLayoutMediator(dotsIndicator,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override   //connect dot indicator tab layout to viewPager2
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) { }
        }).attach();

        stats_rv=view.findViewById(R.id.stats_rv);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        stats_rv.setLayoutManager(lm); //configure recycler view settings

        ArrayList<Statistics> statisticsList = new ArrayList<>();
        StatsAdapter adapter = new StatsAdapter(getContext(),statisticsList);
        stats_rv.setAdapter(adapter);

        RunDBHandler runDBHandler = new RunDBHandler(getContext());
        AuthHandler authHandler = new AuthHandler(getContext());
        runDBHandler.getMyRunStatistics(authHandler.getCurrentUser().getUid(), statisticsList, adapter); // gets data from database and populates bar graph
    }

}