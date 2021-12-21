package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioAtributsBegudaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PantallaSeleccioAtributsBeguda : Fragment() {

    lateinit var binding: FragmentPantallaSeleccioAtributsBegudaBinding
    private val db = FirebaseFirestore.getInstance()


    //Definició de les variables per determinar els atributs
    var atributs = "a"
    var perEmportar: Boolean = false

    lateinit var idProducte: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_atributs_beguda, container, false
        )

        var args = PantallaSeleccioAtributsBegudaArgs.fromBundle(requireArguments())
        idProducte = args.idProductes.toString()

        val currentUser =
            FirebaseAuth.getInstance().currentUser

        var dni: String



        binding.btnConfirmar.setOnClickListener { view: View ->
            setAtributs()

            db.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (currentUser?.email.toString() == document.get("email").toString()) {
                        dni = document.id
                        db.collection("comandes").get().addOnSuccessListener { result ->
                            for (document in result) {
                                if (dni.equals(document.get("user"))){
                                    db.collection("comandes").document(document.id).set(
                                        hashMapOf(
                                            idProducte to mapOf(
                                                "emportar" to perEmportar.toString(),
                                                "quantitat" to +1,
                                                "sucre" to  arrayListOf<String>(atributs)
                                            )
                                        )as Map<String, Any>
                                    )
                                }
                            }
                        }
                    }
                }
            }
            view.findNavController()
                .navigate(
                    PantallaSeleccioAtributsBegudaDirections.actionPantallaSeleccioAtributsBegudaToPantallaSeleccioTipusProducte(
                        atributs,
                        perEmportar
                    )
                )
        }

        binding.btnTornaEnrerre.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    fun setAtributs() {
        binding.rarioGroupAtributs
            .setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioSenseSucre -> {
                        atributs = "SS"
                    }
                    R.id.radioSucreBlanc -> {
                        atributs = "SB"
                    }
                    R.id.radioSucreMore -> {
                        atributs = "SM"
                    }
                    R.id.radioSacarina -> {
                        atributs = "SC"
                    }
                }
            }
        perEmportar = binding.checkPerEmportar.isChecked
    }
}
