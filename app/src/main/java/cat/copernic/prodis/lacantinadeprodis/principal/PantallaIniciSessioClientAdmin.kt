package cat.copernic.prodis.lacantinadeprodis.principal

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
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.activities.AdministradorActivity
import cat.copernic.prodis.lacantinadeprodis.activities.CambrerActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioClientAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PantallaIniciSessioClientAdmin : Fragment() {

    private val db = Firebase.firestore
    private var Email = ""
    private var auth = FirebaseAuth.getInstance()
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

        binding.btnPiniciarSessioClient.setOnClickListener {
            val dni = binding.dtTxtPIniciarSessioClientDni.text.toString().uppercase()
            var passwd = binding.dtTxtPIniciarSessioClientPassword.text.toString()
            if (binding.dtTxtPIniciarSessioClientDni.text.isNotEmpty() &&
                binding.dtTxtPIniciarSessioClientPassword.text.isNotEmpty()
            ) {
                passwd += "prodis"
                db.collection("users").get().addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == dni) {
                            auth.signInWithEmailAndPassword(
                                document.get("email").toString(),
                                passwd,
                            ).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    startActivity(usertype)
                                } else {
                                    showAlert("Error en inici de sessió")
                                }
                            }
                        }
                    }
                }

            } else {
                Toast.makeText(this.context, "L\'usuari no existeix", Toast.LENGTH_SHORT).show()
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

        binding.passwordEyePIniciSessio.setOnClickListener {

        }
        return binding.root
    }

    private fun startActivity(usertype: String) {
        when (usertype) {
            "client" -> showCambrerClient(usertype)
            "cambrer" -> showCambrerClient(usertype)
            "caixer" -> showCaixer()
            "cuiner" -> showCuiner()
            "admin" -> showAdmin()
        }
    }

    private fun showCambrerClient(username: String) {
        val intent = Intent(this.context, CambrerActivity::class.java).apply {
            putExtra("usertype", username)
        }
        startActivity(intent)
    }

    private fun showCaixer() {
        /*val intent = Intent(this.context, CaixerActivity::class.java).apply { }

        startActivity(intent)*/
    }

    private fun showCuiner() {
        /*val intent =Intent(this.context, CuinerActivity::class.java).apply { }
        startActivity(intent)*/
    }

    private fun showAdmin() {
        val intent = Intent(this.context, AdministradorActivity::class.java).apply { }
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