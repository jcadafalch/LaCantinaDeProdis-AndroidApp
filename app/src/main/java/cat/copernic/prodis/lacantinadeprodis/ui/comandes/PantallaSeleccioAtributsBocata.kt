package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioAtributsBocataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PantallaSeleccioAtributsBocata : Fragment() {

    var senseTomaquet : Boolean = false

    var perEemportar : Boolean = false

    private val db = FirebaseFirestore.getInstance()

    lateinit var idProducte: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioAtributsBocataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_atributs_bocata, container, false
        )

        var args = PantallaSeleccioAtributsBegudaArgs.fromBundle(requireArguments())
        idProducte = args.idProductes

        binding.btnConfirmar.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_atributs_bocata_to_pantalla_seleccio_tipus_producte)

            senseTomaquet = binding.checkBoxSenseTomaquet.isChecked

            perEemportar = binding.checkBoxPerEmportar.isChecked

            val currentUser =
                FirebaseAuth.getInstance().currentUser

            var dni: String

            db.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (currentUser?.email.toString() == document.get("email").toString()) {
                        dni = document.id
                        db.collection("comandes").get().addOnSuccessListener { result ->
                            for (document in result) {
                                if (dni.equals(document.get("user"))) {
                                    db.collection("comandes").document(document.id)
                                        .collection("productes").document().set(
                                            hashMapOf(
                                                "idProducte" to idProducte,
                                                "emportar" to perEemportar,
                                                "tomaquet" to senseTomaquet
                                            ) as Map<String, Any>
                                        )
                                }
                            }
                        }
                    }
                }
            }

        }
        return binding.root
    }
}
