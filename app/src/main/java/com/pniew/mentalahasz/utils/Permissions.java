package com.pniew.mentalahasz.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pniew.mentalahasz.R;

public class Permissions {

    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 1;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    public static final int PERMISSION_INTERNET = 3;

    private static void showExplanation(Activity activity, String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.HaSZDialogTheme);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(activity, permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private static void requestPermission(Activity activity, String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissionName}, permissionRequestCode);
    }

    public static void showPhoneStatePermission(Activity activity, String permission, int permissionCode, String explanation) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showExplanation(activity, activity.getResources().getString(R.string.permission_needed), explanation, permission, permissionCode);
            }
        } else {
            requestPermission(activity, permission, permissionCode);
        }
    }
}
