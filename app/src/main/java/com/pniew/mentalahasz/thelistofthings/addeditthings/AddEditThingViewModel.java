package com.pniew.mentalahasz.thelistofthings.addeditthings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.repository.MovementRepository;
import com.pniew.mentalahasz.repository.ThingsRepository;
import com.pniew.mentalahasz.repository.TypeRepository;

import java.util.List;

public class AddEditThingViewModel extends AndroidViewModel {
    //----------------------------------------------------------------------------------------------
    //member variables

    public static final int CHOSEN_ART_PERIOD = 1;
    public static final int CHOSEN_MOVEMENT = 2;
    public static final int CHOSEN_TYPE = 3;

    public static final int PERIOD_UPDATED = 5;
    public static final int PERIOD_ADDED = 6;
    public static final int PERIOD_NOT_OK = 1;

    public static final int MOVEMENT_UPDATED = 7;
    public static final int MOVEMENT_ADDED = 8;
    public static final int MOVEMENT_NOT_OK = 2;

    public static final int TYPE_UPDATED = 9;
    public static final int TYPE_ADDED = 10;
    public static final int TYPE_NOT_OK = 3;

    public static final int NOT_OK = 4;

    ArtPeriodRepository artPeriodRepository;
    MovementRepository movementRepository;
    TypeRepository typeRepository;
    ThingsRepository thingsRepository;

    private LiveData<List<ArtPeriod>> allArtPeriodList;
    private MutableLiveData<Integer> liveDataId;

    private int id;
    private int idOfParent;
    private int typeOfItemChosen;
    private int spinnerTypeSelection;
    private int spinnerParentSelection;

    //----------------------------------------------------------------------------------------------
    //constructor

    public AddEditThingViewModel(@NonNull Application application) {
        super(application);
        artPeriodRepository = new ArtPeriodRepository(application);
        movementRepository = new MovementRepository(application);
        typeRepository = new TypeRepository(application);
        allArtPeriodList = artPeriodRepository.getAllArtPeriods();
        thingsRepository = new ThingsRepository(application);
        liveDataId = thingsRepository.getInsertedIdLiveData();
        spinnerParentSelection = -1;
        spinnerTypeSelection = -1;
    }

    //----------------------------------------------------------------------------------------------

    //getters setters:
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOfParent() { return idOfParent; }
    public void setIdOfParent(int idOfParent) { this.idOfParent = idOfParent; }

    public int getTypeOfItemChosen() { return typeOfItemChosen; }
    public void setTypeOfItemChosen(int typeOfItemChosen) { this.typeOfItemChosen = typeOfItemChosen; }

    public int getSpinnerTypeSelection() { return spinnerTypeSelection; }
    public void setSpinnerTypeSelection(int spinnerTypeSelection) { this.spinnerTypeSelection = spinnerTypeSelection; }

    public int getSpinnerParentSelection() { return spinnerParentSelection; }
    public void setSpinnerParentSelection(int spinnerParentSelection) { this.spinnerParentSelection = spinnerParentSelection; }

    //----------------------------------------------------------------------------------------------
    //Live Data

    public LiveData<List<ArtPeriod>> getAllArtPeriods() {
        return allArtPeriodList;
    }

    public MutableLiveData<Integer> getLiveDataId() {
        return liveDataId;
    }


    //----------------------------------------------------------------------------------------------

    public void bindOnInsertedIdChanged(LifecycleOwner lifecycleOwner) {
        liveDataId.observe(lifecycleOwner, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                id = integer;
            }
        });
    }

    //inserts made on ThingRepository. The difference between adding here
    // and on each other repositories id that this one updates livedata on id of inserted item:
    public void insertArtPeriod(String name, String dating) {
        thingsRepository.insertArtPeriod(name, dating);
    }

    public void insertMovement(String name, String dating, String location, int artPeriodId) {
        thingsRepository.insertMovement(name, dating, location, artPeriodId);
    }

    public void insertType(String name) {
        thingsRepository.insertType(name);
    }


    public Result addEditThings(String name, String dating, String location) {
        Result result = new Result();

        switch (typeOfItemChosen) {
            case (CHOSEN_ART_PERIOD):
                if (name.trim().equals("") || dating.trim().equals("")) {
                    result.setResultOfInsertion(PERIOD_NOT_OK);
                    result.setToast("Please insert name and dating");
                    return result;
                }
                if (id != 0) {
                    artPeriodRepository.updateArtPeriod(id, name, dating);
                    result.setResultOfInsertion(PERIOD_UPDATED);
                    result.setToast("Art Period updated");
                } else {
                    insertArtPeriod(name, dating);
                    result.setResultOfInsertion(PERIOD_ADDED);
                    result.setToast("New Art Period added");
                }
                return result;

            case (CHOSEN_MOVEMENT):
                if (name.trim().equals("") || dating.trim().equals("") || location.trim().equals("")) {
                    result.setResultOfInsertion(MOVEMENT_NOT_OK);
                    result.setToast("Please insert name, dating and location");
                    return result;
                }
                if (id != 0) {
                    movementRepository.updateMovement(id, name, dating, location, idOfParent);
                    result.setResultOfInsertion(MOVEMENT_UPDATED);
                    result.setToast("Movement updated");
                } else {
                    insertMovement(name, dating, location, idOfParent);
                    result.setResultOfInsertion(MOVEMENT_ADDED);
                    result.setToast("New Movement added");
                }
                return result;

            case (CHOSEN_TYPE):
                if (name.trim().equals("")) {
                    result.setResultOfInsertion(TYPE_NOT_OK);
                    result.setToast("Please insert name");
                    return result;
                }
                if (id != 0) {
                    typeRepository.updateType(id, name);
                    result.setResultOfInsertion(TYPE_UPDATED);
                    result.setToast("Type updated");
                } else {
                    insertType(name);
                    result.setResultOfInsertion(TYPE_ADDED);
                    result.setToast("New Type added");
                }
                return result;

            default:
                result.setResultOfInsertion(NOT_OK);
                result.setToast("Cannot add");
                return result;
        }

    }

    public Result deleteThing() {
        Result result = new Result();
        switch (typeOfItemChosen) {
            case (CHOSEN_ART_PERIOD):
                if (id != 0) {
                    artPeriodRepository.deleteArtPeriodAndItsChildren(id);
                    result.setToast("Art Period deleted (along with all its pictures)");
                }
                return result;

            case (CHOSEN_MOVEMENT):
                if (id != 0) {
                    movementRepository.deleteMovementAndItsChildren(id);
                    result.setResultOfInsertion(MOVEMENT_UPDATED);
                    result.setToast("Movement deleted (along with all its pictures)");
                }
                return result;

            case (CHOSEN_TYPE):
                if (id != 0) {
                    typeRepository.deleteTypeAndItsChildren(id);
                    result.setResultOfInsertion(TYPE_UPDATED);
                    result.setToast("Type deleted (along with all its pictures)");
                }
                return result;

            default:
                result.setResultOfInsertion(NOT_OK);
                result.setToast("Cannot delete");
                return result;
        }
    }
}
