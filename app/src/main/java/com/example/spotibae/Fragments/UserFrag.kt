package com.example.spotibae.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.spotibae.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spotibae.Models.Setters.MatchingModel
import com.squareup.picasso.Picasso
import java.lang.Boolean
import java.util.ArrayList
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 * Use the [UserFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFrag : Fragment() {
    var userList1: MutableList<MatchingModel> = ArrayList()
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var users: HashMap<String, HashMap<String, Any>>? = null
    var nameText: TextView? = null
    var ageText: TextView? = null
    var bioText: TextView? = null
    var imageView: ImageView? = null
    var matchButton: ImageView? = null
    var denyButton: ImageView? = null
    var refreshButton: ImageView? = null
    var count = 0
    var imageUriList: MutableList<String?> = ArrayList()
    var leftSide: View? = null
    var rightSide: View? = null
    var picCount = 0

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_user, container, false)
        nameText = view?.findViewById(R.id.nameText)
        ageText = view?.findViewById(R.id.ageText)
        bioText = view?.findViewById(R.id.bioText)
        imageView = view?.findViewById(R.id.spotifyPictures)
        matchButton = view?.findViewById(R.id.matchButton)
        denyButton = view?.findViewById(R.id.denyButton)
        refreshButton = view?.findViewById(R.id.refreshButton)
        leftSide = view?.findViewById(R.id.left)
        rightSide = view?.findViewById(R.id.right)
        setListeners()
        getUsers()
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    fun getUsers() {
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        val uId = user!!.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        mDatabase!!.get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
            } else {
                Log.d("Firebase Users", task.result.value.toString())
                users = task.result.value as HashMap<String, HashMap<String, Any>>?
                val myUser = users!![uId] as HashMap<String, Any>
                val myUserMatches = myUser["matches"] as HashMap<String, Any>?
                users!!.remove(uId)
                Log.d("Firebase Users", users.toString())
                if (myUserMatches == null) {
                } else {
                    for ((key) in myUserMatches) {
                        users!!.remove(key)
                    }
                }
                for ((key, userHash) in users!!) {
                    val uriList: MutableList<String?> = ArrayList()
                    val userSpotifyVerified =
                        Boolean.parseBoolean(userHash["spotifyVerified"].toString())
                    if (userSpotifyVerified) {
                        if (userHash["gender"].toString() == myUser["genderPref"].toString() && myUser["gender"].toString() == userHash["genderPref"].toString()
                            && userHash["lowestAgePref"].toString()
                                .toInt() <= myUser["age"].toString().toInt()
                            && userHash["highestAgePref"].toString()
                                .toInt() >= myUser["age"].toString().toInt()
                            && myUser["highestAgePref"].toString()
                                .toInt() >= userHash["age"].toString().toInt()
                            && myUser["lowestAgePref"].toString()
                                .toInt() <= userHash["age"].toString().toInt()
                        ) {
                            val favoriteArtistMap =
                                userHash["favoriteArtists"] as HashMap<String, HashMap<String, String>>?
                            for ((_, userHash1) in favoriteArtistMap!!) {
                                val imageUri = userHash1["imageURI"]
                                uriList.add(imageUri)
                            }
                            userList1.add(
                                MatchingModel(
                                    key,
                                    uriList,
                                    userHash["firstName"].toString() + ", ",
                                    userHash["age"].toString(),
                                    userHash["bio"].toString()
                                )
                            )
                        }
                    }
                }
                initView()
            }
        }
    }

    fun initView() {
        val size = userList1.size
        if (userList1.isEmpty() || size == count) {
            nameText!!.text = "No More Users To Match With"
            ageText!!.text = ""
            bioText!!.text = ""
            Picasso.get()
                .load("https://img.buzzfeed.com/buzzfeed-static/static/2016-05/10/12/enhanced/webdr06/anigif_enhanced-12275-1462899229-2.gif?output-format=mp4")
                .resize(400, 400).into(imageView)
        } else {
            nameText!!.text = userList1[count].nameText
            ageText!!.text = userList1[count].ageText
            bioText!!.text = userList1[count].bioText
            val imageUri = userList1[count].imageview[picCount]?.trim { it <= ' ' }
            val uri = imageUri?.length?.minus(1)?.let { imageUri.substring(25, it) }
            Picasso.get().isLoggingEnabled = true
            Picasso.get().load("https://i.scdn.co/image/$uri").resize(400, 400).into(imageView)
        }
    }

    private fun setListeners() {
        matchButton!!.setOnClickListener { view1: View? ->
            val size = userList1.size
            if (count <= size - 1) {
                matchUser(count)
                count += 1
                picCount = 0
                initView()
            }
        }
        denyButton!!.setOnClickListener { view1: View? ->
            val size = userList1.size
            if (count <= size - 1) {
                count += 1
                picCount = 0
                initView()
            }
        }
        rightSide!!.setOnClickListener { view1: View? ->
            val size = userList1[count].imageview.size
            if (picCount < size - 1) {
                picCount += 1
                initView()
            }
        }
        leftSide!!.setOnClickListener { view1: View? ->
            if (picCount > 0) {
                picCount -= 1
                initView()
            }
        }
    }

    private fun matchUser(countMatch: Int) {
        val userId = userList1[countMatch].firebaseId
        val uId = mAuth!!.uid
        mDatabase!!.child(uId!!).child("matches").child(userId).setValue(true)
    }

    private val emailAndSetImage: Unit
        private get() {
            val uId = userList1[count].firebaseId
            mDatabase!!.child(uId).get().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("firebase", "Error getting data", task.exception)
                } else {
                    val user = task.result.value as HashMap<String, Any>?
                    val email = user!!["email"].toString()
                    val favoriteArtistMap =
                        user["favoriteArtists"] as HashMap<String, HashMap<String, String>>?
                    for ((_, userHash) in favoriteArtistMap!!) {
                        val imageUri = userHash["imageURI"]
                        imageUriList.add(imageUri)
                    }
                    val imageUri = imageUriList[picCount]!!.trim { it <= ' ' }
                    val uri = imageUri.substring(25, imageUri.length - 1)
                    Picasso.get().isLoggingEnabled = true
                    Picasso.get().load("https://i.scdn.co/image/$uri").resize(400, 400)
                        .into(imageView)
                }
            }
        }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFrag.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): UserFrag {
            val fragment = UserFrag()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}