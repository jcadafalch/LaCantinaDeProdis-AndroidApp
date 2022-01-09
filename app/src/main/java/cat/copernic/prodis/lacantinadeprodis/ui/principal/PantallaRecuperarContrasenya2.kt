package cat.copernic.prodis.lacantinadeprodis.ui.principal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRecuperarContrasenya2Binding
import cat.copernic.prodis.lacantinadeprodis.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PantallaRecuperarContrasenya2 : Fragment() {

    private val db = Firebase.firestore
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaRecuperarContrasenya2Binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_recuperar_contrasenya2, container, false
        )
        val args = PantallaRecuperarContrasenya2Args.fromBundle(requireArguments())
        val usertype = args.usertype
        val dni = args.dni

        binding.btnPRecuperarContrasenya2Continuar.setOnClickListener {
            if (!binding.dtTxtPRegistrePasswordText.text.isNullOrEmpty() &&
                !binding.dtTxtPRegistreRepeteixPasswordText.text.isNullOrEmpty()
            ) {
                println("NOT NULL")
                if (binding.dtTxtPRegistrePasswordText.text.toString() ==
                    binding.dtTxtPRegistreRepeteixPasswordText.text.toString()
                ) {
                    println("CONTRASENYES IGUALS")
                    println("PSW1  = " + binding.dtTxtPRegistrePasswordText.text.toString())
                    println("PSW2 = " + binding.dtTxtPRegistreRepeteixPasswordText.text.toString())

                    val psswd =
                        binding.dtTxtPRegistreRepeteixPasswordText.text.toString() + "prodis"
                    println("PSSWD = $psswd DNI = $dni USERTYPE = $usertype")
                    changePassword(dni, psswd, usertype)

                } else {
                    println("PSW1  = " + binding.dtTxtPRegistrePasswordText.text.toString())
                    println("PSW2 = " + binding.dtTxtPRegistreRepeteixPasswordText.text.toString())
                    utils().showAlert(getString(R.string.error), getString(R.string.les_contrasenyes_no_coincideixen), this.context)
                }
            } else {
                utils().showAlert(getString(R.string.error), getString(R.string.els_camps_no_estan_plens), this.context)
            }
        }

        return binding.root
    }

    private fun changePassword(dni: String, psswd: String, usertype: String) {
        db.collection("users").document(dni).get().addOnSuccessListener { result ->
            auth.signInWithEmailAndPassword(
                result.get("email").toString(),
                result.get("password").toString(),
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.updatePassword(psswd)?.addOnSuccessListener {
                        auth.signOut()
                        Toast.makeText(
                            this.context,
                            getString(R.string.s_ha_canviat_la_contrasenya),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val pas = hashMapOf("password" to psswd)
                    db.collection("users").document(dni).update(
                        pas as Map<String, Any>
                    )
                    view?.findNavController()?.navigate(
                        PantallaRecuperarContrasenya2Directions.actionPantallaRecuperarContrasenya2ToPantallaIniciSessioClientAdmin(
                            usertype
                        )
                    )
                } else {
                    utils().showAlert(getString(R.string.error), getString(R.string.error_en_recuperar_contrasenya), this.context)
                }
            }
        }
            .addOnFailureListener {
                utils().showAlert(getString(R.string.error), getString(R.string.l_usuari_no_esta_registrat), this.context)
            }

    }

}
