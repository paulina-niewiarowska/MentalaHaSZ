package com.pniew.mentalahasz.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.HaszDatabase;
import com.pniew.mentalahasz.model.database.daos.ArtPeriodDao;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArtPeriodRepository {
    private final ExecutorService executorService;
    private final ArtPeriodDao artPeriodDao;
    private final LiveData<List<ArtPeriod>> allArtPeriods;
    private LiveData<ArtPeriod> artPeriodLiveData;

    public ArtPeriodRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        HaszDatabase haszDatabase = HaszDatabase.getInstance(application);
        artPeriodDao = haszDatabase.artPeriodDao();
        allArtPeriods = artPeriodDao.getAllArtPeriods();
    }

    //to insert

    public void insertNewArtPeriod(String name, String dating){
        executorService.execute(() -> {
            ArtPeriod artPeriod = new ArtPeriod(name, dating);
            artPeriodDao.insertArtPeriod(artPeriod);
        });
    }

    //to update

    public void updateArtPeriod(int idOfOldArtPeriod, String newName, String newDating){
        executorService.execute(() -> {
            ArtPeriod artPeriod = new ArtPeriod(newName, newDating);
            artPeriod.setArtPeriodId(idOfOldArtPeriod);
            artPeriodDao.updateArtPeriod(artPeriod);
        });
    }

    //to delete

    public void deleteArtPeriodAndItsChildren(int artPeriodId){
        executorService.execute(() -> artPeriodDao.deleteArtPeriod(artPeriodId));
    }

    //to read

    public LiveData<List<ArtPeriod>> getAllArtPeriods(){ return allArtPeriods; }

    public LiveData<ArtPeriod> getArtPeriodById(int id){ return artPeriodLiveData; }

}
