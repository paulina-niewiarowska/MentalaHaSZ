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

import java.util.ArrayList;
import java.util.List;

public class WebActivity extends AppCompatActivity {

    DownloadManager downloadManager;
    public static final int PREHISTORY_LOADER = 10;
    public static final int CLASSICAL_ART_LOADER = 11;
    public static final int MEDIEVAL_LOADER = 12;
    public static boolean prehistoryStarted;
    public static boolean classicalStarted;
    public static WebActivity Instance;

    private long prehistoryDownloadId;
    private long classicalDownloadId;
    private long medievalDownloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Instance = this;
        if (prehistoryStarted) {
            findViewById(R.id.prehistory_loading).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.prehistory_loading).setVisibility(View.GONE);
        }
        if (classicalStarted) {
            findViewById(R.id.classical_loading).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.classical_loading).setVisibility(View.GONE);
        }

        downloadManager = getSystemService(DownloadManager.class);



    } // =========== end of onCreate() ===================================================================================

    private void requestDownload(String packageName, String downloadUri) {
        Uri uri = Uri.parse(downloadUri);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(packageName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, packageName);
        if(packageName.equals("Prehistoria")) {
            prehistoryDownloadId = downloadManager.enqueue(request);
        } else if (packageName.equals("Starożytność")) {
            classicalDownloadId = downloadManager.enqueue(request);
        }
    }

    public void downloadPrehistoryPackage(View view) {

            findViewById(R.id.prehistory_loading).setVisibility(View.VISIBLE);
            prehistoryStarted = true;
            Toast.makeText(this, "Download request sent.", Toast.LENGTH_LONG).show();
            requestDownload("Prehistoria", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218061&authkey=AOXWs4Ul7_Dv4D0");


        }

    public void downloadClassicalPackage(View view) {

            findViewById(R.id.classical_loading).setVisibility(View.VISIBLE);
            classicalStarted = true;
            Toast.makeText(this, "Download request sent.", Toast.LENGTH_LONG).show();
            requestDownload("Starożytność", "https://onedrive.live.com/download?cid=27A94A812CFD3978&resid=27A94A812CFD3978%218062&authkey=AJaWmwXbOA8zxMA");

        }

    public void downloadMiddleAgesPackage(View view) {
        //findViewById(R.id.middle_loading).setVisibility(View.VISIBLE);
        //which.add(MEDIEVAL_LOADER);
        Toast.makeText(this, "Package is not ready yet. Please try again later.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            finish();
        }
        return true;
    }

    public void stopTheLoader(long downloadId) {
        if(downloadId == prehistoryDownloadId){
            findViewById(R.id.prehistory_loading).setVisibility(View.GONE);
            prehistoryStarted = false;
        } else if(downloadId == classicalDownloadId){
            findViewById(R.id.classical_loading).setVisibility(View.GONE);
            classicalStarted = false;
        }

//            case medievalDownloadId:
//                findViewById(R.id.middle_loading).setVisibility(View.GONE);
//                prehistoryStarted = false;
//                which.remove(0);
//                break;
        }


}