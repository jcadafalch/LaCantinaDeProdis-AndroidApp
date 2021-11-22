package cat.copernic.prodis.lacantinadeprodis.administrador

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
            if (binding.dtTxtPAdministradorNouUsuariPersonName.text.isNotEmpty() &&
                binding.dtTxtPAdministradorNouUsuariPersonSurname.text.isNotEmpty()
            ) {
                newUser(
                    binding.dtTxtPAdministradorNouUsuariPersonName.text.toString(),
                    binding.dtTxtPAdministradorNouUsuariPersonSurname.text.toString()
                )
            } else {
                if (binding.dtTxtPAdministradorNouUsuariPersonName.text.isEmpty() &&
                    binding.dtTxtPAdministradorNouUsuariPersonSurname.text.isEmpty()
                ){
                    showAlert("ERROR", "Els camps nom d\'usuari i cognoms estan buits")
                }
                if (binding.dtTxtPAdministradorNouUsuariPersonName.text.isEmpty()){
                    showAlert("ERROR", "El camp nom d\'usuari està buit")
                }else{
                    showAlert("ERROR", "El camp cognom d\'usuari està buit")
                }

            }
        }

        return binding.root
    }

    private fun showAlert(ttl: String, msg: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(ttl)
        builder.setMessage(msg)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun newUser(username: String, usersurname: String) {
        val userName = username.replace(" ", "")
        val userSurname = usersurname.replace(" ", "")
        val documentid = userName+userSurname
        db.collection("users").document(documentid).get().addOnSuccessListener { document->
            if (!document.exists()){
                db.collection("users").document(documentid).set(
                    hashMapOf("username" to username, "usersurname" to usersurname)
                ).addOnCompleteListener {
                    if (it.isSuccessful){
                        view?.findNavController()?.navigate(PantallaAdministradorNouUsuariDirections.actionPantallaAdministradorNouUsuariToPantallaAdministradorPrincipal())
                        Toast.makeText(this.context, "S\'ha afegit correctament l\'usuari $username $usersurname"
                            , Toast.LENGTH_SHORT).show()
                    }else{
                        showAlert(
                            "ERROR",
                            "Error a l'hora de afegir el nou usuari"
                        )
                    }
                }
            }else{
                showAlert("ERROR", "Aquest usuari ja existeix")
            }
        }

    }

}