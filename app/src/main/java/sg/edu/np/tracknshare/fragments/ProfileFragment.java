package sg.edu.np.tracknshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Field;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.ProfileFragmentAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDBHandler db = new UserDBHandler(view.getContext());
        AuthHandler auth = new AuthHandler(view.getContext());
        db.getMyDetails(auth.getCurrentUser().getUid(), view.getContext());

        StorageHandler storageHandler = new StorageHandler();
        ImageView imageView = view.findViewById(R.id.avatarIMG);
        storageHandler.loadProfileImageToApp(auth.getCurrentUser().getUid(), view.getContext(), imageView);

        ProfileFragmentAdapter profileFragmentAdapter = new ProfileFragmentAdapter(getChildFragmentManager(),getLifecycle());
        ViewPager2 viewPager2 = view.findViewById(R.id.profilePager);
        viewPager2.setAdapter(profileFragmentAdapter);
        viewPager2.setSaveEnabled(false);
        TabLayout tabLayout = view.findViewById(R.id.profiletabLayout);

        Field mFling;
        try {
            Field rvField = ViewPager2.class.getDeclaredField("mFling");
            rvField.setAccessible(true);
            rvField.set(viewPager2, 30);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> { }).attach();
        tabLayout.getTabAt(0).setText("My Posts");
        tabLayout.getTabAt(1).setText("My Statistics");

    }
}