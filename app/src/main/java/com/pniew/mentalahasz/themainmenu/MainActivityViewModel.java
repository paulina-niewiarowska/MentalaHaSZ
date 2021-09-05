package com.pniew.mentalahasz.themainmenu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.Things;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.repository.MovementRepository;
import com.pniew.mentalahasz.repository.ThingsRepository;
import com.pniew.mentalahasz.repository.TypeRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private final ArtPeriodRepository artPeriodRepository;
    private final TypeRepository typeRepository;

    private LiveData<List<ArtPeriod>> allArtPeriods;
    private LiveData<List<Type>> allTypes;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        artPeriodRepository = new ArtPeriodRepository(application);
        typeRepository = new TypeRepository(application);

        allArtPeriods = artPeriodRepository.getAllArtPeriods();
        allTypes = typeRepository.getAllTypes();

    }

    public LiveData<List<ArtPeriod>> getAllArtPeriods() {
        return allArtPeriods;
    }
    public LiveData<List<Type>> getAllTypes() {
        return allTypes;
    }

}
