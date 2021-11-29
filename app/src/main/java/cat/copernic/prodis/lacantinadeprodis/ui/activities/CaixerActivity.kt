package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.MainActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityCaixaBinding

class CaixerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCaixaBinding>(this,
            R.layout.activity_caixa
        )

        //val navController = this.findNavController(R.id.myNavHostFragment)

        //Fletxa enderrere i faltaria metode onSupportNavigateUp
        //NavigationUI.setupActionBarWithNavController(this, navController)

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