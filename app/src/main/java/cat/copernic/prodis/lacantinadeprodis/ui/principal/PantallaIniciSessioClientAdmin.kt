package cat.copernic.prodis.lacantinadeprodis.ui.principal

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.ui.activities.AdministradorActivity
import cat.copernic.prodis.lacantinadeprodis.ui.activities.CaixerActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.ui.activities.ComandesActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioClientAdminBinding
import cat.copernic.prodis.lacantinadeprodis.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PantallaIniciSessioClientAdmin: Fragment() {

    private val db = Firebase.firestore
    private var auth = FirebaseAuth.getInstance()
    private lateinit var dni: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentPantallaIniciSessioClientAdminBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio_client_admin, container, false
        )

        val args = PantallaIniciSessioClientAdminArgs.fromBundle(requireArguments())
        val usertype = args.usertype

        if (usertype == "admin") {
            binding.txtPiniciarSessioClientRegistre.visibility = View.INVISIBLE
        }

        binding.btnPiniciarSessioClient.setOnClickListener {
            dni = binding.dtTxtPIniciarSessioClientDni.text.toString().uppercase()
            var passwd = binding.dtTxtPIniciarSessioClientPassword.text.toString()
            if (binding.dtTxtPIniciarSessioClientDni.text.isNotEmpty() &&
                !binding.dtTxtPIniciarSessioClientPassword.text.isNullOrEmpty()
            ) {
                passwd += "prodis"
                db.collection("users").document(dni).get().addOnSuccessListener { result ->
                    if (result.get("usertype").toString() == usertype) {
                        auth.signInWithEmailAndPassword(
                            result.get("email").toString(),
                            passwd,
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                startActivity(usertype)
                            } else {
                                showAlert("Error en inici de sessió")
                            }
                        }
                            .addOnFailureListener {
                                showAlert("L\'usuari no està registrat")
                            }
                    } else {
                        showAlert("Aquest usuari no és de tipus $usertype")
                    }
                }

            } else {
                Toast.makeText(this.context, "Falta omplir els camps", Toast.LENGTH_SHORT).show()
            }
        }


        binding.txtPiniciarSessioClientOblidatContrasenya.setOnClickListener {
            view?.findNavController()?.navigate(
                PantallaIniciSessioClientAdminDirections.actionPantallaIniciSessioClientAdminToPantallaRecuperarContrasenya1(
                    usertype
                )
            )
        }

        binding.txtPiniciarSessioClientRegistre.setOnClickListener {
            view?.findNavController()?.navigate(
                PantallaIniciSessioClientAdminDirections.actionPantallaIniciSessioClientAdminToPantallaRegistre(
                    usertype
                )
            )
        }

        return binding.root
    }

    private fun startActivity(usertype: String) {
        when (usertype) {
            "clientR" -> showCambrerClient(usertype, dni)
            "cambrer" -> showCambrerClient(usertype, dni)
            "caixer" -> showCaixer(dni)
            "cuiner" -> showCuiner(dni)
            "admin" -> showAdmin(dni)
        }
    }

    private fun showCambrerClient(username: String, dni: String) {
        val intent = Intent(this.context, ComandesActivity::class.java).apply {
            putExtra("usertype", username)
            putExtra("dni", dni)
        }
        //utils().idComanda = "N"
        startActivity(intent)
    }

    private fun showCaixer(dni: String) {
        val intent = Intent(this.context, CaixerActivity::class.java).apply {
            putExtra("dni", dni)
        }

        startActivity(intent)
    }

    private fun showCuiner(dni: String) {
        val intent = Intent(this.context, ComandesActivity::class.java).apply {
            putExtra("dni", dni)
        }
        startActivity(intent)
    }

    private fun showAdmin(dni: String) {
        val intent = Intent(this.context, AdministradorActivity::class.java).apply {
            putExtra("dni", dni)
        }
        startActivity(intent)
    }

    private fun showAlert(mesage: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("ERROR")
        builder.setMessage(mesage)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}