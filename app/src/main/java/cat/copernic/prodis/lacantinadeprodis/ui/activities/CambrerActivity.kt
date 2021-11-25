package cat.copernic.prodis.lacantinadeprodis.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.prodis.lacantinadeprodis.R

class CambrerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambrer)

       /* val bundle = intent.extras
        val username: String? = bundle?.getString("username")

        setup(username ?: "")*/
    }

    private fun setup (username: String){

    }
}