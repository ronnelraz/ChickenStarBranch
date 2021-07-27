package com.charoenpokhandfoodph.viewmodals;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.repo.repo_category;

import java.util.List;

public class CategoryViewModal extends ViewModel {

    private MutableLiveData<List<category_view_list>> mCategory;
    private repo_category mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init(){
        if(mCategory != null){
            return;
        }
        mRepo = repo_category.getInstance();
        mCategory = mRepo.getNicePlaces();
    }

    public void addNewValue(final category_view_list category){
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                List<category_view_list> currentPlaces = mCategory.getValue();
                currentPlaces.add(category);
                mCategory.postValue(currentPlaces);
                mIsUpdating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public LiveData<List<category_view_list>> getCategory(){
        return mCategory;
    }


    public LiveData<Boolean> getIsUpdating(){
        return mIsUpdating;
    }
}
