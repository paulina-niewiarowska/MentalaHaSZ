package com.pniew.mentalahasz.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pniew.mentalahasz.model.database.entities.Picture;

import java.util.List;

@Dao
public interface PictureDao {
    @Insert
    long insertPicture(Picture picture);

    @Update
    void updatePicture(Picture picture);

    @Query("UPDATE Picture SET PictureKnowledgeDegree = :pictureNewDegree WHERE PictureId = :pictureId")
    void updatePictureKnowledgeDegree(int pictureId, int pictureNewDegree);

    @Query("DELETE FROM Picture WHERE pictureId = :id")
    void deletePicture(int id);

    @Query("SELECT * FROM Picture")
    LiveData<List<Picture>> getAllPictures();

    @Query("SELECT * FROM Picture WHERE pictureId = :id")
    LiveData<Picture> getPictureById(int id);

    @Query("SELECT * FROM Picture WHERE pictureMovement = :movementId")
    LiveData<List<Picture>> getPictureListByMovementId(int movementId);

    @Query("SELECT * FROM Picture WHERE pictureType = :typeId")
    LiveData<List<Picture>> getPictureListByTypeId(int typeId);

    @Query("SELECT * FROM Picture WHERE pictureArtPeriod = :artPeriodId")
    LiveData<List<Picture>> getPictureListByArtPeriodId(int artPeriodId);

    @Query("SELECT * FROM Picture WHERE pictureArtPeriod IN (:artPeriodIds) OR pictureMovement IN (:pictureMovementIds)")
    List<Picture> getPictureListByArtPeriodIdOrMovementIdSync(List<Integer> artPeriodIds, List<Integer> pictureMovementIds);
}