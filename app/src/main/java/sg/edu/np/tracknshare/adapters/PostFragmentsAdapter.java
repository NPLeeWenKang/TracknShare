package sg.edu.np.tracknshare.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import sg.edu.np.tracknshare.fragments.fragment_follow;
import sg.edu.np.tracknshare.fragments.fragment_general;

public class PostFragmentsAdapter extends FragmentStateAdapter {
    public PostFragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("FRAGMANAGER", "FRAGMANAGER123");
        switch(position)
        {
            case 0:
                return new fragment_general(); // show general posts
            case 1:
                return new fragment_follow(); // show friends posts
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
