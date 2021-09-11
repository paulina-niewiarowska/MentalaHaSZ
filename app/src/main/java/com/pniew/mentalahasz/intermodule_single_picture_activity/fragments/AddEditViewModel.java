package com.pniew.mentalahasz.intermodule_single_picture_activity.fragments;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.model.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.model.repository.MovementRepository;
import com.pniew.mentalahasz.model.repository.PictureRepository;
import com.pniew.mentalahasz.model.repository.TypeRepository;

import java.util.List;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class AddEditViewModel extends AndroidViewModel {
    //----------------------------------------------------------------------------------------------
    // member variables

    private ArtPeriodRepository artPeriodRepository;
    private MovementRepository movementRepository;
    private PictureRepository pictureRepository;
    private TypeRepository typeRepository;

    private int id;
    private String path;
    private String name;
    private String author;
    private String location;
    private String dating;
    private int artPeriodId;
    private int movementId;
    private int typeId;
    private String trivia;

    private int callingThing;
    private int callingThingId;
    private boolean changedThing;

    private LiveData<Picture> picture;
    private LiveData<List<ArtPeriod>> allArtPeriodList;
    private LiveData<List<Movement>> allMovementsList;
    private LiveData<List<Type>> allTypesList;
    private MutableLiveData<Integer> liveDataId;

    private int spinnerArtPeriodSelection;
    private int spinnerMovementSelection;
    private int spinnerTypeSelection;

    //----------------------------------------------------------------------------------------------
    //constructor

    public AddEditViewModel(@NonNull Application application) {
        super(application);

        artPeriodRepository = new ArtPeriodRepository(application);
        movementRepository = new MovementRepository(application);
        pictureRepository = new PictureRepository(application);
        typeRepository = new TypeRepository(application);

        allArtPeriodList = artPeriodRepository.getAllArtPeriods();
        allMovementsList = movementRepository.getAllMovements();
        allTypesList = typeRepository.getAllTypes();

        spinnerArtPeriodSelection = -1;
        spinnerMovementSelection = -1;
        spinnerTypeSelection = -1;
        id = -1;
        artPeriodId = -1;
        movementId = -1;
        typeId = -1;
    }

    //==============================================================================================
    // getters setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

    public int getArtPeriodId() {
        return artPeriodId;
    }

    public void setArtPeriodId(int art_period) {
        this.artPeriodId = art_period;
    }

    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movement) {
        this.movementId = movement;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTrivia() {
        return trivia;
    }

    public void setTrivia(String trivia) {
        this.trivia = trivia;
    }

    public int getSpinnerArtPeriodSelection() {
        return spinnerArtPeriodSelection;
    }

    public void setSpinnerArtPeriodSelection(int spinnerArtPeriodSelection) {
        this.spinnerArtPeriodSelection = spinnerArtPeriodSelection;
    }

    public int getSpinnerMovementSelection() {
        return spinnerMovementSelection;
    }

    public void setSpinnerMovementSelection(int spinnerMovementSelection) {
        this.spinnerMovementSelection = spinnerMovementSelection;
    }

    public int getSpinnerTypeSelection() {
        return spinnerTypeSelection;
    }

    public void setSpinnerTypeSelection(int spinnerTypeSelection) {
        this.spinnerTypeSelection = spinnerTypeSelection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCallingThing() {
        return callingThing;
    }

    public void setCallingThing(int callingThing) {
        this.callingThing = callingThing;
    }

    public int getCallingThingId() {
        return callingThingId;
    }

    public void setCallingThingId(int callingThingId) {
        this.callingThingId = callingThingId;
    }

    public boolean isChangedThing() {
        return changedThing;
    }

    //==============================================================================================

    public void setPictureById(int id) {
        picture = pictureRepository.getPictureById(id);
    }

    public void itsArtPeriodCalling() {
        artPeriodId = callingThingId;
    }

    public void itsMovementCalling(int parentPeriodId) {
        movementId = callingThingId;
        artPeriodId = parentPeriodId;
    }


    //==============================================================================================
    //Live Data

    public LiveData<List<ArtPeriod>> getAllArtPeriods() {
        return allArtPeriodList;
    }

    public LiveData<List<Movement>> getAllMovements() {
        return allMovementsList;
    }

    public LiveData<List<Type>> getAllTypes() {
        return allTypesList;
    }

    public LiveData<Picture> getPicture() {
        return picture;
    }

    public LiveData<List<Movement>> getMovementListByPeriodId(int id) {
        return movementRepository.getMovementListByArtPeriodId(id);
    }

    //==============================================================================================
    // Do things to picture

    public int savePicture() {
        if (path == null || name == null || author == null || dating == null || location == null
                || typeId == 0 || artPeriodId == 0 || typeId == -1 || artPeriodId == -1
                || path.trim().equals("")
                || name.trim().equals("")
                || author.trim().equals("")
                || dating.trim().equals("")
                || location.trim().equals("")) {
            return 0;
        } else {
            Integer movement;
            if (movementId == -1) {
                movement = null;
            } else {
                movement = movementId;
            }
            if (trivia == null) {
                pictureRepository.insertNewPicture(path, name, author, dating, location, typeId, movement, artPeriodId);
            } else {
                pictureRepository.insertNewPicture(path, name, author, dating, location, typeId, movement, artPeriodId, trivia);
            }
            return 1;
        }
    }

    public int updatePicture() {
        if (path == null || name == null || author == null || dating == null || location == null
                || typeId == 0 || artPeriodId == 0 || typeId == -1 || artPeriodId == -1
                || path.trim().equals("")
                || name.trim().equals("")
                || author.trim().equals("")
                || dating.trim().equals("")
                || location.trim().equals("")) {
            //Toast.makeText(getApplication(), "path: " + path, Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            if (callingThing == MOVEMENT && callingThingId != movementId) {
                changedThing = true;
            } else if (callingThing == ART_PERIOD && callingThingId != artPeriodId) {
                changedThing = true;
            } else if (callingThing == TYPE && callingThingId != typeId) {
                changedThing = true;
            }
            Integer movement;
            if (movementId == -1) {
                movement = null;
            } else {
                movement = movementId;
            }
            pictureRepository.updatePicture(id, path, name, author, dating, location, typeId, movement, artPeriodId, trivia);
            return 1;
        }
    }

    public void deletePicture() {
        pictureRepository.deletePicture(getApplication(), id);
    }

}
