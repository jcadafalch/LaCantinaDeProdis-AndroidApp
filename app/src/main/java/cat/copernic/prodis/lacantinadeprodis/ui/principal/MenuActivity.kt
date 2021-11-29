package cat.copernic.prodis.lacantinadeprodis.ui.principal

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.prodis.lacantinadeprodis.MainActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.ui.activities.PantallaEdicioPerfil


class MenuActivity : AppCompatActivity() {
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