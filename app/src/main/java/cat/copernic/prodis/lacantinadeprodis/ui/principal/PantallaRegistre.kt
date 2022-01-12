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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRegistreBinding
import cat.copernic.prodis.lacantinadeprodis.utils.utils
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

        //botó que crida a la funncio datavalids per comprovar el format de les dades i en cas de ser correcte crida a la funcioo make register per fer el registre
        bdng.btnPregistre.setOnClickListener { view: View ->
            println(bdng.dtTxtPRegistrePassword.text.toString())
            println(bdng.dtTxtPRegistreRepeteixPassword.text.toString())
            if (datavalids(
                    bdng.dtTxtPRegistrePersonName.text.toString(),
                    bdng.dtTxtPRegistrePersonSurname.text.toString(),
                    bdng.dtTxtPRegistreDni.text.toString(),
                    bdng.dtTxtPRegistreEmail.text.toString(),
                    bdng.dtTxtPRegistrePassword.text.toString(),
                    bdng.dtTxtPRegistreRepeteixPassword.text.toString(),
                    bdng.checkBox.isChecked
                )
            ) {
                makeregister(
                    bdng.dtTxtPRegistrePersonName.text.toString(),
                    bdng.dtTxtPRegistrePersonSurname.text.toString(),
                    bdng.dtTxtPRegistreDni.text.toString(),
                    bdng.dtTxtPRegistreEmail.text.toString(),
                    bdng.dtTxtPRegistrePassword.text.toString(),
                    usertype
                )
                view.findNavController().navigate(
                    PantallaRegistreDirections.actionPantallaRegistreToPantallaIniciSessioClientAdmin(
                        usertype
                    )
                )
            }

        }
        // botó per obrir el fragment d'iniciar sessió
        bdng.textPregistreIniciaSessio.setOnClickListener { view: View ->
            view.findNavController().navigate(
                PantallaRegistreDirections.actionPantallaRegistreToPantallaIniciSessioClientAdmin(
                    usertype
                )
            )
        }
        return bdng.root
    }

    //Funció que crida a la BD i realitza el registre complet de l'usuari
    private fun makeregister(
        nom: String,
        cognom: String,
        dni: String,
        email: String,
        password: String,
        usertype: String
    ) {
        val passwd = password + "prodis"
        println(passwd)
        //Registem l'usuari al Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // si es realitza satisfactoriament es crea el document a la collection Users
                    db.collection("users").document(dni).set(
                        hashMapOf(
                            "username" to nom,
                            "usersurname" to cognom,
                            "dni" to dni,
                            "email" to email,
                            "password" to passwd,
                            "usertype" to usertype
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this.context,
                                getString(R.string.t_has_registrat_correctament),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            utils().showAlert(
                                getString(R.string.error),
                                getString(R.string.error_en_guardar_dades),
                                this.context
                            )
                        }
                    }

                } else {
                    utils().showAlert(
                        getString(R.string.error),
                        "Error a l\'hora de fer l\'autenticació",
                        this.context
                    )
                }
            }
    }

    // Funció que comprova que els camps no estiguin buits i que les contrasenyes coincideixin
    private fun datavalids(
        nom: String,
        cognom: String,
        dni: String,
        email: String,
        password: String,
        password2: String,
        checkbox: Boolean
    ): Boolean {
        var errorMessage = ""
        var bool = true

        if (nom.isEmpty()) {
            errorMessage += getString(R.string.falta_introduir_el_nom)
            bool = false
        }

        if (cognom.isEmpty()) {
            errorMessage += getString(R.string.falta_introduir_els_cognoms)
            bool = false
        }

        if (dni.isEmpty()) {
            errorMessage += getString(R.string.falta_introduir_el_dni)
            bool = false
        } else if (!checkDni(dni)) {
            errorMessage += getString(R.string.format_dni_incorrecte)
            bool = false
        }

        if (email.isEmpty()) {
            errorMessage += getString(R.string.falta_introduir_email)
            bool = false
        } else if (!checkEmailFormat(email)) {
            errorMessage += getString(R.string.format_email_incorrecte)
            bool = false
        }

        if (password.isEmpty()) {
            errorMessage += getString(R.string.falta_introduir_la_passwd)
            bool = false
        }
        if (password2.isEmpty()) {
            errorMessage += getString(R.string.falta_introduir_la_contrasenya_repetida)
            bool = false
        }


        if (password.isNotEmpty() && password2.isNotEmpty()) {
            if (password != password2) {
                errorMessage += getString(R.string.contrasenyes_no_coincideixen)
                bool = false
            }
        }

        if (!checkbox) {
            errorMessage += getString(R.string.acceptar_condicions)
            bool = false
        }


        if (errorMessage != "") utils().showAlert(
            getString(R.string.error),
            errorMessage,
            this.context
        )

        return bool

    }

    //Funció que comprova que el format del dni sigui correcte (evita que l'usuari s'inventi el dni)
    private fun checkDni(dni: String): Boolean {
        val dniNum = dni.substring(0, dni.length - 1)
        if (dni.isDigitsOnly()) {
            println("DIGIT ONLY")
            return false
        }

        val dniLletra = dni.substring(dni.length - 1).uppercase()
        val lletraDni = "TRWAGMYFPDXBNJZSQVHLCKE"

        return dniLletra == lletraDni[dniNum.toInt() % 23].toString()
    }

    //Funció que comprova el format del correu electronic
    private fun checkEmailFormat(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}
