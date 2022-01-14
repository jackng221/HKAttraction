import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionHandler (private val activity: AppCompatActivity){
    // if reload activity after handling
    private var reload: Boolean = false
    // if permission not acquired, do rationale request or normal request
    fun getPermission(permission:String, name:String, reloadActivity: Boolean) {
        if (reloadActivity) { reload = true }
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(activity, permission)){
                rationaleRequest(permission, name)
            }
            else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    // permission request with rationale dialog
    private fun rationaleRequest(permission:String, name:String){
        val builder = AlertDialog.Builder(activity)
        builder.apply{
            setTitle("$name access denied")
            setMessage("press OK to give permission")
            setPositiveButton("OK"){dialog, which -> requestPermissionLauncher.launch(permission)}
            setNegativeButton("Cancel"){dialog, which -> }
        }
        val dialog = builder.create()
        dialog.show()
    }
    // permission request
    // show message feedback; optional reload
    private val requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted:Boolean ->
        if (isGranted){
            Toast.makeText(activity.applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
            if (reload){
                activity.finish()
                activity.startActivity(activity.intent)
            }
        }
        else {
            Toast.makeText(activity.applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}