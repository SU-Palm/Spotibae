package com.example.spotibae.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spotibae.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotibae.Models.ModelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spotibae.Adapter.Adapter
import kotlin.Throws
import java.util.ArrayList
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 * Use the [MessagesFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessagesFrag : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private lateinit var mrecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var userList: MutableList<ModelClass>
    private lateinit var adapter: Adapter
    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
    private var storage = FirebaseStorage.getInstance()
    private var matchList: MutableList<String> = ArrayList()
    private lateinit var totalMatches: TextView
    private lateinit var view1: View

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
        mrecyclerView = view1.findViewById(R.id.recyclerView)
        mrecyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mrecyclerView.layoutManager = LinearLayoutManager(view1.context)
        mrecyclerView.adapter = Adapter(userList)
    }

    @Throws(InterruptedException::class)
    private fun initData() {
        userList = ArrayList()
        getMatches()
    }

    private fun getMatches() {
        val user = mAuth.currentUser
        val uId = user!!.uid
        mDatabase.child(uId).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
            } else {
                Log.d("firebase User", task.result.value.toString())
                val user = task.result.value as HashMap<*, *>
                val matchesUsers = user["matches"] as HashMap<*, *>
                if (matchesUsers == null) {
                } else {
                    for ((firebaseUid) in matchesUsers) {
                        matchList.add(firebaseUid as String)
                    }
                    for (firebaseID in matchList) {
                        getMatchesInfo(firebaseID)
                    }
                }
                totalMatches = view1.findViewById(R.id.totalMatches)
                totalMatches.setText(
                    view1.resources.getString(
                        R.string.totalMatches,
                        matchList.size
                    )
                )
            }
        }
    }

    private fun getMatchesInfo(id: String) {
        mDatabase.child(id).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
            } else {
                Log.d("firebase User", task.result.value.toString())
                Log.d("Here", task.result.value.toString())
                if(task.result.value != null) {
                    val user = task.result.value as HashMap<*, *>
                    userList.add(
                        ModelClass(
                            id,
                            user["email"].toString(),
                            user["firstName"].toString(),
                            "How ?",
                            "11:55 pm",
                            "_______________________________________"
                        )
                    )
                }
            }
            initRecyclerView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_messages, container, false)
        try {
            initData()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return view1
    }

    private fun newInstance(param1: String?, param2: String?): MessagesFrag {
        val fragment = MessagesFrag()
        val args = Bundle()
        args.putString(ARG_PARAM1, param1)
        args.putString(ARG_PARAM2, param2)
        fragment.arguments = args
        return fragment
    }
}