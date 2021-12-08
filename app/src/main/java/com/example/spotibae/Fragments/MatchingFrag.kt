package com.example.spotibae.Fragments

import android.os.Bundle
import com.example.spotibae.Fragments.MatchingFrag
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.spotibae.Fragments.MessagesFrag
import kotlin.Throws
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.example.spotibae.Fragments.QRFrag
import com.example.spotibae.Models.Setters.MatchingModel
import com.example.spotibae.Fragments.UserFrag
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Use the [MatchingFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatchingFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var matchButton: ImageView? = null
    var denyButton: ImageView? = null
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
        var view: View?
        view = inflater.inflate(R.layout.fragment_matching, container, false)
        matchButton = view?.findViewById(R.id.matchButton)
        denyButton = view?.findViewById(R.id.denyButton)
        return view
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
         * @return A new instance of fragment MatchingFrag.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): MatchingFrag {
            val fragment = MatchingFrag()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}