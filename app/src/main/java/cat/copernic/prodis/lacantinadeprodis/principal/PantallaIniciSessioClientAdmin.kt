package cat.copernic.prodis.lacantinadeprodis.principal

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
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
import cat.copernic.prodis.lacantinadeprodis.administrador.PantallaAdministradorPrincipal
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

        /*binding.btnPiniciarSessioClient.setOnClickListener {
            println("set on clic listener")
            if (binding.dtTxtPIniciarSessioClientDni.text.isNotEmpty() &&
                binding.dtTxtPIniciarSessioClientPassword.text.isNotEmpty()){
                println("not empty")
                getEmail(binding.dtTxtPIniciarSessioClientDni.text.toString())
                //val email = Email

                val password = binding.dtTxtPIniciarSessioClientPassword.text.toString() + "prodis"
                println("PASWORD GETT $password")

                val email = Email
                println("EMAIL GETT $email")

                if (email != "null"){
                    println("Email not NULL $email")
                    //if (checkUserType(binding.dtTxtPIniciarSessioClientDni.text.toString(), usertype)){
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                            if (it.isSuccessful){
                                println("IS SUCCESSFUL")
                                Toast.makeText(context, "Has iniciat sessió", Toast.LENGTH_SHORT).show()
                                when (usertype){
                                    "client" -> showCambrerClient(usertype)
                                    "cambrer" -> showCambrerClient(usertype)
                                    "caixer" -> showCaixer()
                                    "cuiner" -> showCuiner()
                                    "admin" -> showAdmin()
                                    else -> {
                                        Toast.makeText(context, "Error en l'inici de sessió", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                showAlert("Inici de sessió incorrecte")
                            }


                        }
                    /*}else{
                        showAlert("Aquest usuari no és $usertype")
                    }*/
                }
            }else{
                // dt DNI i/o paswd son empty

                showAlert("DNI i/o contrasenya són buits")
            }
        }*/

        binding.btnPiniciarSessioClient.setOnClickListener {
            val dni = binding.dtTxtPIniciarSessioClientDni.text.toString().uppercase()
            var passwd = binding.dtTxtPIniciarSessioClientPassword.text.toString()
            if (binding.dtTxtPIniciarSessioClientDni.text.isNotEmpty() &&
                binding.dtTxtPIniciarSessioClientPassword.text.isNotEmpty()
            ) {
                //TODO afegir metodes de comprovació de dni valid

                passwd += "prodis"
                db.collection("users").get().addOnSuccessListener { result ->
                    // Recorrer t0do el documento buscando si coincide con algún DNI
                    for (document in result) {
                        if(document.id == dni){
                            auth.signInWithEmailAndPassword(
                                document.get("email").toString(),
                                passwd,).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        startActivity(usertype)
                                    }else{
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

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("ERROR")
        builder.setMessage(msg)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getEmail(dni: String) {
        db.collection("users").document(dni).get().addOnSuccessListener { document ->
            if (document != null) {
                Email = document.get("email").toString()
                do {
                    println(Email)
                } while (Email == "")

                println("END DO WHILE LOOP ${Email}")
            } else {
                Toast.makeText(this.context, "L\'usuari no existeix", Toast.LENGTH_SHORT).show()
                Email = "null"
            }
        }
        Email

    }

/* private fun checkUserType(dni: String, usertype: String): Boolean{
     var user: String
     var bool = false
     var check = false
     db.collection("users").document(dni).get().addOnSuccessListener { document ->
         user = document.get("usertype").toString()

         do {
             println("USER $user")
         }while (user == "")
         println("USERTYPE $user")

         Handler().postDelayed(
             {
                 bool = user == usertype
                 check = true
             },
             10000
         )
     }
     /*do {
         println("Bool WHILE $bool")
     }while (!check)*/
     if (check){
         println(bool)
         return bool
     }
 }*/
}