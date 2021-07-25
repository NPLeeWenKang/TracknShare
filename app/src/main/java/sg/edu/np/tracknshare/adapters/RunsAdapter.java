package sg.edu.np.tracknshare.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import sg.edu.np.tracknshare.CreatePostActivity;
import sg.edu.np.tracknshare.CreateRunActivity;
import sg.edu.np.tracknshare.FullMapActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.fragments.CreatePostFragment;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.viewholders.RunsViewHolder;

public class RunsAdapter extends RecyclerView.Adapter<RunsViewHolder>  {
    FragmentActivity fragmentActivity; //getActivity() object passed from RunFragment to builder AlertDialog
    Context context;
    ArrayList<Run> runs;

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
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:ma");
        storageHandler.LoadFileToApp(r.getImageId(), context, holder.MapImage);

        holder.Run_Date.setText(dateFormat.format(r.getRunDate()));
        holder.Run_distance.setText(String.format("%.4f", r.getRunDistance()));
        holder.Run_calories.setText(""+r.getRunCalories());
        holder.Run_timing.setText(""+r.getRunDuration());
        holder.Run_pace.setText("" + String.format("%.2f", r.getRunPace()) + " m/s");
        Log.d("PACE", "onBindViewHolder: "+r.getRunPace());
        holder.Share_button.setImageDrawable(context.getResources().getDrawable(R.drawable.share));
        holder.post_button.setImageDrawable(context.getResources().getDrawable(R.drawable.writing));
        saveImage(holder.MapImage.getDrawable());

        holder.MapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullMapActivity.class);
                intent.putExtra("mapType", "movable");
                intent.putExtra("id", r.getRunId());
                ((Activity) context).startActivity(intent);
            }
        });

        holder.post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "Share internally to TracknShare app"
                Intent intent = new Intent(v.getContext(), CreatePostActivity.class);
                intent.putExtra("runId", r.getImageId());
                context.startActivity(intent);
            }
        });
        holder.Share_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openShareSheet();

            }
            private void openShareSheet() {
                try{
                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) holder.MapImage.getDrawable());
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build()); //set external app-sharing permissions

                    File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".png");
                   //String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tracknshare/";
                    //File dir = new File(file_path);
                    //dir.mkdirs();
                    //File file = new File(dir,System.currentTimeMillis()+".png");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream);
                    file.setReadable(true,false);
                    Log.d("uri",file.toURI().toString());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Uri uri = Uri.fromFile(file);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    shareIntent.setType("image/png");
                    context.startActivity(Intent.createChooser(shareIntent, "Send"));

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
    }

    public Drawable saveImage(Drawable d) {
        return d;
    }

//    private Uri getBitmapFromView(Bitmap bmp){
//        Uri bitmapUri = null;
//        try{
////            File file = new File(context.getExternalCacheDir(),System.currentTimeMillis()+".png");
////            FileOutputStream fileOutputStream = new FileOutputStream(file);
////            bmp.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream);
////            file.setReadable(true,false);
////            fileOutputStream.flush();
//            //bitmapUri = Uri.fromFile(file);
//
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
//        Log.d("ERROR",bitmapUri.toString());
//        return bitmapUri;
//    }
    @Override
    public int getItemCount() {
        return runs.size();
    }

}
