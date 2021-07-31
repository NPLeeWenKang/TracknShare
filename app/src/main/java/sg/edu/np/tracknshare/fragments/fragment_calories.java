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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_calories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_calories extends Fragment {

    private Bargraph Bargraph;


    public fragment_calories() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static fragment_calories newInstance(String param1, String param2) {
        fragment_calories fragment = new fragment_calories();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_calories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        BarChart barChart = getView().findViewById(R.id.calories_bar);

        Bargraph = new Bargraph();

        RunDBHandler runDBHandler = new RunDBHandler(getContext());
        AuthHandler authHandler = new AuthHandler(getContext());
        runDBHandler.getCaloriesForBarGraph(authHandler.getCurrentUser().getUid(), Bargraph, barChart);

    }
}