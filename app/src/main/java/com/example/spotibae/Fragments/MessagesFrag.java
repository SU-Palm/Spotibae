package com.example.spotibae.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotibae.Adapter.Adapter;
import com.example.spotibae.Models.ModelClass;
import com.example.spotibae.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFrag extends Fragment {

    RecyclerView mrecyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userList;
    Adapter adapter;
    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessagesFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFrag newInstance(String param1, String param2) {
        MessagesFrag fragment = new MessagesFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initRecyclerView() {
        /*
        mrecyclerView= view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(layoutManager);
        adapter=new Adapter(userList);
        mrecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
         */
        mrecyclerView = view.findViewById(R.id.recyclerView);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mrecyclerView.setAdapter(new Adapter(userList));
    }

    private void initData() {
        userList = new ArrayList<>();

        userList.add(new ModelClass(R.drawable.gi,"Anjali","How are you?","10:45 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.bo,"Brijesh","I am fine","15:08 pm","_______________________________________"));

        userList.add(new ModelClass(R.drawable.boy,"Sam","You Know?","1:02 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.girl,"Divya","How are you?","12:55 pm","_______________________________________"));

        userList.add(new ModelClass(R.drawable.gi,"Simran","This is Easy","13:50 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.boy,"Karan","I am Don","1:08 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.bo,"Sameer","You Know this?","4:02 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.girl,"Baby","How ?","11:55 pm","_______________________________________"));

        userList.add(new ModelClass(R.drawable.gi,"Anjali","How are you?","10:45 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.bo,"Brijesh","I am fine","15:08 pm","_______________________________________"));

        userList.add(new ModelClass(R.drawable.boy,"Sam","You Know?","1:02 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.girl,"Divya","How are you?","12:55 pm","_______________________________________"));

        userList.add(new ModelClass(R.drawable.gi,"Simran","This is Easy","13:50 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.boy,"Karan","I am Don","1:08 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.bo,"Sameer","You Know this?","4:02 am","_______________________________________"));

        userList.add(new ModelClass(R.drawable.girl,"Baby","How ?","11:55 pm","_______________________________________"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        initData();
        initRecyclerView();
        return view;
    }
}