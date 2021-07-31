package sg.edu.np.tracknshare.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import sg.edu.np.tracknshare.BaseActivity;
import sg.edu.np.tracknshare.FriendListActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.SettingsActivity;
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

        EditText searchBar = ((Activity) context).findViewById(R.id.searchBarAppBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (!str.equals("")){
                    Log.d("ONKEY", "afterTextChanged: "+str);
                    db.SearchUser(str, uList, mAdapter);
                }else{
                    uList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        ConstraintLayout friendConst = view.findViewById(R.id.friendConstraint);
        friendConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FriendListActivity.class);
                ((Activity) view.getContext()).startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });

    }
    public void SyncReturn(){

    }
}