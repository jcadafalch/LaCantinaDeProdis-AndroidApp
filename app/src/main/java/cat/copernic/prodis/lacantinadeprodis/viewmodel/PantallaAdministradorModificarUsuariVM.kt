package cat.copernic.prodis.lacantinadeprodis.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.ui.administrador.PantallaAdministradorModificarUsuariDirections
import cat.copernic.prodis.lacantinadeprodis.ui.administrador.PantallaAdministradorPrincipalDirections
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PantallaAdministradorModificarUsuariVM : ViewModel() {
    private val db = Firebase.firestore
    private var auth = FirebaseAuth.getInstance()
    var spinnerSelectedBoolean = false
    //var failureListener = true
    //var successListener = false
    //var onCompleteListener = false
    //var onCompleteListener1 = false
    //var isSuccessful = false

    var arrUser = java.util.ArrayList<String>()
    var arrUserId = java.util.ArrayList<String>()
    var arrUsertype = java.util.ArrayList<String>()
    lateinit var usertype: MutableLiveData<String>
    lateinit var username: MutableLiveData<String>
    lateinit var usersurname: MutableLiveData<String>
    lateinit var dni: MutableLiveData<String>
    lateinit var email: MutableLiveData<String>
    lateinit var pswd: MutableLiveData<String>

    init {
        Log.i("ModificarUsuari-VM", "PantallaAdministradorModificarUsuariVM created!")

        usertype.value = ""
        username.value = ""
        usersurname.value = ""
        dni.value = ""
        email.value = ""
        pswd.value = ""


        arrUser.clear()
        arrUserId.clear()
        arrUsertype.clear()

        loadArrUser()
        loadArrUsertype()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ModificarUsuari-VM", "PantallaAdministradorModificarUsuariVM destroyed!")
    }

    fun spinnerSelected(position: Int) {
        db.collection("users").document(arrUserId[position]).get()
            .addOnSuccessListener { document ->
                val pasword = document.get("password").toString()
                pswd.value = pasword.replace("prodis", "")
                spinnerSelectedBoolean = true
                username.value = document.get("username").toString()
                usersurname.value = document.get("usersurname").toString()
                dni.value = document.get("dni").toString()
                email.value = document.get("email").toString()
                usertype.value = document.get("usertype").toString()
            }
    }

    private fun loadArrUser(){
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.id != "usertypes") {
                    val user =
                        document.get("username").toString() + " " + document.get("usersurname")
                            .toString()
                    arrUser.add(user)
                    arrUserId.add(document.id)
                }
            }
        }
    }

    private fun loadArrUsertype(){
        db.collection("users").document("usertypes").get().addOnSuccessListener { document ->
            val admin = document.get("admin").toString()
            val caixer = document.get("caixer").toString()
            val cambrer = document.get("cambrer").toString()
            val client = document.get("client").toString()
            val clientR = document.get("clientR").toString()
            val cuiner = document.get("cuiner").toString()

            arrUsertype.add(admin)
            arrUsertype.add(caixer)
            arrUsertype.add(cambrer)
            arrUsertype.add(client)
            arrUsertype.add(clientR)
            arrUsertype.add(cuiner)
        }
    }

    /* fun deleteUser(dni: String) {
         failureListener = true
        successListener = false
         onCompleteListener = false
         onCompleteListener1 = false
         isSuccessful = false
         isSuccessful = false
         db.collection("users").document(dni).get().addOnSuccessListener { result ->
             auth.signInWithEmailAndPassword(
                 result.get("email").toString(),
                 result.get("password").toString()
             ).addOnCompleteListener {
                 onCompleteListener = true
                 if (it.isSuccessful) {
                     isSuccessful = true
                     val currentUser = auth.currentUser
                     currentUser?.delete()
                         ?.addOnCompleteListener {
                             onCompleteListener1 = true
                             db.collection("users").document(dni).delete()
                                 .addOnSuccessListener {
                                     onCompleteListener = true
                                 }
                                 .addOnFailureListener {
                                     failureListener = false
                                 }
                         }
                 } else {
                     isSuccessful = false
                 }
             }
         }
     }*/

    /*fun spinnerUserTypeSelected(arrUserId: ArrayList<String>, position: Int){
            db.collection("users").document(arrUserId[position]).get()
                .addOnSuccessListener { document ->
                    val nom = binding.dtTxtPAdministradorModificarUsuariPersonName.text.toString()
                    val cognom = binding.dtTxtPAdministradorModificarUsuariPersonSurname.text.toString()
                    if (document.get("usertype") == "client" && position != 3) {
                        val builder =
                            androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
                        builder.setTitle("¡¡¡ATENCIÓ!!!")
                        builder.setMessage("Si vols que l'usuari: $nom $cognom deixi de ser client, " +
                                "l'has de tornar a afegir desde l'apartat \"Afegir nou usuari\".\n" +
                                "Vols anar a \"Afegir nou usuari\" per canviar el tipus d'usuari?")
                        builder.setPositiveButton("Si") { _, _ ->
                            view?.findNavController()?.navigate(PantallaAdministradorModificarUsuariDirections.actionPantallaAdministradorModificarUsuariToPantallaAdministradorNouUsuari(arrUserType))
                        }
                        builder.setNegativeButton("No") { _, _ ->
                            spinnerUserType.setSelection(3)
                        }
                        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
                        dialog.show()
                    } else if (document.get("usertype") == "client") {
                        binding.txtPAdministradorModificarUsuariEmail.visibility =
                            View.INVISIBLE
                        binding.dtTxtPAdministradorModificarUsuariEmail.visibility =
                            View.INVISIBLE
                        binding.dtTxtPAdministradorModificarUsuariPasswordL.visibility =
                            View.INVISIBLE
                        /*binding.dtTxtPAdministradorModificarUsuariEmail.text = ""
                        binding.dtTxtPAdministradorModificarUsuariPassword.setText("")
                        binding.dtTxtPAdministradorModificarUsuariEmail.isEnabled = true
                        binding.dtTxtPAdministradorModificarUsuariEmail.inputType*/
                    } else {
                        binding.txtPAdministradorModificarUsuariEmail.visibility = View.VISIBLE
                        binding.dtTxtPAdministradorModificarUsuariEmail.visibility =
                            View.VISIBLE
                        binding.dtTxtPAdministradorModificarUsuariPasswordL.visibility =
                            View.VISIBLE
                    }
                }

    }*/

}