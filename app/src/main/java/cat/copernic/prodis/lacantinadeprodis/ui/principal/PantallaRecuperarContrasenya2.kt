package cat.copernic.prodis.lacantinadeprodis.ui.principal

import android.app.AlertDialog
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
                    println("PSW2 = "+ binding.dtTxtPRegistreRepeteixPasswordText.text.toString())

                    val psswd = binding.dtTxtPRegistreRepeteixPasswordText.text.toString()+ "prodis"
                    println("PSSWD = $psswd DNI = $dni USERTYPE = $usertype")
                    changePassword(dni,psswd, usertype)

                } else {
                    println("PSW1  = " + binding.dtTxtPRegistrePasswordText.text.toString())
                    println("PSW2 = "+ binding.dtTxtPRegistreRepeteixPasswordText.text.toString())
                    showAlert("Les contrasenyes no coincideixen.")
                }
            } else {
                showAlert("Els camps no están plens")
            }
        }

        return binding.root
    }

    private fun changePassword(dni: String, psswd: String, usertype: String){
        var bool = false
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id == dni) {
                    bool = true
                    println("FIND DOCUMENT")
                    println("EMAIL ID = " + document.get("email").toString())
                    println("ID PASSWORD  = " + document.get("password").toString())
                    println("PSSWD = $psswd")
                    auth.signInWithEmailAndPassword(
                        document.get("email").toString(),
                        document.get("password").toString(),
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            println("SUCCESSFUL")
                            val currentUser = auth.currentUser
                            currentUser?.updatePassword(psswd)?.addOnSuccessListener {
                                println("SUCCESSFUL")
                                db.collection("users").document(dni).update(
                                    hashMapOf(
                                        "password" to psswd
                                    ) as Map<String, Any>
                                )
                                auth.signOut()
                                Toast.makeText(this.context, "S'ha canbiat la contrasenya", Toast.LENGTH_SHORT).show()
                                view?.findNavController()?.navigate(PantallaRecuperarContrasenya2Directions.actionPantallaRecuperarContrasenya2ToPantallaIniciSessioClientAdmin(usertype))
                            }
                        } else {
                            showAlert("Error en inici de sessió")
                        }
                    }
                }
            }
            if (!bool){
                showAlert("L\'usuari no està registrat")
            }
        }

    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("ERROR")
        builder.setMessage(msg)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
