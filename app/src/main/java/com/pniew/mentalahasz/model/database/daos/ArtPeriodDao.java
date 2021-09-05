package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;

import java.util.List;

@Dao
public interface ArtPeriodDao {

    @Insert
    long insertArtPeriod(ArtPeriod artPeriod);

    @Update
    void updateArtPeriod(ArtPeriod artPeriod);

    @Query("DELETE FROM ArtPeriod WHERE artPeriodId = :id")
    void deleteArtPeriod(int id);

    @Query("UPDATE ArtPeriod SET artPeriodFunFact = :artPeriodFunFact WHERE artPeriodId = :artPeriodId")
    void setArtPeriodFunFact(int artPeriodId, String artPeriodFunFact);

    @Query("SELECT * FROM ArtPeriod ORDER BY artPeriodId ASC")
    LiveData<List<ArtPeriod>> getAllArtPeriods();

    @Query("SELECT * FROM ArtPeriod WHERE artPeriodId = :id")
    LiveData<ArtPeriod> getArtPeriodById(int id);

    @Query("SELECT * FROM ArtPeriod WHERE artPeriodName = :name LIMIT 1")
    ArtPeriod getArtPeriodByItsName(String name);
}
