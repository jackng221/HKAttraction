package shape.computing.hkattraction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timer().schedule(3000){
            enterApp()
        }
    }

    private fun enterApp(){
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }
}