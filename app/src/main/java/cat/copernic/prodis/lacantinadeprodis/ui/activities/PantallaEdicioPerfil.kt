package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaEdicioPerfilBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.ui.principal.PantallaRecuperarContrasenya2Directions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.processNextEventInCurrentThread
import java.io.File


class PantallaEdicioPerfil : AppCompatActivity() {
    private lateinit var dni: String

    private val db = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    lateinit var binding: FragmentPantallaEdicioPerfilBinding
    private var latestTmpUri: Uri? = null
    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.userIcon.setImageURI(uri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<FragmentPantallaEdicioPerfilBinding>(
            this,
            R.layout.fragment_pantalla_edicio_perfil
        )

        var bundle = intent.extras
        dni = bundle?.getString("dni").toString()

        binding.btnCambiarFoto.setOnClickListener() { view: View ->
            triaCamGaleria()
        }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTxtNom.setText(document.get("username").toString())
                }
            }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTxtCognom.setText(document.get("usersurname").toString())
                }
            }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTextCorreu.setText(document.get("email").toString())
                }
            }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTextContrassenya.setHint(document.get("password").toString())
                }
            }

        binding.btnGuardar.setOnClickListener() { view: View ->
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
                    ) as Map<String, Any>
                )
                val currentUser =
                    FirebaseAuth.getInstance().currentUser ?: return@setOnClickListener
                if (binding.editTextCorreu.text.toString() != currentUser.email) {
                    currentUser
                        .updateEmail(binding.editTextCorreu.text.toString())
                        .addOnSuccessListener {
                            db.collection("users").document(dni).update(
                                hashMapOf(
                                    "email" to binding.editTextCorreu.text.toString()
                                ) as Map<String, Any>
                            )
                        }
                }
                changePassword(dni, binding.editTextContrassenya.text.toString(), "client")
                //changePass()
                //Toast.makeText(this, "Els canvis s'han fet amb èxit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun datavalids(nom: String, cognom: String, correu: String): Boolean {
        var error = ""
        var bool = true
        if (nom.isEmpty()) {
            error += "Has d'introduïr el nom\r"
            bool = false
        }
        if (cognom.isEmpty()) {
            error += "Has d'introduïr el cognom\r"
            bool = false
        }
        if (correu.isEmpty()) {
            error += "Has d'introduïr el correu\r"
            bool = false
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

    private fun cam() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            //setImageUri només funciona per rutes locals, no a internet
            binding?.userIcon?.setImageURI(data)
        }
    }

    private fun obrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun obrirCamera() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri

                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "cat.copernic.prodis.lacantinadeprodis.provider",
            tmpFile
        )
    }

    fun triaCamGaleria() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("D'on vols treure la foto?")
        alertDialog.setMessage("Selecciona un:")

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "GALERIA"
        ) { dialog, which -> obrirGaleria() }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "CÀMERA"
        ) { dialog, which -> obrirCamera() }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }


    private fun changePassword(dni: String, psswd: String, usertype: String) {
        var bool = false
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == dni) {
                    bool = true
                    println("FIND DOCUMENT")
                    println("EMAIL ID = " + document.get("email").toString())
                    println("ID PASSWORD  = " + document.get("password").toString())
                    println("PSSWD = $psswd")
                    auth.signInWithEmailAndPassword(
                        document.get("email").toString(),
                        document.get("password").toString(),
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            println("SUCCESSFUL")
                            val currentUser = auth.currentUser
                            currentUser!!.updatePassword(psswd).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    println("SUCCESSFUL")
                                    db.collection("users").document(dni).update(
                                        hashMapOf(
                                            "password" to psswd + "prodis"
                                        ) as Map<String, Any>
                                    )
                                }
                                auth.signOut()
                                Toast.makeText(
                                    this,
                                    "S'ha canbiat la contrasenya",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            showAlert("Error en inici de sessió")
                        }
                    }
                }
            }
            if (!bool) {
                showAlert("L\'usuari no està registrat")
            }
        }

    }

/*fun changePass(email: String, passw: String, dni: String) {
        val currentUserPass =
            FirebaseAuth.getInstance().currentUser

        db.collection("users").get().addOnSuccessListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                    }
                }
        }
        currentUserPass?.updatePassword(binding.editTextContrassenya.text.toString())
            ?.addOnSuccessListener {
                //if (task.isSuccessful) {
                println("Entra en succeslistener")
                db.collection("users").document(dni).update(
                    hashMapOf(
                        "password" to binding.editTextContrassenya.text.toString()
                    ) as Map<String, Any>
                )
                // }
            }
    }*/
}