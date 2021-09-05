package com.pniew.mentalahasz.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.HaszDatabase;
import com.pniew.mentalahasz.model.database.daos.PictureDao;
import com.pniew.mentalahasz.model.database.entities.Picture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PictureRepository {
    private final ExecutorService executorService;
    private final PictureDao pictureDao;
    private final LiveData<List<Picture>> allPictures;

    public PictureRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        HaszDatabase haszDatabase = HaszDatabase.getInstance(application);
        pictureDao = haszDatabase.pictureDao();
        allPictures = pictureDao.getAllPictures();
    }

    //to insert

    public void insertNewPicture(String path, String name, String author, String dating, String location, int type, Integer movement, int artPeriod){
        executorService.execute(() -> {
            Picture picture = new Picture(path, name, author, dating, location);
            picture.setPictureArtPeriod(artPeriod);
            if(movement != -1) {
                picture.setPictureMovement(movement);
            } else {
                picture.setPictureMovement(null);
            }
            picture.setPictureType(type);
            pictureDao.insertPicture(picture);
        });
    }

    public void insertNewPicture(String path, String name, String author, String dating, String location, int type, Integer movement, int artPeriod, String trivia){
        executorService.execute(() -> {
            Picture picture = new Picture(path, name, author, dating, location);
            picture.setPictureArtPeriod(artPeriod);
            if(movement != -1) {
                picture.setPictureMovement(movement);
            } else {
                picture.setPictureMovement(null);
            }
            picture.setPictureType(type);
            picture.setPictureFunFact(trivia);
            pictureDao.insertPicture(picture);
        });
    }

    //to update

    public void updatePicture(int idOfOldPicture, String newPath, String newName, String newAuthor, String newDating, String newLocation, int type, Integer movement, int artPeriod){
        executorService.execute(() -> {
            Picture picture = new Picture(newPath, newName, newAuthor, newDating, newLocation);
            picture.setPictureId(idOfOldPicture);
            picture.setPictureArtPeriod(artPeriod);
            if(movement != -1) {
                picture.setPictureMovement(movement);
            } else {
                picture.setPictureMovement(null);
            }
            picture.setPictureType(type);
            pictureDao.updatePicture(picture);
        });
    }

    public void setPictureFunFact(int pictureId, String pictureFunFact){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pictureDao.setPictureFunFact(pictureId, pictureFunFact);
            }
        });
    }

    public void updatePictureKnowledgeDegree(int pictureId, int pictureNewDegree){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                pictureDao.updatePictureKnowledgeDegree(pictureId, pictureNewDegree);
            }
        });
    }

    //to delete

    public void deletePicture(int pictureId){
        executorService.execute(() -> pictureDao.deletePicture(pictureId));
    }

    //to read

    public LiveData<List<Picture>> getAllPictures(){ return allPictures; }

    public LiveData<Picture> getPictureById(int id){ return pictureDao.getPictureById(id); }

    public LiveData<List<Picture>> getPicturesOfMovement(int movementId) { return pictureDao.getPictureListByMovementId(movementId); }

    public LiveData<List<Picture>> getPicturesOfArtPeriod(int artPeriodId) { return pictureDao.getPictureListByArtPeriodId(artPeriodId); }

    public LiveData<List<Picture>> getPicturesOfType(int typeId) { return pictureDao.getPictureListByTypeId(typeId); }

    public List<Picture> getPictureListByArtPeriodIdOrMovementIdSync(List<Integer> artPeriodIds, List<Integer> movementIds) {
        return pictureDao.getPictureListByArtPeriodIdOrMovementIdSync(artPeriodIds, movementIds);
    }
}
