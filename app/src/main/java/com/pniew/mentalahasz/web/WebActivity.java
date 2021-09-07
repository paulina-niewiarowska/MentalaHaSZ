package com.pniew.mentalahasz.web;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.pniew.mentalahasz.R;

public class WebActivity extends AppCompatActivity {

    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        downloadManager = getSystemService(DownloadManager.class);





    } // =========== end of onCreate() ===================================================================================

    private void requestDownload(String packageName, String downloadUri){
        Uri uri = Uri.parse(downloadUri);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(packageName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, packageName);
        downloadManager.enqueue(request);
    }

    public void downloadTestPackage(View view) {
        requestDownload("Test Pack", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218060&authkey=AC0jO1aH_HZncOI");
    }

    public void downloadPrehistoryPackage(View view) {
        requestDownload("Prehistoria", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218061&authkey=AOXWs4Ul7_Dv4D0");
    }

    public void downloadClassicalPackage(View view) {
        requestDownload("Starożytność", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218062&authkey=AJaWmwXbOA8zxMA");
    }
}