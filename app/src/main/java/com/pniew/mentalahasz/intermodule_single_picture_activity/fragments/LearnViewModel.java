package com.pniew.mentalahasz.intermodule_single_picture_activity.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.repository.MovementRepository;
import com.pniew.mentalahasz.model.repository.PictureRepository;

public class LearnViewModel extends AndroidViewModel {
    PictureRepository pictureRepository;
    MovementRepository movementRepository;

    private int pictureId;
    private String nameString;
    private String authorString;
    private String datingString;
    private String locationString;
    private int movementId;
    private String movementString;
    private int currentKnowledgeDegree;


    public LearnViewModel(@NonNull Application application) {
        super(application);
        pictureRepository = new PictureRepository(application);
        movementRepository = new MovementRepository(application);
        movementId = -1;


    }

    public LiveData<Picture> getPictureById(){
        return pictureRepository.getPictureById(pictureId);
    }

    public LiveData<Movement> getMovementById(){
        return movementRepository.getMovementById(movementId);
    }



    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public String getAuthorString() {
        return authorString;
    }

    public void setAuthorString(String authorString) {
        this.authorString = authorString;
    }

    public String getDatingString() {
        return datingString;
    }

    public void setDatingString(String datingString) {
        this.datingString = datingString;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }

    public String getMovementString() {
        return movementString;
    }

    public void setMovementString(String movementString) {
        this.movementString = movementString;
    }

    public int getCurrentKnowledgeDegree() {
        return currentKnowledgeDegree;
    }

    public void setCurrentKnowledgeDegree(int currentKnowledgeDegree) {
        this.currentKnowledgeDegree = currentKnowledgeDegree;
    }


    public void updatePictureKnowledgeDegree() {
        pictureRepository.updatePictureKnowledgeDegree(pictureId, currentKnowledgeDegree);
    }

    public void decreaseKnowledgeDegree() {
        if (currentKnowledgeDegree >= 1) {
            currentKnowledgeDegree--;
            updatePictureKnowledgeDegree();
        }
    }

    public void increaseKnowledgeDegree(){
        if (currentKnowledgeDegree < 10) {
            currentKnowledgeDegree++;
            updatePictureKnowledgeDegree();
        }
    }
}