package com.example.spotibae.Fragments

import android.os.Bundle
import android.util.Log
import com.example.spotibae.Fragments.MatchingFrag
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spotibae.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotibae.Models.ModelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spotibae.Adapter.Adapter
import com.example.spotibae.Fragments.MessagesFrag
import kotlin.Throws
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.example.spotibae.Fragments.QRFrag
import com.example.spotibae.Models.Setters.MatchingModel
import com.example.spotibae.Fragments.UserFrag
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 * Use the [MessagesFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessagesFrag : Fragment() {
    var mrecyclerView: RecyclerView? = null
    var layoutManager: LinearLayoutManager? = null
    var userList: MutableList<ModelClass>? = null
    var adapter: Adapter? = null
    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
    var storage = FirebaseStorage.getInstance()
    var storageRef: StorageReference? = null
    var profileRef: StorageReference? = null
    var matchList: MutableList<String> = ArrayList()
    var totalMatches: TextView? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    private fun initRecyclerView() {
        mrecyclerView = requireView().findViewById(R.id.recyclerView)
        mrecyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        layoutManager!!.orientation = RecyclerView.VERTICAL
        mrecyclerView?.setLayoutManager(LinearLayoutManager(requireView().context))
        mrecyclerView?.setAdapter(Adapter(userList!!))
    }

    @Throws(InterruptedException::class)
    private fun initData() {
        userList = ArrayList()
        matches
    }

    val matches: Unit
        get() {
            val user = mAuth.currentUser
            val uId = user!!.uid
            mDatabase.child(uId).get().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("firebase", "Error getting data", task.exception)
                } else {
                    Log.d("firebase User", task.result.value.toString())
                    val user = task.result.value as HashMap<String, Any>?
                    val matchesUsers = user!!["matches"] as HashMap<String, Any>?
                    if (matchesUsers == null) {
                    } else {
                        for ((firebaseUid) in matchesUsers) {
                            matchList.add(firebaseUid)
                        }
                        for (firebaseID in matchList) {
                            getMatchesInfo(firebaseID)
                        }
                    }
                    totalMatches = requireView().findViewById(R.id.totalMatches)
                    totalMatches?.setText(
                        requireView().resources.getString(
                            R.string.totalMatches,
                            matchList.size
                        )
                    )
                }
            }
        }

    fun getMatchesInfo(id: String?) {
        mDatabase.child(id!!).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
            } else {
                Log.d("firebase User", task.result.value.toString())
                val user = task.result.value as HashMap<String, Any>?
                userList!!.add(
                    ModelClass(
                        id,
                        user!!["email"].toString(),
                        user["firstName"].toString(),
                        "How ?",
                        "11:55 pm",
                        "_______________________________________"
                    )
                )
            }
            initRecyclerView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        try {
            initData()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MessagesFrag.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): MessagesFrag {
            val fragment = MessagesFrag()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}