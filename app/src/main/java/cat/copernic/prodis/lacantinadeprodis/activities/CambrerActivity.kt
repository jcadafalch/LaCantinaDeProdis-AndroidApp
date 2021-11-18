package cat.copernic.prodis.lacantinadeprodis.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import cat.copernic.prodis.lacantinadeprodis.MainActivity
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.homeBtn -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                }
                startActivity(intent)
                true
            }
            R.id.profileBttn -> {
                val intent = Intent(this, PantallaEdicioPerfil::class.java).apply {
                }
                startActivity(intent)
                true
            }
            R.id.logOutBttn -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}