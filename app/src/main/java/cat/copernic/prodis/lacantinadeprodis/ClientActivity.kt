package cat.copernic.prodis.lacantinadeprodis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityMainBinding

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}