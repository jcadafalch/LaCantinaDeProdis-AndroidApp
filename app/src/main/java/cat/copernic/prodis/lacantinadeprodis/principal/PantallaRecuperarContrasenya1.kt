package cat.copernic.prodis.lacantinadeprodis.principal

import android.app.AlertDialog
import android.app.Fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRecuperarContrasenya1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PantallaRecuperarContrasenya1 : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var usertype: String
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaRecuperarContrasenya1Binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_recuperar_contrasenya1, container, false
        )
        val args = PantallaRecuperarContrasenya1Args.fromBundle(requireArguments())
        usertype = args.usertype
       // val dni = binding.//binding.dtTxtPRecuperarContrasenya1DNI.text.toString().uppercase()
        val dni = binding.dtTxtPRecuperarContrasenya1DNI.text.toString().uppercase()
        binding.btnPRecuperarContrasenya1Continuar.setOnClickListener {
            println("SET ON CLICK LISTENER")
            println("DNI  = $dni")
            if (dni.isNotEmpty()){
                println("NOT EMPTY")
                getEmail(dni)
                /*if (email != ""){
                    auth.setLanguageCode("es")
                    resetpassd()
                }else{
                    println("GOOL - $email")
                }*/
            }else{
                Toast.makeText(this.con, "El camp DNI està buit", Toast.LENGTH_SHORT).show()
            }

            //view?.findNavController()?.navigate(PantallaRecuperarContrasenya1Directions.actionPantallaRecuperarContrasenya1ToPantallaRecuperarContrasenya2(usertype))
        }
        return binding.root
    }

    private fun getEmail(dni: String){
        var bool = false
        db.collection("users").get().addOnSuccessListener { result ->
        for (document in result){
            if (document.id == dni){
                bool = true
                println("DOCUMENT IDD " + document.id + " -  EMAIL: "  + document.get("email").toString())
                email = document.get("email").toString()
                println("EMAIL == $email")
                auth.setLanguageCode("es")
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    println("RESET PASSWORD EMAIL")
                    if (it.isSuccessful){
                        println("SUCCESSFUL")
                        Toast.makeText(this.context, "S\'ha enviat un correu a: $email, per restablir la contrasenya", Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(PantallaRecuperarContrasenya1Directions.actionPantallaRecuperarContrasenya1ToPantallaIniciSessioClientAdmin(usertype))
                    }else{
                        println("ERROR")
                        Toast.makeText(this.context, "No s\'ha pogut enviar un correu per restablir contrasenya", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                println("DOCUMENT ID  = " + document.id.toString())
            }
        }
        /*email = document.get("email").toString()
            do {
                println(email)
            }while (email == "")*/
        }
        if (!bool){
            showAlert("L\'usuari no està registrat")
        }
    }

    private fun resetpassd(){
        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this.context, "S\'ha enviat un correu a: $email, per restablir la contrasenya", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context, "No s\'ha pogut enviar un correu per restablir contrasenya", Toast.LENGTH_SHORT).show()
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