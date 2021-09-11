package com.pniew.mentalahasz.module_web;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class WebVievModel extends AndroidViewModel {
    private long prehistoryDownloadId;
    private long classicalDownloadId;
    private long medievalDownloadId;

    private boolean prehistoryStarted;
    private boolean classicalStarted;


    public WebVievModel(@NonNull Application application) {
        super(application);
    }

    public long getPrehistoryDownloadId() {
        return prehistoryDownloadId;
    }

    public void setPrehistoryDownloadId(long prehistoryDownloadId) {
        this.prehistoryDownloadId = prehistoryDownloadId;
    }

    public long getClassicalDownloadId() {
        return classicalDownloadId;
    }

    public void setClassicalDownloadId(long classicalDownloadId) {
        this.classicalDownloadId = classicalDownloadId;
    }

    public long getMedievalDownloadId() {
        return medievalDownloadId;
    }

    public void setMedievalDownloadId(long medievalDownloadId) {
        this.medievalDownloadId = medievalDownloadId;
    }

    public boolean isPrehistoryStarted() {
        return prehistoryStarted;
    }

    public void setPrehistoryStarted(boolean prehistoryStarted) {
        this.prehistoryStarted = prehistoryStarted;
    }

    public boolean isClassicalStarted() {
        return classicalStarted;
    }

    public void setClassicalStarted(boolean classicalStarted) {
        this.classicalStarted = classicalStarted;
    }
}
