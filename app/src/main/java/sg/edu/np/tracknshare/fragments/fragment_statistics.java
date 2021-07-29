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
import sg.edu.np.tracknshare.models.Statistics;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_statistics extends Fragment {
    ViewPager2 viewPager2;
    TabLayout dotsIndicator;
    RecyclerView stats_rv;

    ArrayList<String> titles= new ArrayList<String>();

    public fragment_statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_statistics.
     */
    // TODO: Rename and change types and number of parameters
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
        viewPager2.setSaveEnabled(true);
        viewPager2.setAdapter(statisticsFragmentAdapter);

        new TabLayoutMediator(dotsIndicator,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override   //connect dot indicator tab layout to viewPager2
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) { }
        }).attach();

        stats_rv=view.findViewById(R.id.stats_rv);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.VERTICAL);
        stats_rv.setLayoutManager(lm); //configure recycler view settings
        titles.add("steps");
        titles.add("calories");
        titles.add("distance(km)");

        ArrayList<Statistics> statisticsList = generateStats();
        StatsAdapter adapter = new StatsAdapter(getContext(),statisticsList);
        stats_rv.setAdapter(adapter);
    }

    private ArrayList<Statistics> generateStats() {// retrieve stats from db and populate into this list
        ArrayList<Statistics> stats = new ArrayList<Statistics>();
        //1: stat1:Daily avg steps stat2:Total Steps so far
        //2:stat1: Daily avg calories stat2: Total Calories burned
        //3:stat1: Daily avg distance(km) stat2: Total distance so far(km)

        for(int i = 0;i<3;i++){
            Statistics stat = new Statistics();
            stat.setTitle(titles.get(i));
            int stat1 = i*5;//daily avg stat
            int stat2 = i*5*2; //total stat
            stat.setStat1number(stat1);
            stat.setStat2number(stat2);
            stats.add(stat);

        }
        return stats;
    }
}