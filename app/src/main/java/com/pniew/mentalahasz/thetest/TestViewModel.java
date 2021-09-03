package com.pniew.mentalahasz.thetest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.repository.MovementRepository;
import com.pniew.mentalahasz.repository.PictureRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TestViewModel extends AndroidViewModel {

    private final ExecutorService executorService;
    private ArtPeriodRepository artPeriodRepository;
    private MovementRepository movementRepository;
    private PictureRepository pictureRepository;

    private LiveData<List<ArtPeriod>> allArtPeriods;


    public TestViewModel(@NonNull Application application) {
        super(application);
        executorService = Executors.newSingleThreadExecutor();
        movementRepository = new MovementRepository(application);
        artPeriodRepository = new ArtPeriodRepository(application);
        allArtPeriods = artPeriodRepository.getAllArtPeriods();
        pictureRepository = new PictureRepository(application);
    }

    public LiveData<List<Movement>> getMovements(int id){ return movementRepository.getMovementListByArtPeriodId(id); }
    public LiveData<List<ArtPeriod>> getArtPeriods(){ return allArtPeriods; }

    public LiveData<List<Picture>> getPictureListByArtPeriodId(int id) {
        return pictureRepository.getPicturesOfArtPeriod(id);
    }
    public LiveData<List<Picture>> getPictureListByMovementId(int id) {
        return pictureRepository.getPicturesOfMovement(id);
    }

    public List<Integer> getPictureIdsByArtPeriodOrMovementIds(List<Integer> artPeriodIds, List<Integer> movementIds) {
        return pictureRepository.getPictureListByArtPeriodIdOrMovementIdSync(artPeriodIds, movementIds).stream().map(Picture::getId).collect(Collectors.toList());
    }

    public Executor getBackgroundExecutor() {
        return executorService;
    }
}
