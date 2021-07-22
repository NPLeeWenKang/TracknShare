package sg.edu.np.tracknshare.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import sg.edu.np.tracknshare.fragments.fragment_calories;
import sg.edu.np.tracknshare.fragments.fragment_pace;
import sg.edu.np.tracknshare.fragments.fragment_TotalRuns;
import sg.edu.np.tracknshare.fragments.fragment_steps;

public class StatisticsFragmentAdapter extends FragmentStateAdapter {
    public StatisticsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0://fragment for run steps
                return new fragment_TotalRuns();
            case 1://fragment for total runs
                return new fragment_steps();
            case 2://fragment for calories data
                return new fragment_calories();

            case 3://fragment for run pace trend
                return new fragment_pace();

            default:
                return new fragment_TotalRuns();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
