package com.pniew.mentalahasz.model.repository;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.ART_PERIOD_STRING;
import static com.pniew.mentalahasz.utils.CallsStringsIntents.MOVEMENT_STRING;
import static com.pniew.mentalahasz.utils.CallsStringsIntents.TYPE_STRING;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.HaszDatabase;
import com.pniew.mentalahasz.model.database.Things;
import com.pniew.mentalahasz.model.database.daos.PictureDao;
import com.pniew.mentalahasz.model.database.entities.Picture;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PictureRepository {
    private final ExecutorService executorService;
    private final PictureDao pictureDao;
    private final LiveData<List<Picture>> allPictures;

    public PictureRepository(Application application) {
        executorService = Executors.newSingleThreadExecutor();
        HaszDatabase haszDatabase = HaszDatabase.getInstance(application);
        pictureDao = haszDatabase.pictureDao();
        allPictures = pictureDao.getAllPictures();
    }

    //to insert

    public void insertNewPicture(String path, String name, String author, String dating, String location, int type, Integer movement, int artPeriod) {
        executorService.execute(() -> {
            Picture picture = new Picture(path, name, author, dating, location);
            picture.setPictureArtPeriod(artPeriod);
            if (movement != null && movement != -1) {
                picture.setPictureMovement(movement);
            } else {
                picture.setPictureMovement(null);
            }
            picture.setPictureType(type);
            pictureDao.insertPicture(picture);
        });
    }

    public void insertNewPicture(String path, String name, String author, String dating, String location, int type, Integer movement, int artPeriod, String trivia) {
        executorService.execute(() -> {
            Picture picture = new Picture(path, name, author, dating, location);
            picture.setPictureArtPeriod(artPeriod);
            if (movement != -1) {
                picture.setPictureMovement(movement);
            } else {
                picture.setPictureMovement(null);
            }
            picture.setPictureType(type);
            picture.setPictureFunFact(trivia);
            pictureDao.insertPicture(picture);
        });
    }

    public void insertNewPictureSync(Picture picture) {
        pictureDao.insertPicture(picture);
    }

    //to update

    public void updatePicture(int idOfOldPicture, String newPath, String newName, String newAuthor, String newDating, String newLocation, int type, @Nullable Integer movement, int artPeriod, @Nullable String trivia) {
        executorService.execute(() -> pictureDao.updatePicture(idOfOldPicture, newPath, newName, newAuthor, newLocation, newDating, artPeriod, movement, type, trivia));
    }

    public void setPictureFunFact(int pictureId, String pictureFunFact) {
        executorService.execute(() -> pictureDao.setPictureFunFact(pictureId, pictureFunFact));
    }

    public void updatePictureKnowledgeDegree(int pictureId, int pictureNewDegree) {
        executorService.execute(() -> pictureDao.updatePictureKnowledgeDegree(pictureId, pictureNewDegree));
    }

    //to delete

    public void deletePicture(Context context, int pictureId) {
        executorService.execute(() -> deletePicturesByIdsButSynchronicznie(context, Collections.singletonList(pictureId)));
    }

    //to read

    public LiveData<List<Picture>> getAllPictures() {
        return allPictures;
    }

    public LiveData<Picture> getPictureById(int id) {
        return pictureDao.getPictureById(id);
    }

    public LiveData<List<Picture>> getPicturesOfMovement(int movementId) {
        return pictureDao.getPictureListByMovementId(movementId);
    }

    public LiveData<List<Picture>> getPicturesOfArtPeriod(int artPeriodId) {
        return pictureDao.getPictureListByArtPeriodId(artPeriodId);
    }

    public LiveData<List<Picture>> getPicturesOfType(int typeId) {
        return pictureDao.getPictureListByTypeId(typeId);
    }

    public List<Picture> getPictureListByArtPeriodIdOrMovementIdButSynchronicznie(List<Integer> artPeriodIds, List<Integer> movementIds) {
        return pictureDao.getPictureListByArtPeriodIdOrMovementIdSync(artPeriodIds, movementIds);
    }

    public List<Picture> getPictureListByThingsIdButSynchronicznie(int artPeriodId, int pictureMovementId, int pictureTypeId) {
        return pictureDao.getPictureListByThingsIdsSync(artPeriodId, pictureMovementId, pictureTypeId);
    }

    public List<Picture> getPictureListByThingsButSynchronicznie(Things things) {
        switch (things.getObjectType()) {
            case ART_PERIOD_STRING:
                return pictureDao.getPictureListByThingsIdsSync(things.getId(), 0, 0);
            case MOVEMENT_STRING:
                return pictureDao.getPictureListByThingsIdsSync(0, things.getId(), 0);
            case TYPE_STRING:
                return pictureDao.getPictureListByThingsIdsSync(0, 0, things.getId());
        }
        return null;
    }

    public List<Picture> getPicturesByIdsButSynchronicznie(List<Integer> pictureIds) {
        return pictureDao.getPicturesByIdsSync(pictureIds);
    }

    public void deletePicturesByIdsButSynchronicznie(Context context, List<Integer> pictureIds) {
        List<Picture> picturesToDelete = getPicturesByIdsButSynchronicznie(pictureIds);
        deletePicturesButSynchronicznie(context, picturesToDelete);
    }

    public void deletePicturesButSynchronicznie(Context context, List<Picture> pictures) {
        File localAppStorage = context.getFilesDir();
        pictureDao.deletePicturesByIds(pictures.stream().map(Picture::getPictureId).collect(Collectors.toList()));
        for (Picture picture : pictures) {
            if (picture.getPicturePath().contains(localAppStorage.getPath())) {
                new File(picture.getPicturePath()).delete();
            }
        }
    }

}
