package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Dao;

import com.pniew.mentalahasz.model.database.connections.MovementsOfPeriod;
import com.pniew.mentalahasz.model.database.connections.PicturesOfMovement;
import com.pniew.mentalahasz.model.database.connections.PicturesOfPeriod;
import com.pniew.mentalahasz.model.database.connections.PicturesOfType;

import java.util.List;

@Dao
public interface HaSZDao {

    @Transaction
    @Query("SELECT * FROM ArtPeriod")
    LiveData<List<MovementsOfPeriod>> loadAllPeriodsWithAllTheirMovements();

    @Transaction
    @Query("SELECT * FROM Movement")
    LiveData<List<PicturesOfMovement>> loadAllMovementsWithAllTheirPictures();

    @Transaction
    @Query("SELECT * FROM ArtPeriod")
    LiveData<List<PicturesOfPeriod>> loadAllPeriodsWithAllTheirPictures();

    @Transaction
    @Query("SELECT * FROM Type")
    LiveData<List<PicturesOfType>> loadAllTypesWithAllTheirPictures();

}
