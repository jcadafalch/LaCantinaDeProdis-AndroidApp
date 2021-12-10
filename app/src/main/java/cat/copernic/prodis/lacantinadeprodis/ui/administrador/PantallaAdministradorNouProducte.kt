package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.app.Activity
import android.app.AlertDialog
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
import com.google.protobuf.Empty
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class PantallaAdministradorNouProducte : Fragment(), AdapterView.OnItemSelectedListener {

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

    private var accepta: Boolean = false

    lateinit var storageRef: StorageReference

    private lateinit var adapter: ArrayAdapter<*>

    var arrayTipusProducte = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_nou_producte, container, false
        )

        storageRef = FirebaseStorage.getInstance().getReference()

        tipusProducte = ""

        val spinner: Spinner = binding.spinTipusProducte

        val context = this.requireContext()

        val args = PantallaAdministradorNouProducteArgs.fromBundle(requireArguments())

        if (arrayTipusProducte.isEmpty()) {
            println("Is empty")
            arrayTipusProducte = args.arrayTipusProducte as ArrayList<String>
        }

        adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayTipusProducte)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = this

        binding.imgFotoCamera.setOnClickListener() {
            obrirCamera()
            binding.imgProducte.visibility = View.VISIBLE
        }

        binding.imgFotoGaleria.setOnClickListener() {
            obrirGaleria()
            binding.imgProducte.visibility = View.VISIBLE
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
                    if (!document.get("nomid").toString().equals(formatCorrecte())) {
                        if (!document.id.equals(num.toString())) {
                            db.collection("productes").document(num.toString()).set(
                                hashMapOf(
                                    "nom" to binding.editTextNomProducte.text.toString(),
                                    "preu" to preu,
                                    "tipus" to tipusProducte,
                                    "visible" to true,
                                    "nomid" to formatCorrecte(),
                                    "img" to "productes/" + binding.editTextNomProducte.text.toString() + ".jpg"
                                ) as Map<String, Any>
                            )
                            break

                        } else {
                            num++
                        }
                    }
                }
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

                    }
                    R.id.radio2Euro -> {
                        preu = 2.0
                        binding.editTextNumberDecimal2.visibility = View.INVISIBLE
                        binding.editTextNumberDecimal2.setText("")

                    }
                    R.id.radioAltrePreu -> {
                        binding.editTextNumberDecimal2.visibility = View.VISIBLE

                    }
                }
            }
    }

    fun formatCorrecte(): String {
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

    /*private fun sobreescriure(): Boolean {

        val alertDialog = AlertDialog.Builder(this.requireContext()).create()
        alertDialog.setTitle("ATENCIÓ! PRODUCTE DUPLICAT!")
        alertDialog.setMessage("Ja hi ha un producte amb aquest nom.\r Vols sobreescriure els canvis?")

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Si"
        ) { dialog, which -> accepta = true }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "No"
        ) { dialog, which -> accepta = false }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams

        return accepta
    }*/

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