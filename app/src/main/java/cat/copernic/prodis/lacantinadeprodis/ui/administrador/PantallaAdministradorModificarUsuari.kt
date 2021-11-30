package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorModificarUsuariBinding
import cat.copernic.prodis.lacantinadeprodis.ui.principal.PantallaRecuperarContrasenya1Args
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class PantallaAdministradorModificarUsuari : Fragment(), AdapterView.OnItemSelectedListener {

    private val db = Firebase.firestore
    private var arrUser = ArrayList<String>()
    lateinit var usuari: String
    private lateinit var spinner: Spinner
    private var bool = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPantallaAdministradorModificarUsuariBinding>(
            inflater,
            R.layout.fragment_pantalla_administrador_modificar_usuari, container, false
        )

        val args = PantallaAdministradorModificarUsuariArgs.fromBundle(requireArguments())
        arrUser = args.userArr as ArrayList<String>

       //getUser()
        spinner = binding.spinUsuariModificar

       val context = this.requireContext()
        /*do{
            println("Bool == $bool")
        }while (!bool)*/
        //arrUser.size
       // R.array.llistat_usuaris
        ArrayAdapter.createFromResource(
            context,
            R.array.llistat_usuaris,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        println("/////////////////////////////")
        println(arrUser)
        println("??????????????????????????????")

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        usuari = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        showAlert("Has de seleccionar un tipus d\'usuari")
    }


    /*private fun getUser(){
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                val user = document.get("username").toString() + " " + document.get("usersurname").toString()
                println("USERNAME ==== "+document.get("username").toString())
                println("USERSURNAME ===== " + document.get("usersurname").toString() )
                println("----------------------------------------------------------")
                arrUser.add(user)
            }
            println("*************************")
            println(arrUser)
            println("------------------------------")
            bool = true

        }

    }*/


    private fun showAlert(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        builder.setTitle("¡¡¡Error!!!")
        builder.setMessage(message)
        builder.setPositiveButton("Acceptar", null)
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
    }


}
/*ArrayAdapter.createFromResource(
            context,
            arrUser,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }*/