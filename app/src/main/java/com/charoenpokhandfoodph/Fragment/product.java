package com.charoenpokhandfoodph.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.modal.category_view_list;

import java.util.ArrayList;

public class product extends Fragment {

    public ArrayList<category_view_list> dataSet = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_completed,parent,false);

        return view;
    }


    public void setCategoryList(){
        dataSet.add(new category_view_list("1","test","product/rr.jpg"));
    }
}
