package com.example.spotibae.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spotibae.R
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Use the [QRFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"
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
        return inflater.inflate(R.layout.fragment_q_r, container, false)
    }

    fun newInstance(param1: String?, param2: String?): QRFrag {
        val fragment = QRFrag()
        val args = Bundle()
        args.putString(ARG_PARAM1, param1)
        args.putString(ARG_PARAM2, param2)
        fragment.arguments = args
        return fragment
    }
}