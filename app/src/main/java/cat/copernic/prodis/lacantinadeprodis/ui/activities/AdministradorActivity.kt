package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.MainActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityAdministradorBinding
import com.google.firebase.auth.FirebaseAuth

class AdministradorActivity : AppCompatActivity() {

    private lateinit var dni: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAdministradorBinding>(this,
            R.layout.activity_administrador
        )

        //val navController = this.findNavController(R.id.myNavHostFragment)

        //Fletxa enderrere i faltaria metode onSupportNavigateUp
        //NavigationUI.setupActionBarWithNavController(this, navController)

        var bundle = intent.extras
        dni = bundle?.getString("dni").toString()

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
                    putExtra("dni", dni)
                }
                startActivity(intent)
                true
            }
            R.id.logOutBttn -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}