package sg.edu.np.tracknshare.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.User;

public class SearchFragment extends Fragment {
    private Context context;
    private ArrayList<User> uList = new ArrayList<>();
    public SearchFragment(Context c) {
        context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDBHandler db = new UserDBHandler(context);

        RecyclerView recyclerView = ((Activity) context).findViewById(R.id.search_rv);
        SearchItemAdapter mAdapter = new SearchItemAdapter(context, uList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        EditText searchBar = view.findViewById(R.id.searchBar);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (!searchBar.getText().toString().equals("")){

                    if (keyEvent.getAction() == KeyEvent.ACTION_UP||i == KeyEvent.KEYCODE_DEL){
                        String name = searchBar.getText().toString();
                        if (i == KeyEvent.KEYCODE_DEL){
                            name = name.substring(0,name.length() - 1)+ "";
                        }
                        if (!name.equals("")) {
                            db.SearchUser(name, uList, mAdapter);
                        } else {
                            uList.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                    } else{
                        uList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    uList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }
    public void SyncReturn(){

    }
}