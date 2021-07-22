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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_totalRuns#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_totalRuns extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_totalRuns() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_totalRuns.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_totalRuns newInstance(String param1, String param2) {
        fragment_totalRuns fragment = new fragment_totalRuns();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total_runs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChart barChart = getActivity().findViewById(R.id.steps_bar);

        //generating bar data
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(1,200));
        data.add(new BarEntry(2,500));
        data.add(new BarEntry(3,350));
        data.add(new BarEntry(4,700));
        data.add(new BarEntry(5,150));
        String graphLabel = "Steps for 5 previous runs";

        BarDataSet barDataSet  = new BarDataSet(data,graphLabel);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);


        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setText(graphLabel);
        barChart.animateY(2000);
    }
}