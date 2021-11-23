package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaEdicioPerfilBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class PantallaEdicioPerfil : AppCompatActivity() {
    private lateinit var dni: String

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<FragmentPantallaEdicioPerfilBinding>(
            this,
            R.layout.fragment_pantalla_edicio_perfil
        )

        var bundle = intent.extras
        dni = bundle?.getString("dni").toString()

        var nomBinding = binding.editTxtNom.text.toString()
        var cognomBinding = binding.editTxtCognom.text.toString()

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTxtNom.setHint(document.get("username").toString())
                }
            }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTxtCognom.setHint(document.get("usersurname").toString())
                }
            }

        binding.btnGuardar.setOnClickListener() { view: View ->
            println(nomBinding)
            if (datavalids(
                    binding.editTxtNom.text.toString(),
                    binding.editTxtCognom.text.toString(),
                    binding.editTextCorreu.text.toString()
                )
            ) {
                db.collection("users").document(dni).update(
                    hashMapOf(
                        "username" to binding.editTxtNom.text.toString(),
                        "usersurname" to binding.editTxtCognom.text.toString(),
                        "email" to binding.editTextCorreu.text.toString()
                    ) as Map<String, Any>
                )
            }
        }
    }
    private fun datavalids(nom: String, cognom: String, correu: String): Boolean {
        var error = ""
        var bool = true
        if (nom.isEmpty()) {
            error += "Has d'introduïr el nom\r"
            bool = false
            println("El nom está buiy")
        }
        if (cognom.isEmpty()) {
            error += "Has d'introduïr el cognom\r"
            bool = false
            println("El cognom está buit")
        }
        if (correu.isEmpty()) {
            error += "Has d'introduïr el correu\r"
            bool = false
            println("El correu está buit")
        } else if (!checkEmailFormat(correu)) {
            error = "El format del correu no es correcte"
            bool = false
        }
        if (error != "") {
            showAlert(error)
        }
        return bool
    }

    private fun checkEmailFormat(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    private fun showAlert(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("¡¡¡Error!!!")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
    }

}

