package com.charoenpokhandfoodph.repo;

import androidx.lifecycle.MutableLiveData;

import com.charoenpokhandfoodph.Fragment.product;
import com.charoenpokhandfoodph.modal.category_view_list;

import java.util.ArrayList;
import java.util.List;

public class repo_category {

    private static repo_category instance;
    com.charoenpokhandfoodph.Fragment.product product = new product();


    public static repo_category getInstance(){
        if(instance == null){
            instance = new repo_category();
        }
        return instance;
    }


    // Pretend to get data from a webservice or online source
    public MutableLiveData<List<category_view_list>> getNicePlaces(){
        product.setCategoryList();
        MutableLiveData<List<category_view_list>> data = new MutableLiveData<>();
        data.setValue(product.dataSet);
        return data;
    }





}
