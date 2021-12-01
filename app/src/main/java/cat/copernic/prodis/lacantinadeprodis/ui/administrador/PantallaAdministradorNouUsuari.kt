package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorNouUsuariBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PantallaAdministradorNouUsuari : Fragment() {

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaAdministradorNouUsuariBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_nou_usuari, container, false
        )

        binding.btnPNouUsuariGurardar.setOnClickListener {
            if (
                datavalids(
                    binding.dtTxtPAdministradorNouUsuariPersonName.text.toString(),
                    binding.dtTxtPAdministradorNouUsuariPersonSurname.text.toString(),
                    binding.dtTxtPAdministradorNouUsuariDni.text.toString()
                )
            ) {
                newUser(
                    binding.dtTxtPAdministradorNouUsuariDni.text.toString(),
                    binding.dtTxtPAdministradorNouUsuariPersonName.text.toString(),
                    binding.dtTxtPAdministradorNouUsuariPersonSurname.text.toString()
                )
            }
        }

        return binding.root
    }

    private fun newUser(dni: String, username: String, usersurname: String) {
        db.collection("users").document(dni).get().addOnSuccessListener { document ->
            if (!document.exists()) {
                db.collection("users").document(dni).set(
                    hashMapOf("username" to username, "usersurname" to usersurname, "dni" to dni)
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        view?.findNavController()
                            ?.navigate(PantallaAdministradorNouUsuariDirections.actionPantallaAdministradorNouUsuariToPantallaAdministradorPrincipal())
                        Toast.makeText(
                            this.context,
                            "S\'ha afegit correctament l\'usuari $username $usersurname",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        showAlert("Error a l'hora de afegir el nou usuari")
                    }
                }
            } else {
                showAlert("Aquest usuari ja existeix")
            }
        }

    }

    private fun datavalids(
        nom: String,
        cognom: String,
        dni: String
    ): Boolean {
        var errorMessage = ""
        var bool = true

        if (nom.isEmpty()) {
            errorMessage += "Falta introduir el nom\n"
            bool = false
        }

        if (cognom.isEmpty()) {
            errorMessage += "Falta introduir els cognoms\n"
            bool = false
        }

        if (dni.isEmpty()) {
            errorMessage += "Falta introduir el dni\n"
            bool = false
        } else if (!checkDni(dni)) {
            errorMessage += "Format DNI incorrecte.\n"
            bool = false
        }

        if (errorMessage != "") {
            showAlert(errorMessage)
        }

        return bool

    }

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

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("ERROR")
        builder.setMessage(msg)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

}