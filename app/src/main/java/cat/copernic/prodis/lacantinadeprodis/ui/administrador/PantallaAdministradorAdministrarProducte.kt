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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorAdministrarProducteBinding
import cat.copernic.prodis.lacantinadeprodis.viewmodel.PantallaAdministradorAdministrarProducteViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class PantallaAdministradorAdministrarProducte : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var tipusProducte: String
    lateinit var producte: String
    private var preu: Double = 0.0
    lateinit var prevProducte : String

    private lateinit var viewModel: PantallaAdministradorAdministrarProducteViewModel


    var arrayTipusProducte = ArrayList<String>()
    var arrayProductes = ArrayList<String>()

    var producteVisible: Boolean = true

    private lateinit var adapter: ArrayAdapter<*>

    lateinit var binding: FragmentPantallaAdministradorAdministrarProducteBinding

    private val db = Firebase.firestore
    lateinit var storageRef: StorageReference

    private lateinit var documentId: String

    private var latestTmpUri: Uri? = null

    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.imgProducte2.setImageURI(uri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pantalla_administrador_administrar_producte,
            container,
            false
        )
        binding.editTextNumberDecimal3.isGone = true
        storageRef = FirebaseStorage.getInstance().getReference()
        viewModel = ViewModelProvider(this).get(PantallaAdministradorAdministrarProducteViewModel::class.java)


        carregarSpinTipusProductes()
        carregarSpinProductes()
        producteEsVisible()
        setPreu()
        actualitzaDades()
        actualitzaFoto()

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        if (parent == binding.spinTipusProducte) {
            tipusProducte = parent.getItemAtPosition(pos).toString()

        } else if (parent == binding.spinSeleccionaProducte) {
            producte = parent.getItemAtPosition(pos).toString()
            binding.editTextTextProductName.setText(binding.spinSeleccionaProducte.selectedItem.toString())
        }

        db.collection("productes").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.get("nom").toString().equals(producte)) {
                    documentId = document.id
                }
            }
        }
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

    private fun carregarSpinTipusProductes() {
        val context = this.requireContext()

        val spinner: Spinner = binding.spinTipusProducte

        val args = PantallaAdministradorAdministrarProducteArgs.fromBundle(requireArguments())

        if (arrayTipusProducte.isEmpty()) {
            arrayTipusProducte = args.arrayTipusProducte as ArrayList<String>
        }

        adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayTipusProducte)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = this
    }

    private fun carregarSpinProductes() {
        val context = this.requireContext()

        val spinner: Spinner = binding.spinSeleccionaProducte

        val args = PantallaAdministradorAdministrarProducteArgs.fromBundle(requireArguments())

        if (arrayProductes.isEmpty()) {
            arrayProductes = args.arrayProductes as ArrayList<String>
        }

        adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayProductes)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = this
    }

    private fun producteEsVisible() {
        binding.btnGuardar.setOnClickListener() {
            if (binding.checkBoxProducteVisible.isChecked) {
                producteVisible = true
            }
        }
    }

    private fun formatCorrecte(): String {
        var string = binding.editTextTextProductName.text.toString()

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

    private fun actualitzaDades() {
        binding.btnGuardar.setOnClickListener() {
            if (!binding.editTextNumberDecimal3.text.toString().isEmpty()) {
                preu = binding.editTextNumberDecimal3.text.toString().toDouble()
            }
            prevProducte = binding.editTextTextProductName.text.toString()

            borrarImatge()
            pujarImatge(it)

            db.collection("productes").document(documentId).update(
                hashMapOf(
                    "tipus" to binding.spinTipusProducte.selectedItem.toString(),
                    "nom" to binding.editTextTextProductName.text.toString(),
                    "nomid" to formatCorrecte(),
                    "preu" to preu,
                    "img" to "productes/" + binding.editTextTextProductName.text.toString() + ".jpg"
                ) as Map<String, Any>
            )
        }
    }

    fun setPreu() {
        binding.radioGroup2
            .setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radio1Euro2 -> {
                        preu = 1.0
                        binding.editTextNumberDecimal3.visibility = View.INVISIBLE
                        binding.editTextNumberDecimal3.setText("")
                        binding.editTextNumberDecimal3.isGone = true

                    }
                    R.id.radio2Euro2 -> {
                        preu = 2.0
                        binding.editTextNumberDecimal3.visibility = View.INVISIBLE
                        binding.editTextNumberDecimal3.setText("")
                        binding.editTextNumberDecimal3.isGone = true

                    }
                    R.id.radioAltrePreu2 -> {
                        binding.editTextNumberDecimal3.visibility = View.VISIBLE
                        binding.editTextNumberDecimal3.isGone = false
                    }
                }
            }
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            //setImageUri només funciona per rutes locals, no a internet
            binding.imgProducte2.setImageURI(data)
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

    private fun actualitzaFoto(){
        binding.imgCamera.setOnClickListener(){
            obrirCamera()
        }

        binding.imgPujarImatge.setOnClickListener(){
            obrirGaleria()
        }
    }

    private fun borrarImatge(){
        println(prevProducte)
        val pathReference =
            storageRef.child("productes/"+ prevProducte +".png")



        pathReference.delete()
    }

    fun pujarImatge(view: View) {
        // pujar imatge al Cloud Storage de Firebase
        // https://firebase.google.com/docs/storage/android/upload-files?hl=es

        // Creem una referència amb el path i el nom de la imatge per pujar la imatge
        val pathReference =
            storageRef.child("productes/" + binding.editTextTextProductName.text.toString() + ".png")
        val bitmap =
            (binding.imgProducte2.drawable as BitmapDrawable).bitmap // agafem la imatge del imageView
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