package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorAdministrarProducteBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class PantallaAdministradorAdministrarProducte : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var tipusProducte: String
    lateinit var producte: String

    var arrayTipusProducte = ArrayList<String>()
    var arrayProductes = ArrayList<String>()

    var producteVisible: Boolean = true

    private lateinit var adapter: ArrayAdapter<*>

    lateinit var binding: FragmentPantallaAdministradorAdministrarProducteBinding

    private val db = Firebase.firestore

    private lateinit var documentId: String

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

        carregarSpinTipusProductes()
        carregarSpinProductes()
        producteEsVisible()
        actualitzaNom()
        actualitzaTipusProducte()

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
                    println(documentId)
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

    private fun actualitzaNom() {
        binding.btnGuardar.setOnClickListener() {
            db.collection("productes").document(documentId).update(
                hashMapOf(
                    "nom" to binding.editTextTextProductName.text.toString(),
                    "nomid" to formatCorrecte()
                ) as Map<String, Any>
            )
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

    private fun actualitzaTipusProducte() {
        binding.btnGuardar.setOnClickListener() {
            db.collection("productes").document(documentId).update(
                hashMapOf(
                    "tipus" to binding.spinTipusProducte.selectedItem.toString()
                ) as Map<String, Any>
            )
        }
    }
}