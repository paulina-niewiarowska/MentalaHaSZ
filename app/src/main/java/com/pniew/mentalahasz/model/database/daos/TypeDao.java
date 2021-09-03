package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pniew.mentalahasz.model.database.entities.Type;

import java.util.List;

@Dao
public interface TypeDao {

    @Insert
    long insertType(Type type);

    @Update
    void updateType(Type type);

    @Query("DELETE FROM Type WHERE typeId = :id")
    void deleteType(int id);

    @Query("SELECT * FROM Type")
    LiveData<List<Type>> getAllTypes();

    @Query("SELECT * FROM Type WHERE typeId = :id")
    LiveData<Type> getTypeById(int id);
}
