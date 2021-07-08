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
    public boolean HasPermissions(Context c){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            return EasyPermissions.hasPermissions(c,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        } else{
            return EasyPermissions.hasPermissions(c,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
    }
    public boolean HasPerms(Context c){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            boolean accessFine = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean accessCoarse = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            Log.d("PERMS", "HasPerms: "+accessFine+" "+accessCoarse);
            Log.d("PERMS", "HasPerms: "+(accessFine && accessCoarse));
            return  accessFine && accessCoarse;
        } else{
            boolean accessFine = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean accessCoarse = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//            boolean accessBackground = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
//            Log.d("PERMS", "HasPerms: "+accessFine+" "+accessCoarse+" "+accessBackground);
//            Log.d("PERMS", "HasPerms: "+(accessFine && accessCoarse && accessBackground));
            return  accessFine && accessCoarse;
        }
    }
    public boolean PermsDeniedPermemently(Context c){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            boolean accessFine = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean accessCoarse = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.d("PERMS", "PermsDeniedPermemently: "+accessFine+" "+accessCoarse);
            return  !(accessFine && accessCoarse);
        } else{
            boolean accessFine = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_FINE_LOCATION);
            boolean accessCoarse = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_COARSE_LOCATION);
//            boolean accessBackground = ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//            Log.d("PERMS", "PermsDeniedPermemently: "+accessFine+" "+accessCoarse+" "+accessBackground);
//            Log.d("PERMS", "PermsDeniedPermemently: "+(accessFine && accessCoarse && accessBackground));
            return  !(accessFine && accessCoarse);
        }
    }
    public void requestPermission(Context c){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            ActivityCompat.requestPermissions((Activity) c,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else{
            ActivityCompat.requestPermissions((Activity) c,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }


    public boolean isMyServiceRunning(Class<?> serviceClass, Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
