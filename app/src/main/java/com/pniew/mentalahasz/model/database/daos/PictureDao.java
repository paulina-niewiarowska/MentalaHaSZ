package com.pniew.mentalahasz.model.database.daos;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pniew.mentalahasz.model.database.entities.Picture;

import java.util.List;

@Dao
public interface PictureDao {
    @Insert
    long insertPicture(Picture picture);

    @Query("UPDATE Picture SET picturePath = :newPath, pictureName = :name, pictureAuthor = :author, pictureLocation = :location, pictureDating = :dating, pictureArtPeriod = :artPeriod, pictureMovement = :movement, pictureType = :type, pictureFunFact = :trivia WHERE pictureId = :id")
    void updatePicture(int id, String newPath, String name, String author, String location, String dating, int artPeriod, @Nullable Integer movement, int type, @Nullable String trivia);

    @Query("UPDATE Picture SET pictureKnowledgeDegree = :pictureNewDegree WHERE pictureId = :pictureId")
    void updatePictureKnowledgeDegree(int pictureId, int pictureNewDegree);

    @Query("UPDATE Picture SET pictureFunFact = :pictureFunFact WHERE pictureId = :pictureId")
    void setPictureFunFact(int pictureId, String pictureFunFact);

    @Query("DELETE FROM Picture WHERE pictureId = :id")
    void deletePicture(int id);

    @Query("DELETE FROM Picture WHERE pictureId IN (:pictureIds)")
    void deletePicturesByIds(List<Integer> pictureIds);

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

    @WorkerThread
    @Query("SELECT * FROM Picture WHERE pictureArtPeriod IN (:artPeriodIds) OR pictureMovement IN (:pictureMovementIds)")
    List<Picture> getPictureListByArtPeriodIdOrMovementIdSync(List<Integer> artPeriodIds, List<Integer> pictureMovementIds);

    @WorkerThread
    @Query("SELECT * FROM Picture WHERE pictureArtPeriod = :artPeriodId OR pictureMovement = :pictureMovementId OR pictureType = :pictureTypeId")
    List<Picture> getPictureListByThingsIdsSync(int artPeriodId, int pictureMovementId, int pictureTypeId);

    @WorkerThread
    @Query("SELECT * FROM Picture WHERE pictureId IN (:pictureIds)")
    List<Picture> getPicturesByIdsSync(List<Integer> pictureIds);
}
