package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioNomClientComandaBinding
import com.google.firebase.firestore.FirebaseFirestore

class PantallaSeleccioNomCientComanda : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var userArr: ArrayList<*>
    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var dni: String
    private lateinit var docId: String
    private lateinit var binding: FragmentPantallaSeleccioNomClientComandaBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_nom_client_comanda, container, false
        )
        val args = PantallaSeleccioNomCientComandaArgs.fromBundle(requireArguments())
        userArr = args.arrUser
        dni = args.dni
        docId = args.docId
        spinner = binding.spnNomClient

        val context = this.requireContext()

        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, userArr)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        binding.btnTornaEnrerre.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnConfirmar.setOnClickListener {
            if(spinner.selectedItemPosition == userArr.size-1){
                db.collection("comandes").get().addOnSuccessListener { result ->
                    for (dc in result) {
                        if (dc.get("comandaComencada").toString() == "true" && dc.get("user")
                                .toString() == dni
                        ) {
                            db.collection("comandes").document(docId).update(
                                hashMapOf(
                                    "comandaComencada" to false,
                                    "visible" to true,
                                    "user" to binding.dtTxtNomUsuariExtern.text.toString(),
                                ) as Map<String, Any>
                            )
                            Toast.makeText(this.context, "Comanda de ${binding.dtTxtNomUsuariExtern.text} enviada", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(PantallaSeleccioNomCientComandaDirections.actionPantallaSeleccioNomClientComandaToPantallaSeleccioTipusProducte())
                            break
                        }
                    }
                }
            }else{
                db.collection("comandes").get().addOnSuccessListener { result ->
                    for (dc in result) {
                        if (dc.get("comandaComencada").toString() == "true" && dc.get("user")
                                .toString() == dni
                        ) {
                            db.collection("comandes").document(docId).update(
                                hashMapOf(
                                    "comandaComencada" to false,
                                    "visible" to true,
                                    "user" to spinner.selectedItem.toString(),
                                ) as Map<String, Any>
                            )
                            Toast.makeText(this.context, "Comanda de ${spinner.selectedItem} enviada", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(PantallaSeleccioNomCientComandaDirections.actionPantallaSeleccioNomClientComandaToPantallaSeleccioTipusProducte())
                            break
                        }
                    }
                }
            }


        }


        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == userArr.size - 1) {
            binding.dtTxtNomUsuariExtern.visibility = View.VISIBLE
        } else {
            binding.dtTxtNomUsuariExtern.visibility = View.INVISIBLE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}