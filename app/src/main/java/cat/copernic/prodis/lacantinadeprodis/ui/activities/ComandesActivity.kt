package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.app.Activity
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

    //Funció per inflar el menú
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Funció per indicar que fará cada botó del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.arrowBackBtn -> {
                //Anirá a la pantalla anterior
                this.onBackPressed()
                true
            }
            R.id.homeBtn -> {
                //Anirá a la pantalla principal
                val intent = Intent(this, MainActivity::class.java).apply {
                }
                startActivity(intent)
                true
            }
            R.id.profileBttn -> {
                //Anirá a la pantalla d'edició de perfil
                val intent = Intent(this, PantallaEdicioPerfil::class.java).apply {
                }
                startActivity(intent)
                true
            }
            //Tancará sessió
            R.id.logOutBttn -> {
                FirebaseAuth.getInstance().signOut()
                finish()
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

    fun finishFun(){
        finish()
    }
}