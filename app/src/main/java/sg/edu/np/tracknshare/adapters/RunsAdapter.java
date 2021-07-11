package sg.edu.np.tracknshare.adapters;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManagerNonConfig;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.np.tracknshare.BaseActivity;
import sg.edu.np.tracknshare.CreatePostActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.fragments.CreatePostFragment;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.viewholders.RunsViewHolder;

public class RunsAdapter extends RecyclerView.Adapter<RunsViewHolder>  {
    FragmentActivity fragmentActivity; //getActivity() object passed from RunFragment to builder AlertDialog
    Context context;
    ArrayList<Run> runs;
    ArrayList<String> selectedSharingOptions;
    /*sharing options will be added to this list
   as list cannot be returned in AlertDialog.builder method*/

    String[] sharingOptions = new String[]{"Share to TracknShare app","Share to other apps"};
    //default options in alertDialog
    public RunsAdapter(Context c,ArrayList<Run> runsArrayList,FragmentActivity fActivity){
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
        Run r = runs.get(position);
        StorageHandler storageHandler = new StorageHandler();
        storageHandler.LoadFileToApp(r.getImageId(), context, holder.MapImage);
        holder.Run_Date.setText(r.getRunDate());
        holder.Run_distance.setText(String.format("%.4f", r.getRunDistance()));
        holder.Run_calories.setText(""+r.getRunCalories());
        holder.Run_timing.setText(""+r.getRunDuration());
        holder.Run_pace.setText(""+r.getRunPace());
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
                                        Fragment fragment = new CreatePostFragment();
                                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.drawLayout,fragment);
                                        fragmentTransaction.commit();
                                    }
                                    else{
                                        BitmapDrawable bitmapDrawable = ((BitmapDrawable) holder.MapImage.getDrawable());
                                        Bitmap bitmap = bitmapDrawable.getBitmap();

                                        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build()); //set external app-sharing permissions
                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM,getBitmapFromView(bitmap));
                                        shareIntent.setType("image/*");
                                        context.startActivity(Intent.createChooser(shareIntent,"Send"));

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
    private void shareRunImage(String uri){
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build()); //set external app-sharing permissions
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent,"Send"));
//        Picasso.get().load(uri).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM,getBitmapFromView(bitmap));
//                shareIntent.setType("image/*");
//                context.startActivity(Intent.createChooser(shareIntent,"Send"));
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) { }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) { }
//        });

    }
//    public static URI encodeURL(String url) throws MalformedURLException, URISyntaxException
//    {
//        URI uriFormatted = null;
//
//        URL urlLink = new URL(url);
//        uriFormatted = new URI("http", urlLink.getHost(), urlLink.getPath(), urlLink.getQuery(), urlLink.getRef());
//
//        return uriFormatted;
//    }
    private Uri getBitmapFromView(Bitmap bmp){
        Uri bitmapUri = null;
        try{
            File file = new File(context.getExternalCacheDir(),String.valueOf(System.currentTimeMillis())+".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG,90,fileOutputStream);
            fileOutputStream.close();
            bitmapUri = Uri.fromFile(file);

        }
        catch(IOException e){
            e.printStackTrace();
        }
        Log.d("ERROR",bitmapUri.toString());
        return bitmapUri;
    }
    @Override
    public int getItemCount() {
        return runs.size();
    }

}
