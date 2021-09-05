package com.pniew.mentalahasz.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pniew.mentalahasz.model.database.HaszDatabase;
import com.pniew.mentalahasz.model.database.Things;
import com.pniew.mentalahasz.model.database.daos.ArtPeriodDao;
import com.pniew.mentalahasz.model.database.daos.MovementDao;
import com.pniew.mentalahasz.model.database.daos.ThingsDao;
import com.pniew.mentalahasz.model.database.daos.TypeDao;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Type;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThingsRepository {
    private final ExecutorService executorService;
    private final ThingsDao thingsDao;
    private ArtPeriodDao artPeriodDao;
    private TypeDao typeDao;
    private MovementDao movementDao;
    private LiveData<List<Things>> allThings;

    private MutableLiveData<Integer> liveDataId;

    public ThingsRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        HaszDatabase haszDatabase = HaszDatabase.getInstance(application);
        thingsDao = haszDatabase.thingsDao();
        allThings = thingsDao.getAllThings();
        artPeriodDao = haszDatabase.artPeriodDao();
        typeDao = haszDatabase.typeDao();
        movementDao = haszDatabase.movementDao();

        liveDataId = new MutableLiveData<>();
    }

    public void insertArtPeriod(String name, String dating){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArtPeriod artPeriod = new ArtPeriod(name, dating);
                long id = artPeriodDao.insertArtPeriod(artPeriod);
                liveDataId.postValue((int) id);
            }
        });
    }

    public void insertMovement(String name, String dating, String location, int artPeriodId){
            executorService.execute(() -> {
                Movement movement = new Movement(name, dating, location);
                movement.setMovementArtPeriod(artPeriodId);
                long id = movementDao.insertMovement(movement);
                liveDataId.postValue((int) id);
        });
    }

    public void insertType(String name){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Type type = new Type(name);
                long id = typeDao.insertType(type);
                liveDataId.postValue((int) id);
            }
        });
    }

    public LiveData<List<Things>> getAllThings(){
        return allThings;
    }

    public MutableLiveData<Integer> getInsertedIdLiveData() {
        return liveDataId;
    }

    public Type getTypeByItsName(String name) {return typeDao.getTypeByItsName(name); }
}
