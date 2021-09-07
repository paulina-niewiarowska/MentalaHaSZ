package com.pniew.mentalahasz.web;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.repository.ArtPeriodRepository;
import com.pniew.mentalahasz.repository.MovementRepository;
import com.pniew.mentalahasz.repository.PictureRepository;
import com.pniew.mentalahasz.repository.TypeRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.pniew.mentalahasz.utils.RealPathUtil.getRealPath;

public class PackDownloadReceiver extends BroadcastReceiver {

    PictureRepository pictureRepository;
    MovementRepository movementRepository;
    ArtPeriodRepository artPeriodRepository;
    TypeRepository typeRepository;


    @Override
    public void onReceive(Context context, Intent intent) {

        Application application = (Application) context.getApplicationContext();
        pictureRepository = new PictureRepository(application);
        movementRepository = new MovementRepository(application);
        artPeriodRepository = new ArtPeriodRepository(application);
        typeRepository = new TypeRepository(application);

        Toast.makeText(context, "Download completed. Unpacking...", Toast.LENGTH_LONG).show();

        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadManager manager = context.getSystemService(DownloadManager.class);
        Uri uri = manager.getUriForDownloadedFile(downloadId);
        String downloadedPackagePath = getRealPath(context, uri);

        File destinationPath = context.getFilesDir();
        File file = new File(destinationPath.getPath()+"/images");
        file.mkdir();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ZipFile zipFile = new ZipFile(downloadedPackagePath);

                    // package with data about movements and art periods and types ===================================
                    JSONObject packInfo = getJsonObjectFromZipEntry(zipFile, zipFile.getEntry("pack.json"));
                    JSONArray artPeriods = packInfo.getJSONArray("artPeriods");
                    for( int i = 0; i < artPeriods.length(); i++) {
                        JSONObject artPeriodObject = artPeriods.getJSONObject(i);
                        ArtPeriod artPeriod = artPeriodRepository.getArtPeriodByItsName(artPeriodObject.getString("name"));
                        if (artPeriod == null) {
                            artPeriod = new ArtPeriod(artPeriodObject.getString("name"), artPeriodObject.getString("dating"));
                            artPeriod.setArtPeriodFunFact(artPeriodObject.optString("funFact"));
                            int id = artPeriodRepository.insertNewArtPeriodButSynchronicznie(artPeriod);
                            artPeriod.setArtPeriodId(id);
                        }

                        JSONArray movementsArray = artPeriodObject.optJSONArray("movements");
                        if(movementsArray != null) {
                            for (int j = 0; j < movementsArray.length(); j++) {
                                JSONObject movementObject = movementsArray.getJSONObject(j);
                                Movement movement = movementRepository.getMovementByItsName(movementObject.getString("name"));
                                if (movement == null) {
                                    movement = new Movement(movementObject.getString("name"), movementObject.getString("dating"), movementObject.getString("location"));
                                    movement.setMovementArtPeriod(artPeriod.getArtPeriodId());
                                    movement.setMovementFunFact(movementObject.optString("funFact"));
                                    movementRepository.insertNewMovementButSynchronicznie(movement);
                                }
                            }
                        }
                    }

                    zipFile.stream().filter(zipEntry -> zipEntry.getName().contains(".json") && !zipEntry.getName().equals("pack.json")).forEach(zipEntry -> {
                        try {
                            JSONObject imageData = getJsonObjectFromZipEntry(zipFile, zipEntry);
                            ZipEntry image = zipFile.getEntry("assets/" + imageData.getString("imagePath"));
                            String destinationImagePath = destinationPath.getPath() + image.getName().replace("assets", "");
                            Files.copy(zipFile.getInputStream(image), Paths.get(destinationImagePath));
                            Picture picture = new Picture(
                                    destinationImagePath,
                                    imageData.getString("name"),
                                    imageData.getString("author"),
                                    imageData.getString("dating"),
                                    imageData.getString("location")
                                    );
                            picture.setPictureArtPeriod(artPeriodRepository.getArtPeriodByItsName(imageData.getString("artPeriod")).getArtPeriodId());
                            if(imageData.has("movement")) {
                                picture.setPictureMovement(movementRepository.getMovementByItsName(imageData.getString("movement")).getMovementId());
                            }
                            Type type = typeRepository.getTypeByItsName(imageData.getString("type"));
                            int typeId;
                            if(type == null){
                                type = new Type(imageData.getString("type"));
                                typeId = typeRepository.insertNewTypeSynchronicznie(type);
                            } else {
                                typeId = type.getTypeId();
                            }
                            picture.setPictureType(typeId);
                            picture.setPictureFunFact(imageData.optString("funFact"));
                            pictureRepository.insertNewPictureButSynchronicznie(picture);

                        } catch (IOException | JSONException | NullPointerException e) {
                            Log.e(this.getClass().getName(),"Format error in json: " + zipEntry.getName());
                            e.printStackTrace();
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public JSONObject getJsonObjectFromZipEntry(ZipFile zipFile, ZipEntry zipEntry) throws IOException, JSONException {
        InputStream is = zipFile.getInputStream(zipEntry);
        byte[] buffer = new byte[is.available()];
        long read = is.read(buffer);
        is.close();
        return new JSONObject(new String(buffer, StandardCharsets.UTF_8));
    }
}
