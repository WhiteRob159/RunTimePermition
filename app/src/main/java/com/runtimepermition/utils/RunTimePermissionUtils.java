package com.runtimepermition.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class RunTimePermissionUtils {

    private static final String TAG = RunTimePermissionUtils.class.getSimpleName();

    public static final String[] VALUE_PERMISSION_STORAGE_GROUP = {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};

    public static final int VALUE_PERMISSION_CAMERA = 1;
    public static final int VALUE_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    public static final int VALUE_PERMISSION_READ_EXTERNAL_STORAGE = 3;
    public static final int VALUE_PERMISSION_STORAGE = 4;
    public static final int VALUE_PERMISSIONS_GROUP_1 = 5;


    public static final int VALUE_REQUEST_CAMERA = 111;
    public static final int VALUE_REQUEST_READ_WRITE_EXTERNAL_STORAGE = 112;


    public static int checkPermission(Context context, int permissionIndex) {
        int status = PackageManager.PERMISSION_GRANTED;

        if (permissionIndex == VALUE_PERMISSION_CAMERA) {
            status = ContextCompat.checkSelfPermission(context, CAMERA);
        } else if (permissionIndex == VALUE_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            status = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        } else if (permissionIndex == VALUE_PERMISSION_READ_EXTERNAL_STORAGE) {
            status = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        }

        return status;
    }

    public static boolean checkVersionForRunTimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    public static void requestPermission(Activity activity, int permissionIndex) {

        switch (permissionIndex) {

            case VALUE_PERMISSION_CAMERA:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(activity, new String[]{CAMERA}, VALUE_REQUEST_CAMERA);
                }
                break;

        }

    }

    public static void showDialog(final Activity activity, int permissionIndex) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        switch (permissionIndex) {

            case VALUE_PERMISSION_CAMERA :
                builder.setMessage("This app need to access your device camera.. Please allow");
                builder.setTitle("Camera Permission Needed");

                break;

            case VALUE_PERMISSION_STORAGE :
                builder.setMessage("This app need to access your device storage.. Please allow");
                builder.setTitle("Storage Permission Needed");
                break;
        }

        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void requestGroupPermission(Activity activity, int permissionIndex) {

        ArrayList<String> permissionNeeded = new ArrayList<>();

        switch (permissionIndex) {

            case VALUE_PERMISSION_STORAGE:
                for (String permission : VALUE_PERMISSION_STORAGE_GROUP) {
                    // Check permission granted or not
                    if (ContextCompat.checkSelfPermission(activity, permission) !=
                            PackageManager.PERMISSION_GRANTED) {
                        permissionNeeded.add(permission);
                    }
                }
                String[] arrayPermissions = new String[permissionNeeded.size()];
                permissionNeeded.toArray(arrayPermissions);
                for (String ss : arrayPermissions) {
                    Log.d(TAG, "arrayPermissions : " + ss);

                }

                ActivityCompat.requestPermissions(activity, arrayPermissions, VALUE_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
                break;
        }


    }


}
