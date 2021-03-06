package com.pniew.mentalahasz.intermodule_single_picture_activity.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.repository.MovementRepository;
import com.pniew.mentalahasz.model.repository.PictureRepository;

public class TestViewModel extends AndroidViewModel {

    PictureRepository pictureRepository;
    MovementRepository movementRepository;

    private int currentPictureId;
    private String nameString;
    private String authorString;
    private String datingString;
    private String locationString;
    private String movementString;
    private int movementId;
    boolean isMovement;

    boolean hasMovement;

    public TestViewModel(@NonNull Application application) {
        super(application);
        pictureRepository = new PictureRepository(application);
        movementRepository = new MovementRepository(application);
        movementId = -1;
    }

    // getters and setters =========================================================================


    public LiveData<Picture> getPictureById(){
        return pictureRepository.getPictureById(currentPictureId);
    }

    public LiveData<Movement> getMovementById(){
        return movementRepository.getMovementById(movementId);
    }

    //----------------------------------------------------------------------------------------------

    public int getCurrentPictureId() { return currentPictureId; }
    public void setCurrentPictureId(int currentPictureId) { this.currentPictureId = currentPictureId; }

    public String getNameString() { return nameString; }
    public void setNameString(String nameString) { this.nameString = nameString; }

    public String getAuthorString() { return authorString; }
    public void setAuthorString(String authorString) { this.authorString = authorString; }

    public String getDatingString() { return datingString; }
    public void setDatingString(String datingString) { this.datingString = datingString; }

    public String getLocationString() { return locationString; }
    public void setLocationString(String locationString) { this.locationString = locationString; }

    public String getMovementString() { return movementString; }
    public void setMovementString(String movementString) { this.movementString = movementString; }

    public int getMovementId() { return movementId; }
    public void setMovementId(int movementId) { this.movementId = movementId; }

    public boolean isMovement() { return isMovement; }
    public void setMovement(boolean movement) { isMovement = movement; }

    public boolean isHasMovement() { return hasMovement; }
    public void setHasMovement(boolean hasMovement) { this.hasMovement = hasMovement; }

    public boolean isThatCorrect(String title, String author, String location, String dating, String movement) {

        if(movementString != null && !movementString.isEmpty()) {
            isMovement = movementString.equals(movement);
        } else {isMovement = true;}
        return (nameString.equals(title) &&
                authorString.equals(author) &&
                locationString.equals(location) &&
                datingString.equals(dating) &&
                isMovement);
    }

    // methods =====================================================================================


}