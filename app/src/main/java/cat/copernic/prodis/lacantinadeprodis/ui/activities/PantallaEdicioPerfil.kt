package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaEdicioPerfilBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


class PantallaEdicioPerfil : AppCompatActivity() {
    private lateinit var dni: String

    private val db = FirebaseFirestore.getInstance()

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

    lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<FragmentPantallaEdicioPerfilBinding>(
            this,
            R.layout.fragment_pantalla_edicio_perfil
        )

        var bundle = intent.extras
        dni = bundle?.getString("dni").toString()

        agafarImatgeUsuari()

        binding.btnCambiarFoto.setOnClickListener() { view: View ->
            triaCamGaleria()
        }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    println("entra")
                    binding.editTxtNom.setHint(document.get("username").toString())
                    println(document.get("username").toString())
                    println(dni)
                }
            }



        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTxtCognom.setHint(document.get("usersurname").toString())
                }
            }

        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.editTextCorreu.setHint(document.get("email").toString())
                }
            }

        binding.btnGuardar.setOnClickListener() { view: View ->
            pujarImatge(view)
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

                val currentUserPass =
                    FirebaseAuth.getInstance().currentUser

                currentUserPass?.updatePassword(binding.editTextContrassenya.text.toString())
                    ?.addOnSuccessListener {
                        println("Entra en succeslistener")
                        db.collection("users").document(dni).update(
                            hashMapOf(
                                "passwd" to binding.editTextContrassenya.text.toString()
                            ) as Map<String, Any>
                        )
                    }
                Toast.makeText(this, "Els canvis s'han fet amb èxit", Toast.LENGTH_SHORT).show()
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

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            //setImageUri només funciona per rutes locals, no a internet
            binding.userIcon.setImageURI(data)
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

    fun pujarImatge(view: View) {
        // pujar imatge al Cloud Storage de Firebase
        // https://firebase.google.com/docs/storage/android/upload-files?hl=es

        // Creem una referència amb el path i el nom de la imatge per pujar la imatge
        val pathReference = storageRef.child("users/" + dni + ".jpg")
        val bitmap =
            (binding.userIcon.drawable as BitmapDrawable).bitmap // agafem la imatge del imageView
        val baos = ByteArrayOutputStream() // declarem i inicialitzem un outputstream

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) // convertim el bitmap en outputstream
        val data = baos.toByteArray() //convertim el outputstream en array de bytes.

        val uploadTask = pathReference.putBytes(data)
        uploadTask.addOnFailureListener {
            Snackbar.make(view, "Error al pujar la foto", Snackbar.LENGTH_LONG).show()
            it.printStackTrace()

        }.addOnSuccessListener {
            Snackbar.make(view, "Exit al pujar la foto", Snackbar.LENGTH_LONG).show()
        }
    }

    fun agafarImatgeUsuari() {
        storageRef = FirebaseStorage.getInstance().getReference()

        var imgRef = Firebase.storage.reference.child("users/" + dni + ".jpg")


        imgRef.downloadUrl.addOnSuccessListener { Uri ->
            val imgUrl = Uri.toString()

            Glide.with(this).load(imgUrl).into(binding.userIcon)
        }

    }
}