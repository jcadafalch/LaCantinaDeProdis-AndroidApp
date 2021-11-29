package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorNouProducteBinding
import java.io.File
import kotlin.properties.Delegates

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_nou_producte, container, false
        )

        val spinner: Spinner = binding.spinTipusProducte

        val context = this.requireContext()

        ArrayAdapter.createFromResource(
            context,
            R.array.tipus_producte,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        binding.imgFotoCamera.setOnClickListener(){
            obrirCamera()
            binding.imgProducte.visibility = View.VISIBLE
            }

        binding.imgFotoGaleria.setOnClickListener(){
            obrirGaleria()
            binding.imgProducte.visibility = View.VISIBLE
        }

        binding.btnPAdministradorNouProducteGuardar.setOnClickListener{

            preu = binding.editTextNumberDecimal2.text.toString().toDouble()
            println(id)
            println(preu)
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
            binding?.imgProducte?.setImageURI(data)
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
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "cat.copernic.prodis.lacantinadeprodis.provider",
            tmpFile
        )
    }

    fun setPreu(view: View) {

        if (view is RadioButton) {
            val checked = (view as RadioButton).isChecked

            when ((view as RadioButton).getId()) {
                R.id.radio1Euro ->
                    if (checked) {
                        preu = 1.0
                    }
                R.id.radio2Euro ->
                    if (checked) {
                        preu = 2.0
                    }
                R.id.radioAltrePreu ->
                    if (checked) {
                        binding.editTextNumberDecimal2.visibility = View.VISIBLE

                        preu = binding.editTextNumberDecimal2.text.toString().toDouble()
                    }
            }
        }
    }
}