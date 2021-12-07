package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorModificarUsuariBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class PantallaAdministradorModificarUsuari : Fragment(), AdapterView.OnItemSelectedListener {

    private val db = Firebase.firestore
    private var auth = FirebaseAuth.getInstance()
    private var arrUser = ArrayList<String>()
    private var arrUserId = ArrayList<String>()
    private var arrUserType = ArrayList<String>()
    private lateinit var spinner: Spinner
    private lateinit var spinnerUserType: Spinner
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var adapterUType: ArrayAdapter<*>
    private lateinit var binding: FragmentPantallaAdministradorModificarUsuariBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pantalla_administrador_modificar_usuari, container, false
        )

        val args = PantallaAdministradorModificarUsuariArgs.fromBundle(requireArguments())
        arrUser = args.userArr as ArrayList<String>
        arrUserId = args.userArrId as ArrayList<String>
        arrUserType = args.usertypeArr as ArrayList<String>

        //getUser()
        spinner = binding.spinUsuariModificar

        val context = this.requireContext()

        adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, arrUser
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        spinnerUserType = binding.spinTipusUsuari
        adapterUType = ArrayAdapter(
            context, android.R.layout.simple_spinner_item, arrUserType
        )
        adapterUType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserType.adapter = adapterUType


        binding.btnPAdministradorModificarUsuariGuardar.setOnClickListener {
            var usertype = ""
            when (spinnerUserType.selectedItem.toString()) {
                "Administrador" -> usertype = "admin"
                "Caixer" -> usertype = "caixer"
                "Cambrer" -> usertype = "cambrer"
                "Client" -> usertype = "client"
                "Client Remot" -> usertype = "clientR"
                "Cuiner" -> usertype = "cuiner"
            }

            saveUser(usertype)
        }

        binding.btnPAdministradorModificarUsuariElimiarUsuari.setOnClickListener {
            val user = binding.dtTxtPAdministradorModificarUsuariPersonName.text.toString() +
                    " " +
                    binding.dtTxtPAdministradorModificarUsuariPersonSurname.text.toString()
            val dni = binding.dtTxtPAdministradorModificarUsuariDtDni.text.toString()
            val builder = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
            builder.setTitle("¡¡¡AVIS!!!")
            builder.setMessage("Estas segur que vols eliminar a l'usuari: $user")
            builder.setPositiveButton("Acceptar") { _, _ ->
                deleteUser(dni)
            }
            builder.setNegativeButton("Cancelar", null)
            val dialog: androidx.appcompat.app.AlertDialog = builder.create()
            dialog.show()
        }


        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("ITEM position = $position")

        println("USER ID = " + arrUserId[position])
        db.collection("users").document(arrUserId[position]).get()
            .addOnSuccessListener { document ->
                val pasword = document.get("password").toString()
                println("PASSWORD  = $pasword")
                val pswd = pasword.replace("prodis", "")
                println("PSWD = $pswd")
                binding.dtTxtPAdministradorModificarUsuariPersonName.setText(
                    document.get("username").toString()
                )

                binding.dtTxtPAdministradorModificarUsuariPersonSurname.setText(
                    document.get("usersurname").toString()
                )

                binding.dtTxtPAdministradorModificarUsuariDtDni.setText(
                    document.get("dni").toString()
                )

                if (document.get("email").toString() != "null") {
                    binding.txtPAdministradorModificarUsuariEmail.visibility = View.VISIBLE
                    binding.dtTxtPAdministradorModificarUsuariEmail.visibility = View.VISIBLE
                    binding.dtTxtPAdministradorModificarUsuariEmail.setText(
                        document.get("email").toString()
                    )
                } else {
                    binding.txtPAdministradorModificarUsuariEmail.visibility = View.INVISIBLE
                    binding.dtTxtPAdministradorModificarUsuariEmail.visibility = View.INVISIBLE

                }

                if (pswd == "null") {
                    binding.dtTxtPAdministradorModificarUsuariPasswordL.visibility =
                        View.INVISIBLE

                } else {
                    binding.dtTxtPAdministradorModificarUsuariPasswordL.visibility =
                        View.VISIBLE
                    binding.dtTxtPAdministradorModificarUsuariPassword.setText(pswd)
                }

                when (document.get("usertype").toString()) {
                    "admin" -> spinnerUserType.setSelection(0)
                    "caixer" -> spinnerUserType.setSelection(1)
                    "cambrer" -> spinnerUserType.setSelection(2)
                    "client" -> spinnerUserType.setSelection(3)
                    "clientR" -> spinnerUserType.setSelection(4)
                    "cuiner" -> spinnerUserType.setSelection(5)
                    else -> {
                        Toast.makeText(
                            this.context,
                            "ERROR EN EL TIPUS DE USUARI",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        showAlert("Has de seleccionar un tipus d\'usuari")
    }


    private fun showAlert(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        builder.setTitle("¡¡¡Error!!!")
        builder.setMessage(message)
        builder.setPositiveButton("Acceptar", null)
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
    }

    private fun deleteUser(dni: String) {
        db.collection("users").document(dni).get().addOnSuccessListener { result ->
            auth.signInWithEmailAndPassword(
                result.get("email").toString(),
                result.get("password").toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.delete()
                        ?.addOnCompleteListener {
                            db.collection("users").document(dni).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this.context,
                                        "Usuari eliminat correctament",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    view?.findNavController()
                                        ?.navigate(PantallaAdministradorModificarUsuariDirections.actionPantallaAdministradorModificarUsuariToPantallaAdministradorPrincipal())
                                }
                                .addOnFailureListener {
                                    showAlert("L'usuari no s'ha pogut eliminar")
                                }
                        }

                } else {
                    showAlert("Hi ha hagut un error en intentar eliminar a l'usuari")
                }


            }
        }

    }

    private fun saveUser(usertype: String) {
        println(spinnerUserType.selectedItem.toString())
        val dni = binding.dtTxtPAdministradorModificarUsuariDtDni.text.toString()
        val username = binding.dtTxtPAdministradorModificarUsuariPersonName.text.toString()
        val usersurname = binding.dtTxtPAdministradorModificarUsuariPersonSurname.text.toString()
        val email = binding.dtTxtPAdministradorModificarUsuariEmail.text.toString()
        val passwd = binding.dtTxtPAdministradorModificarUsuariPassword.text.toString()
        db.collection("users").document(dni).get()
            .addOnSuccessListener { document ->
                //Si ha canviat i es de tipus Client
                if (usertype == "client") {
                    //El nou tipus d'usuari és client i el dni no ha canviat
                    if (document.get("dni").toString() ==
                        binding.dtTxtPAdministradorModificarUsuariDtDni.text.toString()
                    ) {
                        db.collection("users").document(dni).update(
                            hashMapOf(
                                "username" to username,
                                "usersurname" to usersurname,
                                "usertype" to usertype
                            ) as Map<String, Any>
                        )
                    } else {
                        //El nou tipus d'usuari és client i el dni si ha canviat
                        var b = false
                        var Bdni = ""

                        //comprovem si el dni ja està registrat
                        db.collection("users").get().addOnSuccessListener { result ->
                            for (document in result) {

                                if (document.id == dni) {
                                    b = true
                                }

                                if (document.get("username").toString() == username &&
                                    document.get("usersurname").toString() == usersurname
                                ) {
                                    Bdni = document.get("dni").toString()

                                }
                            }
                        }

                        //Si esta registrat
                        if (b) {
                            val builder =
                                androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                            builder.setTitle("¡¡¡AVIS!!!")
                            builder.setMessage("El dni: $dni ja està registrat. \n El vols sobreecriure")
                            builder.setPositiveButton("Si") { _, _ ->
                                if (deleteUserI(Bdni)) {
                                    db.collection("users").document(dni).set(
                                        hashMapOf(
                                            "dni" to dni,
                                            "username" to username,
                                            "usersurname" to usersurname,
                                            "usertype" to usertype
                                        )
                                    )
                                }
                            }

                            builder.setNegativeButton("No") { _, _ ->
                                binding.dtTxtPAdministradorModificarUsuariDtDni.setText(Bdni)
                            }

                            val dialog: androidx.appcompat.app.AlertDialog =
                                builder.create()
                            dialog.show()
                        } else {
                            //Si el dni no està regitrat
                            if (deleteUserI(Bdni)) {
                                db.collection("users").document(dni).set(
                                    hashMapOf(
                                        "dni" to dni,
                                        "username" to username,
                                        "usersurname" to usersurname,
                                        "usertype" to usertype
                                    )
                                )
                            }
                        }
                    }
                } else {
                    //El nou tipus d'usuari ha canbiat, NO és client i el dni no ha canviat
                    if (document.get("dni").toString() == dni
                    ) {

                        if (document.get("email").toString() != email) {
                            changeEmail(dni, email)
                        }
                        val password = document.get("password")
                        if (password != passwd) {
                            changePassword(dni, passwd)
                        }
                        db.collection("users").document(dni).update(
                            hashMapOf(
                                "email" to email,
                                "password" to passwd,
                                "username" to username,
                                "usersurname" to usersurname,
                                "usertype" to usertype
                            ) as Map<String, Any>
                        )
                    } else {
                        ////El nou tipus d'usuari ha canbiat, NO és client i el dni si ha canviat
                        var b = false
                        var Bdni = ""

                        //comprovem si el dni ja està registrat
                        db.collection("users").get().addOnSuccessListener { result ->
                            for (document in result) {

                                if (document.id == dni) {
                                    b = true
                                }

                                if (document.get("username").toString() == username &&
                                    document.get("usersurname").toString() == usersurname
                                ) {
                                    Bdni = document.get("dni").toString()

                                }
                            }
                        }
                        if (b) {
                            val builder =
                                androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                            builder.setTitle("¡¡¡AVIS!!!")
                            builder.setMessage("El dni: $dni ja està registrat. \n El vols sobreecriure")
                            builder.setPositiveButton("Si") { _, _ ->
                                if (document.get("email").toString() != email) {
                                    changeEmail(dni, email)
                                }
                                val password = document.get("password")
                                if (password != passwd) {
                                    changePassword(dni, passwd)
                                }
                                if (deleteUserI(Bdni)) {
                                    db.collection("users").document(dni).set(
                                        hashMapOf(
                                            "dni" to dni,
                                            "username" to username,
                                            "usersurname" to usersurname,
                                            "email" to email,
                                            "password" to passwd,
                                            "usertype" to usertype
                                        )
                                    )
                                }
                            }

                            builder.setNegativeButton("No") { _, _ ->
                                binding.dtTxtPAdministradorModificarUsuariDtDni.setText(Bdni)
                            }

                            val dialog: androidx.appcompat.app.AlertDialog =
                                builder.create()
                            dialog.show()
                        } else {
                            //Si el dni no està regitrat
                            if (deleteUserI(Bdni)) {
                                db.collection("users").document(dni).set(
                                    hashMapOf(
                                        "dni" to dni,
                                        "username" to username,
                                        "usersurname" to usersurname,
                                        "email" to email,
                                        "password" to passwd,
                                        "usertype" to usertype
                                    )
                                )
                            }
                        }

                    }
                }

            }
    }

    private fun changeEmail(dni: String, email: String) {
        db.collection("users").document(dni).get().addOnSuccessListener { result ->
            auth.signInWithEmailAndPassword(
                result.get("email").toString(),
                result.get("password").toString(),
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.updateEmail(email)?.addOnSuccessListener {
                        auth.signOut()
                        Toast.makeText(this.context, "S'ha canviat el email", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun changePassword(dni: String, psswd: String) {
        db.collection("users").document(dni).get().addOnSuccessListener { result ->
            auth.signInWithEmailAndPassword(
                result.get("email").toString(),
                result.get("password").toString(),
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.updatePassword(psswd)?.addOnSuccessListener {
                        auth.signOut()
                        Toast.makeText(
                            this.context,
                            "S'ha canbiat la contrasenya",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val pas = hashMapOf("password" to psswd)
                    db.collection("users").document(dni).update(
                        pas as Map<String, Any>
                    )
                } else {
                    showAlert("Error en inici de sessió")
                }
            }
        }
            .addOnFailureListener {
                showAlert("L\'usuari no està registrat")
            }

    }

    private fun deleteUserI(dni: String): Boolean {
        var correct = false
        db.collection("users").document(dni).delete()
            .addOnSuccessListener {
                Toast.makeText(
                    this.context,
                    "S\'ha fet el canvi de dni",
                    Toast.LENGTH_SHORT
                ).show()
                correct = true
            }
            .addOnFailureListener {
                showAlert("No s'ha pogut fer el canvi de dni")
                correct = false

            }

        return correct
    }
}
