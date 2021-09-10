package com.pniew.mentalahasz.module_periodization;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.Things;
import com.pniew.mentalahasz.model.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.model.repository.MovementRepository;
import com.pniew.mentalahasz.model.repository.ThingsRepository;
import com.pniew.mentalahasz.model.repository.TypeRepository;

import java.util.List;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class ListOfThingsViewModel extends AndroidViewModel {
    private final LiveData<List<Things>> allThings;
    private final ArtPeriodRepository artPeriodRepository;
    private final MovementRepository movementRepository;
    private final TypeRepository typeRepository;


    public ListOfThingsViewModel(@NonNull Application application) {
        super(application);
        ThingsRepository thingsRepository = new ThingsRepository(application);
        artPeriodRepository = new ArtPeriodRepository(application);
        movementRepository = new MovementRepository(application);
        typeRepository = new TypeRepository(application);

        allThings = thingsRepository.getAllThings();
    }

    public LiveData<List<Things>> getAllThings() {
        return allThings;
    }

    public void deleteThing(Things things){
        String type = things.getObjectType();
        switch (type){
            case ART_PERIOD_STRING:
                artPeriodRepository.deleteArtPeriodAndItsChildren(things.getId());
                break;
            case MOVEMENT_STRING:
                movementRepository.deleteMovementAndItsChildren(things.getId());
                break;
            case TYPE_STRING:
                typeRepository.deleteTypeAndItsChildren(things.getId());
                break;
            default:
                break;
        }
    }
}
