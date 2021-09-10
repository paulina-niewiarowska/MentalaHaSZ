package com.pniew.mentalahasz.intermodule_single_picture_activity;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.repository.PictureRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LearnShowAddViewModel extends AndroidViewModel {

    private int iWantTo;
    private String path;
    private int pictureId;
    private PictureRepository pictureRepository;
    private Bitmap bitmap;
    private List<Integer> picturesIdsList;
    private LiveData<Picture> pictureLiveData;
    private int calledBy;
    private int idOfCallingThing;
    private int parentPeriodId;
    private String triviaText;

    private int points;
    private int maxPoints;

    private List<Integer> picturesIdsListReserve;


    public LearnShowAddViewModel(@NonNull Application application) {
        super(application);
        pictureRepository = new PictureRepository(application);
    }

    // setters getters


    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getMaxPoints() { return maxPoints; }
    public void setMaxPoints(int maxPoints) { this.maxPoints = maxPoints; }

    public int getParentPeriodId() { return parentPeriodId; }
    public void setParentPeriodId(int parentPeriodId) { this.parentPeriodId = parentPeriodId; }

    public int getIWantTo() { return iWantTo; }
    public void setIWantTo(int iWantTo) { this.iWantTo = iWantTo; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public int getPictureId() { return pictureId; }
    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
        setPictureById();
    }

    public int getCalledBy() { return calledBy; }
    public void setCalledBy(int calledBy) { this.calledBy = calledBy; }

    public String getTriviaText() { return triviaText; }
    public void setTriviaText(String triviaText) { this.triviaText = triviaText; }

    public int getIdOfCallingThing() { return idOfCallingThing; }
    public void setIdOfCallingThing(int idOfCallingThing) { this.idOfCallingThing = idOfCallingThing; }

    public void setPictureById(){
        if(pictureId != 0) {
            pictureLiveData = null;
            pictureLiveData = pictureRepository.getPictureById(pictureId);
        }
    }

    public LiveData<Picture> getPictureById() {
        return pictureLiveData;
    }

    public Bitmap getPictureBitmap() {
        return bitmap;
    }
    public void setPictureBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }


    public void setPicturesIdsArray(int[] picturesIdsArray) {
        picturesIdsList = Arrays.stream(picturesIdsArray).boxed().collect(Collectors.toList());
        picturesIdsListReserve = new ArrayList<>(picturesIdsList);
        maxPoints = picturesIdsList.size();
    }


    public void updateArray(Integer idToThrowAway){
        picturesIdsList.remove(idToThrowAway);
    }

    public boolean isArrayFinished(){
        return picturesIdsList.size() == 1;
    }


    public void getOneBefore(Context context){
        int pId;
        if (picturesIdsList.size() == 0) {
            pId = -1;
            setPictureId(pId);
            return;
        }
        for (int i = 0; i < picturesIdsList.size(); i++) {
            if (picturesIdsList.get(i)==pictureId) {
                if(i == 0) {
                    Collections.shuffle(picturesIdsList);
                    Toast.makeText(context, "Reshuffling...", Toast.LENGTH_SHORT).show();
                    pId = picturesIdsList.get(picturesIdsList.size()-1);
                } else {
                    pId = picturesIdsList.get(i - 1);
                }
                setPictureId(pId);
                return;
            }
        }
    }

    public void getOneAfter(Context context) {
        int pId;
        if (picturesIdsList.size() == 0) {
            pId = -1;
            setPictureId(pId);
            return;
        }
        for (int i = 0; i < picturesIdsList.size(); i++) {
            if (picturesIdsList.get(i) == pictureId) {
                if (i == picturesIdsList.size() - 1) {
                    Collections.shuffle(picturesIdsList);
                    Toast.makeText(context, "Reshuffling...", Toast.LENGTH_SHORT).show();
                    pId = picturesIdsList.get(0);
                } else {
                    pId = picturesIdsList.get(i + 1);
                }
                setPictureId(pId);
                return;
            }
        }
    }

    public void setFirstToTest() {
        if(!picturesIdsList.isEmpty()) {
            pictureId = picturesIdsList.get(0);
        }
    }

    public void reload() {
        Collections.shuffle(picturesIdsListReserve);
        picturesIdsList = new ArrayList<>(picturesIdsListReserve);
        points = 0;
        maxPoints = picturesIdsList.size();
    }

    public void addPoint(boolean gotPoint) {
        if(gotPoint) {
            points++;
        }
    }

    public void setPictureFunFact(String funFact){
        pictureRepository.setPictureFunFact(pictureId, funFact);
    }
}
