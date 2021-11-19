package cat.copernic.prodis.lacantinadeprodis.ui.principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRegistreBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class PantallaRegistre : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bdng: FragmentPantallaRegistreBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_registre, container, false
        )
        val args = PantallaRegistreArgs.fromBundle(requireArguments())
        val usertype = args.usertype

        //Analytics Event
        val analytics = this.context?.let { FirebaseAnalytics.getInstance(it) }
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics?.logEvent("InitScreen", bundle)

        bdng.btnPregistre.setOnClickListener { view: View ->
            println(bdng.dtTxtPRegistrePassword.text.toString())
            println(bdng.dtTxtPRegistreRepeteixPassword.text.toString())
            if (datavalids(
                    bdng.dtTxtPRegistrePersonName.text.toString(),
                    bdng.dtTxtPRegistreDni.text.toString(),
                    bdng.dtTxtPRegistreEmail.text.toString(),
                    bdng.dtTxtPRegistrePassword.text.toString(),
                    bdng.dtTxtPRegistreRepeteixPassword.text.toString(),
                    bdng.checkBox.isChecked
                )
            ) {
                makeregister(
                    bdng.dtTxtPRegistrePersonName.text.toString(),
                    bdng.dtTxtPRegistreDni.text.toString(),
                    bdng.dtTxtPRegistreEmail.text.toString(),
                    bdng.dtTxtPRegistrePassword.text.toString(),
                    usertype)
                view.findNavController().navigate(
                    PantallaRegistreDirections.actionPantallaRegistreToPantallaIniciSessioClientAdmin(
                        usertype
                    )
                )
            }

        }

        bdng.textPregistreIniciaSessio.setOnClickListener { view: View ->
            view.findNavController().navigate(
                PantallaRegistreDirections.actionPantallaRegistreToPantallaIniciSessioClientAdmin(
                    usertype
                )
            )
        }
        return bdng.root
    }

    private fun makeregister(
        nomCognom: String,
        dni: String,
        email: String,
        password: String,
        usertype: String
    ) {
        val passwd = password + "prodis"
        println(passwd)
        var bool = false
        var bool1 = false
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    bool = true

                } else {
                    showAlert("Error a l\'hora de fer l\'autenticació")
                }
            }

        db.collection("users").document(dni).set(
            hashMapOf(
                "username" to nomCognom,
                "dni" to dni,
                "email" to email,
                "password" to passwd,
                "usertype" to usertype
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                bool1 = true
            } else {
                showAlert("Error a l\'hora de fer el guardat de dades")
            }
        }

        if (bool && bool1) {
            Toast.makeText(this.context, "T\'has registrat correctament", Toast.LENGTH_SHORT).show()
        }
    }

    private fun datavalids(
        nomCognom: String,
        dni: String,
        email: String,
        password: String,
        password2: String,
        checkbox: Boolean
    ): Boolean {
        var errorMessage = ""
        var bool = true

        if (nomCognom.isEmpty()) {
            errorMessage += "Falta introduir el nom i cognoms\n"
            bool = false
        }

        if (dni.isEmpty()) {
            errorMessage += "Falta introduir el dni\n"
            bool = false
        } else if (!checkDni(dni)) {
            errorMessage += "Format DNI incorrecte.\n"
            bool = false
        }

        if (email.isEmpty()) {
            errorMessage += "Falta introduir el correu electronic.\n"
            bool = false
        }else if (!checkEmailFormat(email)){
            errorMessage += "Format correu electronic incorrecte.\n"
            bool = false
        }

        if (password.isEmpty()) {
            errorMessage += "Falta introduir la contrasenya.\n"
            bool = false
        }
        if (password2.isEmpty()) {
            errorMessage += "Falta introduir la contrasenya repetida.\n"
            bool = false
        }


        if (password.isNotEmpty() && password2.isNotEmpty()) {
            if (password != password2) {
                errorMessage += "Les contrasenyes no coincideixen.\n"
                bool = false
            }
        }

        if (!checkbox) {
            errorMessage += "Has d\'acpetar les condicions"
            bool = false
        }


        if (errorMessage != "") {
            showAlert(errorMessage)
        }

        return bool

    }

    private fun checkDni(dni: String): Boolean{
        val dniNum = dni.substring(0, dni.length -1)
        if (dni.isDigitsOnly()){
            println("DIGIT ONLY")
            return false
        }

        val dniLletra = dni.substring(dni.length -1).uppercase()
        val lletraDni = "TRWAGMYFPDXBNJZSQVHLCKE"

        return dniLletra == lletraDni[dniNum.toInt() % 23].toString()
    }

    private fun checkEmailFormat(email: String): Boolean{
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("¡¡¡Error!!!")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
