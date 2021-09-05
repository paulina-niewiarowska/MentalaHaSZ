package com.pniew.mentalahasz.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.pniew.mentalahasz.model.database.HaszDatabase;
import com.pniew.mentalahasz.model.database.daos.MovementDao;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.thetest.TestMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovementRepository {
    private final ExecutorService executorService;
    private final MovementDao movementDao;
    private final LiveData<List<Movement>> allMovements;

    public MovementRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        HaszDatabase haszDatabase = HaszDatabase.getInstance(application);
        movementDao = haszDatabase.movementDao();
        allMovements = movementDao.getAllMovements();
    }

    //to insert

    public void insertNewMovement(String name, String dating, String location, int artPeriodId){
        executorService.execute(() -> {
            Movement movement = new Movement(name, dating, location);
            movement.setMovementArtPeriod(artPeriodId);
            movementDao.insertMovement(movement);
        });
    }

    //to update

    public void updateMovement(int idOfOldMovement, String newName, String newDating, String newLocation, int artPeriod){
        executorService.execute(() -> {
            Movement movement = new Movement(newName, newDating, newLocation);
            movement.setMovementId(idOfOldMovement);
            movement.setMovementArtPeriod(artPeriod);
            movementDao.updateMovement(movement);
        });
    }

    public void setMovementFunFact(int movementId, String movementFunFact){
        executorService.execute(() -> {
            movementDao.setMovementFunFact(movementId, movementFunFact);
        });
    }

    //to delete

    public void deleteMovementAndItsChildren(int movementId){
        executorService.execute(() -> movementDao.deleteMovement(movementId));
    }

    //to read

    public LiveData<List<Movement>> getAllMovements(){ return allMovements; }

    public LiveData<Movement> getMovementById(int id){ return movementDao.getMovementById(id); }

    public LiveData<List<Movement>> getMovementListByArtPeriodId(int id) { return movementDao.getMovementListByArtPeriodId(id); }
}
