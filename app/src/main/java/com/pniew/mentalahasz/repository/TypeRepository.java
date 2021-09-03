package com.pniew.mentalahasz.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.HaszDatabase;
import com.pniew.mentalahasz.model.database.daos.TypeDao;
import com.pniew.mentalahasz.model.database.entities.Type;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TypeRepository {
    private final ExecutorService executorService;
    private final TypeDao typeDao;
    private final LiveData<List<Type>> allTypes;
    private LiveData<Type> typeLiveData;

    public TypeRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        HaszDatabase haszDatabase = HaszDatabase.getInstance(application);
        typeDao = haszDatabase.typeDao();
        allTypes = typeDao.getAllTypes();
    }

    //to insert

    public void insertNewType(String name){
        executorService.execute(() -> {
            Type type = new Type(name);
            typeDao.insertType(type);
        });
    }



    //to update

    public void updateType(int idOfOldType, String newName){
        executorService.execute(() -> {
            Type type = new Type(newName);
            type.setTypeId(idOfOldType);
            typeDao.updateType(type);
        });
    }

    //to delete

    public void deleteTypeAndItsChildren(int typeId){
        executorService.execute(() -> typeDao.deleteType(typeId));
    }

    //to read

    public LiveData<List<Type>> getAllTypes(){ return allTypes; }

    public LiveData<Type> getTypeById(){ return typeLiveData; }
}
