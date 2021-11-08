package cat.copernic.prodis.lacantinadeprodis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}