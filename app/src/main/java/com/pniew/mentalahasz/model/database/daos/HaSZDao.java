package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Dao;

import com.pniew.mentalahasz.model.database.Things;
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

    @Query("SELECT m.movementId AS id, m.movementName AS \"name\", m.movementDating AS \"dating\", " +
            "m.movementLocation AS \"location\", m.movementArtPeriod AS \"parent_art_period_id\"," +
            "a.artPeriodName AS \"in_period\",m.movementFunFact AS \"fun_fact\", \"Movement\" AS \"object_type\" FROM Movement m " +
            "JOIN ArtPeriod a ON artPeriodId = movementArtPeriod " +
            "UNION SELECT artPeriodId, artPeriodName, artPeriodDating, null, null, null, artPeriodFunFact, \"Art Period\" FROM ArtPeriod " +
            "UNION SELECT typeId, typeName, null,  null, null, null, typeFunFact, \"Type of Artwork\" FROM Type ORDER BY \"object_type\"")
    LiveData<Things> load();
}
