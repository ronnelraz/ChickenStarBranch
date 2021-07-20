package com.charoenpokhandfoodph;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.charoenpokhandfoodph.Login;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import static com.charoenpokhandfoodph.function.animIntent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        function.getInstance(this);


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.VIBRATE
//                        Manifest.permission.READ_SMS,
//                        Manifest.permission.RECEIVE_SMS,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.CALL_PHONE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            new Handler().postDelayed(() -> {

                                if(function.ifLogin().equals("true")){

                                        Intent i = new Intent(getApplicationContext(), Home.class);
                                        startActivity(i);
                                        animIntent(MainActivity.this, config.ltr);

                                }
                                else{
                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,(findViewById(R.id.logo)),"logo");
                                    startActivity(i,option.toBundle());

                                }


//                                    if(isLogin.equals("true")){
////                                        function.intent(getApplicationContext(),Home.class);
////                                        CustomIntent.customType(getApplicationContext(),config.ANIM);
//                                        Intent i = new Intent(getApplicationContext(), newHome.class);
//                                        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,(findViewById(R.id.logo)),"logo");
//                                        startActivity(i,option.toBundle());
//                                    }
//                                    else{
//                                        Intent i = new Intent(getApplicationContext(), Login.class);
//                                        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,(findViewById(R.id.logo)),"logo");
//                                        startActivity(i,option.toBundle());
//                                    }

                            },3000); // 3 second splash Screen

                        }

                        // check for permanent denial of any permission
                        else if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })

                .onSameThread()
                .check();




    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}