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
import sg.edu.np.tracknshare.models.Runs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RunsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RunsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RunsFragment.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Runs");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        ArrayList<Runs> runs = generateRuns();
        RecyclerView rv = view.findViewById(R.id.rv_runs);
        RunsAdapter runsAdapter = new RunsAdapter(getActivity().getApplicationContext(),runs,getActivity());
        LinearLayoutManager lm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(runsAdapter);
    }

    public ArrayList<Runs> generateRuns(){
        ArrayList<Runs> runs = new ArrayList<Runs>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();

        for(int i = 1;i<=20;i++){
            Runs r = new Runs();
            r.setRunId(i);
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