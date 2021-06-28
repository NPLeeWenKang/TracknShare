package sg.edu.np.tracknshare;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import pub.devrel.easypermissions.EasyPermissions;

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
}
