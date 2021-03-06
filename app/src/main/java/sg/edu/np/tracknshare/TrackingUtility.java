package sg.edu.np.tracknshare;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pub.devrel.easypermissions.EasyPermissions;
import sg.edu.np.tracknshare.models.Comment;

public class TrackingUtility {
    public boolean HasPerms(Context c){
        // checks if phone has permissions
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            boolean accessFine = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean accessCoarse = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            Log.d("PERMS", "HasPerms: "+accessFine+" "+accessCoarse);
            return  accessFine && accessCoarse;
        } else{
            boolean accessFine = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean accessActivity = ContextCompat.checkSelfPermission(c, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
            boolean accessCoarse = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            Log.d("PERMS", "HasPerms: "+accessFine+" "+accessCoarse+" "+accessActivity);
            return  accessFine && accessCoarse && accessActivity;

        }
    }
    public boolean PermsDeniedPermemently(Context c){
        // checks if phone has permemently blocked permissions
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            boolean accessFine = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean accessCoarse = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.d("PERMS", "PermsDeniedPermemently: "+accessFine+" "+accessCoarse);
            return  !(accessFine && accessCoarse);
        } else{
            boolean accessFine = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean accessActivity = ContextCompat.checkSelfPermission(c, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
            boolean accessCoarse = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.d("PERMS", "PermsDeniedPermemently: "+accessFine+" "+accessCoarse+" "+accessActivity);
            return  !(accessFine && accessCoarse && accessActivity);
        }
    }
    public void requestPermission(Context c){
        // request permissions from the phone
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            ActivityCompat.requestPermissions((Activity) c,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else{
            ActivityCompat.requestPermissions((Activity) c,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION},
                    1);
        }
    }


    public boolean isMyServiceRunning(Class<?> serviceClass, Context c) {
        // checks if the TrackingService is running
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
