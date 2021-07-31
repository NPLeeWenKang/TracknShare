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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        db.GetMyDetails(auth.getCurrentUser().getUid(), view.getContext());

        StorageHandler storageHandler = new StorageHandler();
        ImageView imageView = view.findViewById(R.id.avatarIMG);
        storageHandler.LoadProfileImageToApp(auth.getCurrentUser().getUid(), view.getContext(), imageView);

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

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) { }

        }).attach();
        tabLayout.getTabAt(0).setText("My Posts");
        tabLayout.getTabAt(1).setText("My Statistics");

    }
}