package sg.edu.np.tracknshare.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import sg.edu.np.tracknshare.fragments.fragment_mypost;
import sg.edu.np.tracknshare.fragments.fragment_statistics;

public class ProfileFragmentAdapter extends FragmentStateAdapter {
    public ProfileFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new fragment_mypost(); // go to my posts
            case 1:
                return new fragment_statistics(); // go to my statistics
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
