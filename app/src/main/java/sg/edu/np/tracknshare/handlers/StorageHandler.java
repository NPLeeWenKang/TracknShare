package sg.edu.np.tracknshare.handlers;

import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class StorageHandler {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    public StorageHandler(){}
    public void UploadRunImage(Bitmap bm){
        StorageReference imagesRef = storageRef.child("images");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        imagesRef.child("1").putBytes(data);
    }
}
