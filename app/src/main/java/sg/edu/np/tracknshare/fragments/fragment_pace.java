package sg.edu.np.tracknshare.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.models.Bargraph;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_pace#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_pace extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Bargraph paceGraph;
    private BarChart paceChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_pace() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_pace.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_pace newInstance(String param1, String param2) {
        fragment_pace fragment = new fragment_pace();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_pace, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paceGraph = new Bargraph(); //instantiate object
        paceChart = getView().findViewById(R.id.pace_chart);
        // retrieve pace of runs from database here and attach to list
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(3,5));
        data.add(new BarEntry(4,16));
        data.add(new BarEntry(5,3));
        data.add(new BarEntry(6,25));
        //------------------------------------------------//
        int BarColor = Color.GREEN;
        paceGraph.setChart(data, paceChart,BarColor);
    }
}