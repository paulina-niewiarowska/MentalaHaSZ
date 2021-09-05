package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pniew.mentalahasz.model.database.entities.Movement;

import java.util.List;

@Dao
public interface MovementDao {
    @Insert
    long insertMovement(Movement movement);

    @Update
    void updateMovement(Movement movement);

    @Query("DELETE FROM Movement WHERE movementId = :id")
    void deleteMovement(int id);

    @Query("UPDATE Movement SET movementFunFact = :movementFunFact WHERE movementId = :movementId")
    void setMovementFunFact(int movementId, String movementFunFact);

    @Query("SELECT * FROM Movement")
    LiveData<List<Movement>> getAllMovements();

    @Query("SELECT * FROM Movement WHERE movementId = :id")
    LiveData<Movement> getMovementById(int id);

    @Query("SELECT * FROM Movement WHERE movementArtPeriod = :id")
    LiveData<List<Movement>> getMovementListByArtPeriodId(int id);

    @Query("SELECT * FROM Movement WHERE movementName = :name LIMIT 1")
    Movement getMovementByItsName(String name);

}
