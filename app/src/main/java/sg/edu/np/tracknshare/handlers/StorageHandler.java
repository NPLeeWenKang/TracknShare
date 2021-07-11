package sg.edu.np.tracknshare.handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class StorageHandler {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    public StorageHandler(){}

    public void UploadRunImage(long imageId, Bitmap bm){
        Log.d("GENERATEID", "GenerateId: "+imageId);
        StorageReference imagesRef = storageRef.child("images");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        imagesRef.child(""+imageId).putBytes(data);
    }
    public void LoadFileToApp(String imageId, Context c, ImageView imageRef){
        Log.d("GENERATEID", "LoadFileToApp: "+imageId);
        StorageReference imagesRef = storageRef.child("images/"+imageId);
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(c)
                        .load(downloadUrl.toString())
                        .into(imageRef);
                //Send image URI for sharesheet
                SharedPreferences.Editor editor = c.getSharedPreferences("ImageFile",Context.MODE_PRIVATE).edit();
                Log.d("uri",downloadUrl.toString());
                editor.putString("uri",downloadUrl.toString());
                editor.apply();
            }
        });

    }
    public long GenerateId(){
        Calendar calendar = Calendar.getInstance();
        long timeMilli = calendar.getTimeInMillis();
        Log.d("GENERATEID", "GenerateId: "+timeMilli);
        return  timeMilli;
    }

}
