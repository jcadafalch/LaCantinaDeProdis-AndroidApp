package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.MainActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityComandesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ComandesActivity: AppCompatActivity() {
    private lateinit var usertype: String
    private lateinit var dni: String
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityComandesBinding>(this, R.layout.activity_comandes)

        var bundle = intent.extras
        usertype = bundle?.getString("usertype").toString()
        dni = bundle?.getString("dni").toString()

        title = ""


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
                deleteComanda()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteComanda(){
        db.collection("comandes").get().addOnSuccessListener { result ->
            for (document in result){
                if(document.get("comandaComencada").toString().equals("true") && document.get("user").toString() == dni){
                    val idDocument = document.id
                    db.collection("comandes").document(document.id).collection("productes").get().addOnSuccessListener { document ->
                        for (dc in document){
                            db.collection("comandes").document(idDocument).collection("productes").document(dc.id).delete()
                        }

                    }
                    db.collection("comandes").document(document.id).delete()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteComanda()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        deleteComanda()
    }
}