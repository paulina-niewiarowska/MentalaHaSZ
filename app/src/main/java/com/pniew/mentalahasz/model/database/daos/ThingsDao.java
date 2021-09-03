package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.pniew.mentalahasz.model.database.Things;

import java.util.List;

@Dao
public interface ThingsDao {

    @Query("SELECT * FROM Things")
    LiveData<List<Things>> getAllThings();
}
