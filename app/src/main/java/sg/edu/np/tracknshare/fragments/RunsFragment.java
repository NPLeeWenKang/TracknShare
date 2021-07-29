package sg.edu.np.tracknshare.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.RunsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.models.Run;

public class RunsFragment extends Fragment {
    private ArrayList<Run> rList = new ArrayList<>();

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
    public void onPause() {
        super.onPause();
        //
        Log.d("state","PAUSED");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv_runs);
        RunsAdapter runsAdapter = new RunsAdapter(view.getContext(),rList,getActivity());
        LinearLayoutManager lm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(runsAdapter);

        RunDBHandler runDB = new RunDBHandler(view.getContext());
        AuthHandler auth = new AuthHandler(view.getContext());
        runDB.GetRuns(auth.getCurrentUser().getUid(), rList, runsAdapter, view.getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("state","RESUMED");
        SharedPreferences sharedPreferences = getContext().
                getSharedPreferences("PostStatus",Context.MODE_PRIVATE);
        boolean showSnackbar = sharedPreferences.getBoolean("Success",false);
        if(showSnackbar){// if post successfully created
            View v = getActivity().findViewById(R.id.coordinatorlayout);
            Snackbar snackbar = Snackbar.make(v,"Successfully posted your run.",Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab);
                    snackbar.show();//one-time event

            sharedPreferences.edit().clear().commit();//to avoid snackbar from appearing repetitively

        }

    }
    public void onDestroy(){
        super.onDestroy();
    }
}