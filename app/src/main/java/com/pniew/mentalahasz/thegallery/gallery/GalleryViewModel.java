package com.pniew.mentalahasz.thegallery.gallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.repository.PictureRepository;

import java.util.List;

public class GalleryViewModel extends AndroidViewModel {

    private int parentPeriodId;

    private int idOfCallingThing;
    private int callingThing;

    private PictureRepository pictureRepository;
    private LiveData<List<Picture>> picturesOfMovement;
    private LiveData<List<Picture>> picturesOfArtPeriod;


    // constructor =================================================================================
    public GalleryViewModel(@NonNull Application application) {
        super(application);
        pictureRepository = new PictureRepository(application);

    }

    // getters setters =============================================================================

    public LiveData<List<Picture>> getPictureListByPeriodId(int artPeriodId){
        return pictureRepository.getPicturesOfArtPeriod(artPeriodId);
    }

    public LiveData<List<Picture>> getPictureListByMovementId(int movementId){
        return pictureRepository.getPicturesOfMovement(movementId);
    }

    public int parentPeriodId() { return parentPeriodId; }
    public void setParentPeriodId(int parentPeriodId) { this.parentPeriodId = parentPeriodId; }

    public int idOfCallingThing() { return idOfCallingThing; }
    public void setIdOfCallingThing(int idOfCallingThing) { this.idOfCallingThing = idOfCallingThing; }

    public int callingThing() { return callingThing; }
    public void setCallingThing(int callingThing) { this.callingThing = callingThing; }
}
