package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorNouProducteBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class PantallaAdministradorNouProducte : Fragment(), AdapterView.OnItemSelectedListener {

    //Definim les variables globals
    lateinit var tipusProducte: String

    lateinit var binding: FragmentPantallaAdministradorNouProducteBinding

    private var latestTmpUri: Uri? = null

    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.imgProducte.setImageURI(uri)
                }
            }
        }

    private var preu: Double = 0.0

    private val db = Firebase.firestore

    lateinit var storageRef: StorageReference

    private lateinit var adapter: ArrayAdapter<*>

    var arrayTipusProducte = ArrayList<String>()

    //Comennça el onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_nou_producte, container, false
        )
        //Fem que l'edit text i l'imatge del producte s'amaguin.
        binding.editTextNumberDecimal2.isGone = true
        binding.imgProducte.isGone = true

        //Inicialitzem l'storageRef.
        storageRef = FirebaseStorage.getInstance().getReference()
        //Inicialitzem el tipus de producte.
        tipusProducte = ""

        //Declarem i inicialitzem l'spinner
        val spinner: Spinner = binding.spinTipusProducte

        val context = this.requireContext()
        //Declarem i inicialitzem la variable args per agafar les dades que pasam per parametres
        val args = PantallaAdministradorNouProducteArgs.fromBundle(requireArguments())

        //Fem que si l'array de tipus de productes es buit, li pasarem els args en forma de ArrayList
        if (arrayTipusProducte.isEmpty()) {
            arrayTipusProducte = args.arrayTipusProducte as ArrayList<String>
        }

        //Inicialitzem l'adapter amb l'array de tispus de productes
        adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayTipusProducte)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = this

        binding.imgFotoCamera.setOnClickListener() {
            obrirCamera()
            binding.imgProducte.visibility = View.VISIBLE
            binding.imgProducte.isGone = false

        }

        binding.imgFotoGaleria.setOnClickListener() {
            obrirGaleria()
            binding.imgProducte.visibility = View.VISIBLE
            binding.imgProducte.isGone = false
        }

        binding.imgProducte.isEnabled = false

        setPreu()


        var num: Int
        num = 0

        binding.btnPAdministradorNouProducteGuardar.setOnClickListener {
            pujarImatge(it)

            when (tipusProducte) {
                "Bocata" -> tipusProducte = "bocata"
                "Beguda calenta" -> tipusProducte = "bCalenta"
                "Beguda freda" -> tipusProducte = "bFreda"
            }

            if (!binding.editTextNumberDecimal2.text.toString().isEmpty()) {
                preu = binding.editTextNumberDecimal2.text.toString().toDouble()
            }

            db.collection("productes").get().addOnSuccessListener { result ->
                for (document in result) {
                    num++
                }
                db.collection("productes").document().set(
                    hashMapOf(
                        "nom" to binding.editTextNomProducte.text.toString(),
                        "preu" to preu,
                        "tipus" to tipusProducte,
                        "visible" to true,
                        "nomid" to formatCorrecte(),
                        "img" to "productes/" + binding.editTextNomProducte.text.toString() + ".jpg",
                        "idProducte" to num
                    ) as Map<String, Any>
                )
            }


            Toast.makeText(
                this.requireContext(),
                "S'ha afegit el producte amb éxit",
                Toast.LENGTH_SHORT
            ).show()
            num = 0
        }
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        tipusProducte = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        showAlert("Has de seleccionar un tipus de prodcute")
    }

    private fun showAlert(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
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
            binding.imgProducte.setImageURI(data)
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
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "cat.copernic.prodis.lacantinadeprodis.provider",
            tmpFile
        )
    }

    fun setPreu() {
        binding.radioGroup
            .setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radio1Euro -> {
                        preu = 1.0
                        binding.editTextNumberDecimal2.visibility = View.INVISIBLE
                        binding.editTextNumberDecimal2.setText("")
                        binding.editTextNumberDecimal2.isGone = true
                    }
                    R.id.radio2Euro -> {
                        preu = 2.0
                        binding.editTextNumberDecimal2.visibility = View.INVISIBLE
                        binding.editTextNumberDecimal2.setText("")
                        binding.editTextNumberDecimal2.isGone = true
                    }
                    R.id.radioAltrePreu -> {
                        binding.editTextNumberDecimal2.visibility = View.VISIBLE
                        binding.editTextNumberDecimal2.isGone = false
                    }
                }
            }
    }

    private fun formatCorrecte(): String {
        var string = binding.editTextNomProducte.text.toString()

        string = string.lowercase(Locale.getDefault())

        string = string.replace(
            " de ",
            ""
        )

        string = string.replace(
            " ",
            ""
        )

        string = string.replace(
            " amb ",
            ""
        )

        string = string.replace(
            " i ",
            ""
        )

        string = string.trim()

        return string
    }

    fun pujarImatge(view: View) {
        // pujar imatge al Cloud Storage de Firebase
        // https://firebase.google.com/docs/storage/android/upload-files?hl=es

        // Creem una referència amb el path i el nom de la imatge per pujar la imatge
        val pathReference =
            storageRef.child("productes/" + binding.editTextNomProducte.text.toString() + ".png")
        val bitmap =
            (binding.imgProducte.drawable as BitmapDrawable).bitmap // agafem la imatge del imageView
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
}