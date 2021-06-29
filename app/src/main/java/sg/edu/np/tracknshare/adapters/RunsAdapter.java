package sg.edu.np.tracknshare.adapters;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManagerNonConfig;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.tracknshare.BaseActivity;
import sg.edu.np.tracknshare.CreatePostActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.models.Runs;
import sg.edu.np.tracknshare.viewholders.RunsViewHolder;

public class RunsAdapter extends RecyclerView.Adapter<RunsViewHolder> {
    FragmentActivity fragmentActivity; //getActivity() object passed from RunFragment to builder AlertDialog
    Context context;
    ArrayList<Runs> runs;
    ArrayList<String> selectedSharingOptions;
    /*sharing options will be added to this list
   as list cannot be returned in AlertDialog.builder method*/

    String[] sharingOptions = new String[]{"Share to TracknShare app","Share to other apps"};
    //default options in alertDialog
    public RunsAdapter(Context c,ArrayList<Runs> runsArrayList,FragmentActivity fActivity){
        context = c;
        runs = runsArrayList;
        fragmentActivity = fActivity;
    }
    @NonNull
    @Override
    public RunsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_runs,parent,false);
        return new RunsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunsViewHolder holder, int position) {
        Runs r = runs.get(position);
        holder.MapImage.setImageResource(R.drawable.running_splash);
        holder.Run_Date.setText(r.getRunDate());
        holder.Run_distance.setText(""+r.getRunDistance());
        holder.Run_calories.setText(""+r.getRunCalories());
        holder.Run_timing.setText(""+r.getRunDuration());
        holder.Run_pace.setText(String.format("%,.2f",r.getRunPace()));
        holder.Post_Button.setImageDrawable(context.getResources().getDrawable(R.drawable.share));
        holder.Post_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedSharingOptions = new ArrayList<String>();
                openShareModal(sharingOptions);

            }
            private void openShareModal(String[] sharingOptions){
                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
                builder.setTitle("Choose your sharing option");
                builder.setCancelable(true);
                builder.setMultiChoiceItems(sharingOptions, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            selectedSharingOptions.add(sharingOptions[which]);
                        } else {
                            selectedSharingOptions.remove(sharingOptions[which]);

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(selectedSharingOptions.size() > 0){
                            Log.d("HELO","ghd");
                            if(!selectedSharingOptions.isEmpty()){
                                for (String option:selectedSharingOptions) {
                                    if(option.equals(sharingOptions[0])){ // index 0 --> "Share to TracknShare app"
                                        Log.d("HELO","jeff");
/*
                                        Fragment fragment = new CreatePostFragment();
                                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.drawLayout,fragment);
                                        fragmentTransaction.commit();
*/

                                        Intent intent = new Intent(context, CreatePostActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                    else{
                                        //call sharesheet API
                                    }
                                }
                            }

                        }

                    }

                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }

}
