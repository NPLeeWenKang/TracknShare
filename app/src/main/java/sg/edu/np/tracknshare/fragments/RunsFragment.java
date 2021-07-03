package sg.edu.np.tracknshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.RunsAdapter;
import sg.edu.np.tracknshare.models.Run;

public class RunsFragment extends Fragment {

    private String mParam2;

    public RunsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_runs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Run> runs = generateRuns();
        RecyclerView rv = view.findViewById(R.id.rv_runs);
        RunsAdapter runsAdapter = new RunsAdapter(getActivity().getApplicationContext(),runs,getActivity());
        LinearLayoutManager lm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(runsAdapter);
    }

    public ArrayList<Run> generateRuns(){
        ArrayList<Run> runs = new ArrayList<Run>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();

        for(int i = 1;i<=20;i++){
            Run r = new Run();
            r.setRunId(""+i);
            r.setRunDate(sdf.format(c.getTime()));
            r.setRunDistance(i*5.00);
            r.setRunCalories(234);
            r.setRunDuration(1500000);
            r.setRunPace();
            runs.add(r);
        }
        return runs;
    }
}