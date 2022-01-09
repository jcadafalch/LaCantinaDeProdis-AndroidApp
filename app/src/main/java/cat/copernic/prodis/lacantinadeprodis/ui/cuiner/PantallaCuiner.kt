package cat.copernic.prodis.lacantinadeprodis.ui.cuiner

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.cuiner_adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaCuinerBinding
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_cuiner
import com.google.firebase.firestore.*

class PantallaCuiner : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cuinerAdapter: cuiner_adapter
    private lateinit var comandesList: ArrayList<dtclss_cuiner>
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SwitchIntDef", "UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaCuinerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_cuiner, container, false
        )

        recyclerView = binding.rcyclrVwCuiner

        when(resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT -> {
                recyclerView.layoutManager = LinearLayoutManager(this.context)
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                recyclerView.layoutManager = LinearLayoutManager(
                    this.context,
                    RecyclerView.HORIZONTAL,
                    false
                )
            }
            Configuration.ORIENTATION_UNDEFINED -> {
                recyclerView.layoutManager = LinearLayoutManager(
                    this.context,
                    RecyclerView.HORIZONTAL,
                    false
                )
            }
        }

        recyclerView.setHasFixedSize(true)

        comandesList = arrayListOf()

        cuinerAdapter = this.context?.let { cuiner_adapter(comandesList, it) }!!

        //

        eventChangeListener()

        return binding.root
    }

    private fun eventChangeListener() {
        db.collection("comandes").addSnapshotListener(object : EventListener<QuerySnapshot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                //comandesList.clear()
                db.collection("comandes").get().addOnSuccessListener { result ->
                   comandesList.clear()
                    for (document in result) {
                        if(document.get("visible").toString() == "true"){
                            comandesList.add(document.toObject(dtclss_cuiner::class.java))

                        }
                    }
                    recyclerView.adapter = cuinerAdapter
                    cuinerAdapter.notifyDataSetChanged()
                }

            }
        })
    }
}