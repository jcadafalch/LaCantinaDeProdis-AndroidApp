package cat.copernic.prodis.lacantinadeprodis.principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRegistreBinding
/*import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore*/

class PantallaRegistre : Fragment() {
    //private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bdng: FragmentPantallaRegistreBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_registre, container, false
        )
        val args = PantallaRegistreArgs.fromBundle(requireArguments())
        val usertype = args.usertype



        bdng.btnPregistre.setOnClickListener { view: View ->
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
                    bdng.dtTxtPRegistrePersonName.toString(),
                    bdng.dtTxtPRegistreDni.toString(),
                    bdng.dtTxtPRegistreEmail.toString(),
                    bdng.dtTxtPRegistrePassword.toString(),
                    usertype
                )
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
        var bool: Boolean
        /*FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                bool = true

            }else{
                showAlert("Error a l\'hora de fer el registre")
            }
        }

        db.collection("users").document(dni).set(
            hashMapOf("username" to nomCognom,
            "dni" to dni)
        )

        if (bool){
            Toast.makeText(this.context, "T\'has registrat correctament", Toast.LENGTH_SHORT).show()
        }*/
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
        } /*else if (!dniformat(dni)) {
            errorMessage += "Format DNI incorrecte.\n"
            bool = false
        }*/

        if (email.isEmpty()) {
            errorMessage += "Falta introduir el correu electronic.\n"
            bool = false
        } else if (!email.contains("@") /*&& email.substring(email.length - 4) != "."*/) {
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

        if (!checkbox){
            errorMessage += "Has d\'acpetar les condicions"
            bool = false
        }


        if (errorMessage != ""){
            showAlert(errorMessage)
        }

        return bool

    }

    private fun dniformat(dni: String): Boolean {
        var letramayuscula = ""


        if (dni.length != 9 || Character.isLetter(dni[8]) == false) {
            return false
        }

        letramayuscula = dni.substring(8).uppercase()
        return onlyNumbers(dni) && letradni(dni) == letramayuscula
    }

    private fun onlyNumbers(dni: String): Boolean {
        var i = 0
        var j = 0
        var numero = ""
        var DNI = ""
        val unoNueve = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

        while (i < dni.length - 1) {
            numero = dni.substring(i, i + 1)

            while (j < unoNueve.size) {
                if (numero == unoNueve[j]) {
                    DNI += unoNueve[j]
                }
                j++
            }
            i++
        }
        return if(DNI.length != 8) {
            Toast.makeText(this.context, "Els numeros del DNI no coincideixen amb la lletra", Toast.LENGTH_SHORT).show()
            false;
        } else {
            true;
        }
    }

    private fun letradni(dni: String): String? {
        val miDNI: Int = dni.substring(0, 8).toInt()
        var resto = 0
        val asignacionLetra = arrayOf(
            "T",
            "R",
            "W",
            "A",
            "G",
            "M",
            "Y",
            "F",
            "P",
            "D",
            "X",
            "B",
            "N",
            "J",
            "Z",
            "S",
            "Q",
            "V",
            "H",
            "L",
            "C",
            "K",
            "E"
        )
        resto = miDNI % 23

        return asignacionLetra[resto]
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