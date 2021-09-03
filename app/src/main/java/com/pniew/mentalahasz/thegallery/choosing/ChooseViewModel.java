package com.pniew.mentalahasz.thegallery.choosing;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.repository.MovementRepository;
import com.pniew.mentalahasz.repository.TypeRepository;

import java.util.List;

public class ChooseViewModel extends AndroidViewModel {

    private int parentPeriodId;

    private ArtPeriodRepository artPeriodRepository;
    private MovementRepository movementRepository;
    private TypeRepository typeRepository;

    private LiveData<List<ArtPeriod>> allArtPeriodList;
    private LiveData<List<Movement>> allMovementList;
    private LiveData<List<Type>> allTypeList;

    private LiveData<List<Movement>> movementListByPeriodId;

    private boolean isMovementListEmpty;
    private boolean nothingToShow;

    //constructor ==================================================================================

    public ChooseViewModel(@NonNull Application application) {
        super(application);

        artPeriodRepository = new ArtPeriodRepository(application);
        movementRepository = new MovementRepository(application);
        typeRepository = new TypeRepository(application);

        allArtPeriodList = artPeriodRepository.getAllArtPeriods();
        allMovementList = movementRepository.getAllMovements();
        allTypeList = typeRepository.getAllTypes();
    }

    // getters setters =============================================================================


    public int getParentPeriodId() { return parentPeriodId; }
    public void setParentPeriodId(int parentPeriodId) { this.parentPeriodId = parentPeriodId; }

    public LiveData<List<ArtPeriod>> getAllArtPeriods(){ return allArtPeriodList; }
    public LiveData<List<Movement>> getAllMovements(){ return allMovementList; }
    public LiveData<List<Type>> getAllTypes(){ return allTypeList; }

    public LiveData<List<Movement>> getMovementListByPeriodId() {
        return movementRepository.getMovementListByArtPeriodId(parentPeriodId);
    }

    public boolean isNothingToShow() { return nothingToShow; }
    public void setNothingToShow(boolean nothingToShow) { this.nothingToShow = nothingToShow; }
}