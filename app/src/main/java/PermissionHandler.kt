import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionHandler (private val activity: AppCompatActivity){
    private var reload: Boolean = false
    fun getPermission(permission:String, name:String, reloadIntent: Boolean) {
        if (reloadIntent) { reload = true }
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(activity, permission)){
                rationaleRequest(permission, name)
            }
            else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    //Dialog for permission request rationale
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
    //Message feedback for permissionRequest
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