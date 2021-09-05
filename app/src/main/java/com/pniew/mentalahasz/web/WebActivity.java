package com.pniew.mentalahasz.web;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

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


}