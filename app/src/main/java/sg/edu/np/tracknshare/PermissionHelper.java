package sg.edu.np.tracknshare;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

/*This PermissionHandler is used to implement the permissions for FragmentActivity where the map needs to go to the Current Location of the user
as soon as the permission is accepted by the user*/

public class PermissionHelper {
    public void startPermissionRequest(FragmentActivity fragmentActivity, PermissionInterface permissionInterface, String manifest){
        ActivityResultLauncher<String> requestPermissionLauncher =
                fragmentActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), permissionInterface::onGranted);
        requestPermissionLauncher.launch(
                manifest);
    }
}
