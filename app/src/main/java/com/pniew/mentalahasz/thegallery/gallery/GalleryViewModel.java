package com.pniew.mentalahasz.thegallery.gallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.repository.MovementRepository;
import com.pniew.mentalahasz.repository.PictureRepository;

import java.util.List;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.ART_PERIOD;
import static com.pniew.mentalahasz.utils.CallsStringsIntents.MOVEMENT;

public class GalleryViewModel extends AndroidViewModel {

    private int parentPeriodId;

    private int idOfCallingThing;
    private int callingThing;
    private String triviaText;

    private final PictureRepository pictureRepository;
    private final MovementRepository movementRepository;
    private final ArtPeriodRepository artPeriodRepository;
    private LiveData<List<Picture>> picturesOfMovement;
    private LiveData<List<Picture>> picturesOfArtPeriod;


    // constructor =================================================================================
    public GalleryViewModel(@NonNull Application application) {
        super(application);
        pictureRepository = new PictureRepository(application);
        movementRepository = new MovementRepository(application);
        artPeriodRepository = new ArtPeriodRepository(application);


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

    public String getTriviaText() { return triviaText; }
    public void setTriviaText(String triviaText) { this.triviaText = triviaText; }



    public void updateTrivia() {
        if(callingThing == MOVEMENT){
            movementRepository.setMovementFunFact(idOfCallingThing, triviaText);
        }
        if(callingThing == ART_PERIOD){
            artPeriodRepository.setArtPeriodFunFact(idOfCallingThing, triviaText);
        }

    }
}
