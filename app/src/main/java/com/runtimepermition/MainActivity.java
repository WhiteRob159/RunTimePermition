package com.runtimepermition;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.runtimepermition.utils.RunTimePermissionUtils;

import java.io.File;

import static com.runtimepermition.utils.RunTimePermissionUtils.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mBtnCamera, btnOpenFile;
    private Context mContext;
    private Button mBtnReadWritePermission;

    public static final String ACTION_PERMISSIONS = "com.runtimepermition.action.ACTION_PERMISSIONS";

    public static final int VALUE_STORAGE_PERMISSION = 1;
    public static final int VALUE_CAMERA_PERMISSION = 2;
    public static final String KEY_STORAGE_PERMISSION = "Action_Storage";
    public static final String KEY_CAMERA_PERMISSION = "Action_Camera";

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive() : ");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int flag = 0;
                if (bundle.containsKey(KEY_CAMERA_PERMISSION)) {
                    flag = bundle.getInt(KEY_CAMERA_PERMISSION);

                } else if (bundle.containsKey(KEY_STORAGE_PERMISSION)) {
                    flag = bundle.getInt(KEY_STORAGE_PERMISSION);
                }
                Log.d(TAG, "onReceive() flag : " + flag);
                if (flag == VALUE_CAMERA_PERMISSION) {
                    cameraClick();
                } else if (flag == VALUE_STORAGE_PERMISSION) {
                    mBtnReadWritePermission();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PERMISSIONS);
        registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        mBtnCamera = (Button) findViewById(R.id.btnCamera);
        btnOpenFile = (Button) findViewById(R.id.btnOpenFile);
        mBtnReadWritePermission = (Button) findViewById(R.id.btnReadWritePermission);


        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(KEY_CAMERA_PERMISSION, VALUE_CAMERA_PERMISSION);
                i.setAction(ACTION_PERMISSIONS);
                sendBroadcast(i);
//                cameraClick();
            }
        });
        btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory()
                        + File.separator
                        + "LPL_Investor" + File.separator + "Statement" ,
                        "Consolidated Statement - Jul 2018.pdf");
//                Uri path = Uri.fromFile(file);

                Uri path = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file);

                Log.d(TAG, "path : " + path);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/pdf");

                    startActivity(pdfOpenintent);

            }
        });
        mBtnReadWritePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(KEY_STORAGE_PERMISSION, VALUE_STORAGE_PERMISSION);
                i.setAction(ACTION_PERMISSIONS);
                sendBroadcast(i);
//                mBtnReadWritePermission();
            }
        });
    }

    private void mBtnReadWritePermission() {
        // Check android version
        if (RunTimePermissionUtils.checkVersionForRunTimePermission()) {
            // Check android permission is granted or not
            if (RunTimePermissionUtils.checkPermission(mContext,
                    VALUE_PERMISSION_READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || RunTimePermissionUtils.checkPermission(mContext,
                    VALUE_PERMISSION_WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // launch permission dialog
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        || !ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Log.d(TAG, "if : ");
                    RunTimePermissionUtils.requestGroupPermission(this, VALUE_PERMISSION_STORAGE);
                } else { // Launch permission explanation dialog.
                    Log.d(TAG, "else : ");
                    RunTimePermissionUtils.showDialog(this, VALUE_PERMISSION_STORAGE);
                }
            } else {
                launchSecondAct();
            }
        } else {
            launchSecondAct();
        }


    }

    private void cameraClick() {
        // Check android version
        if (RunTimePermissionUtils.checkVersionForRunTimePermission()) {
            // Check android permission is granted or not
            if (RunTimePermissionUtils.checkPermission(mContext, VALUE_PERMISSION_CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // launch permission dialog
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    RunTimePermissionUtils.requestPermission(this, VALUE_PERMISSION_CAMERA);
                } else { // Launch permission explanation dialog.
                    RunTimePermissionUtils.showDialog(this, VALUE_PERMISSION_CAMERA);
                }
            } else {
                launchSecondAct();
            }
        } else {
            launchSecondAct();
        }
    }

    private void launchSecondAct() {
        Intent i = new Intent(mContext, SecondeActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "requestCode : " + requestCode + ", permissions : " + permissions.toString()
                + ", grantResults : " + grantResults.toString());

        switch (requestCode) {

            case VALUE_REQUEST_CAMERA:

                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "Camera Permission is Required.", Toast.LENGTH_LONG).show();

                }

                break;

            case VALUE_REQUEST_READ_WRITE_EXTERNAL_STORAGE:

                if (grantResults != null) {
                    if (grantResults.length > 0) {
                        boolean status = true;
                        for (int i = 0; i < permissions.length; i++) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                status = false;
                            }
                        }
                        if (!status) {
                            Toast.makeText(mContext, "Storage Permission is required.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
