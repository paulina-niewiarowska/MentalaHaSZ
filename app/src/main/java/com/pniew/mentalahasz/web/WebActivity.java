package com.pniew.mentalahasz.web;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pniew.mentalahasz.R;

public class WebActivity extends AppCompatActivity {

    DownloadManager downloadManager;
    public static final int PREHISTORY_LOADER = 10;
    public static final int CLASSICAL_ART_LOADER = 11;
    public static final int MEDIEVAL_LOADER = 12;
    private int which;
    public static boolean prehistoryStarted;
    public static boolean classicalStarted;
    public static WebActivity Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Instance = this;
        if(prehistoryStarted){
            findViewById(R.id.prehistory_loading).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.prehistory_loading).setVisibility(View.GONE);
        }
        if(classicalStarted){
            findViewById(R.id.classical_loading).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.classical_loading).setVisibility(View.GONE);
        }

        downloadManager = getSystemService(DownloadManager.class);


    } // =========== end of onCreate() ===================================================================================

    private void requestDownload(String packageName, String downloadUri){
        Uri uri = Uri.parse(downloadUri);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(packageName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, packageName);
        downloadManager.enqueue(request);
    }

    public void downloadPrehistoryPackage(View view) {
        findViewById(R.id.prehistory_loading).setVisibility(View.VISIBLE);
        prehistoryStarted = true;
        which = PREHISTORY_LOADER;
        Toast.makeText(this, "Download request sent.", Toast.LENGTH_LONG).show();
        requestDownload("Prehistoria", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218061&authkey=AOXWs4Ul7_Dv4D0");
    }

    public void downloadClassicalPackage(View view) {
        findViewById(R.id.classical_loading).setVisibility(View.VISIBLE);
        classicalStarted = true;
        which = CLASSICAL_ART_LOADER;
        Toast.makeText(this, "Download request sent.", Toast.LENGTH_LONG).show();
        requestDownload("Starożytność", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218062&authkey=AJaWmwXbOA8zxMA");
    }

    public void downloadMiddleAgesPackage(View view) {
        //findViewById(R.id.middle_loading).setVisibility(View.VISIBLE);
        //which = MEDIEVAL_LOADER;
        Toast.makeText(this, "Package is not ready yet. Please try again later.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            finish();
        }
        return true;
    }

    public void stopTheLoader(){
        switch (which){
            case PREHISTORY_LOADER:
                findViewById(R.id.prehistory_loading).setVisibility(View.GONE);
                prehistoryStarted = false;
                break;
            case CLASSICAL_ART_LOADER:
                findViewById(R.id.classical_loading).setVisibility(View.GONE);
                classicalStarted = false;
                break;
            case MEDIEVAL_LOADER:
                findViewById(R.id.middle_loading).setVisibility(View.GONE);
                prehistoryStarted = false;
                break;
        }
    }

}