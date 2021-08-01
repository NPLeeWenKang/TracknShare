package sg.edu.np.tracknshare.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;

public class fragment_TotalRuns extends Fragment {


    public fragment_TotalRuns() {
        // Required empty public constructor
    }

    public static fragment_TotalRuns newInstance() {
        fragment_TotalRuns fragment = new fragment_TotalRuns();
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
        return inflater.inflate(R.layout.fragment_total_runs, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button run_count =  view.findViewById(R.id.run_count);
        run_count.setBackgroundTintList(null);//hide the background tint
        RunDBHandler runDBHandler = new RunDBHandler(getContext());
        AuthHandler authHandler = new AuthHandler(getContext());
        runDBHandler.getCount(authHandler.getCurrentUser().getUid(), run_count); // gets the total amount of runs the current user has and displays it
    }
}