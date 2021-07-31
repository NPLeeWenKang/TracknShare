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
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.models.Bargraph;

public class fragment_pace extends Fragment {

    private Bargraph Bargraph;

    public fragment_pace() {
        // Required empty public constructor
    }
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
        BarChart barChart = getView().findViewById(R.id.pace_bar);

        Bargraph = new Bargraph();

        RunDBHandler runDBHandler = new RunDBHandler(getContext());
        AuthHandler authHandler = new AuthHandler(getContext());
        runDBHandler.getPacesForBarGraph(authHandler.getCurrentUser().getUid(), Bargraph, barChart);
    }
}