package com.example.zhu.test4;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;

public class ImageAccess extends AppCompatActivity implements View.OnClickListener {
    /**
     * To load local image
     */

    private ImageView imageView;
    public TextView myImagePath;
    private Button myButton;
    public String realPathFromUri;
    public static final int REQUEST_PICK_IMAGE = 11101;
    public  String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private File localFile;
    private FileInputStream localStream = null;
    private Bitmap bitmap = null;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_access);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=(ImageView)findViewById(R.id.imageView);
        myImagePath=(TextView)findViewById(R.id.myPath);
        myButton=(Button) findViewById(R.id.buttonForImage);
        myButton.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClick(View v) {
// Do something interesting when the button is clicked

        ActivityCompat.requestPermissions(ImageAccess.this, mPermissionList, 100);

        setImageView(realPathFromUri);
    }

    private void setImageView(String realPathFromUri) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    getImage();
                } else {
                    Toast.makeText(this, "need permission", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getImage(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                         realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                         myImagePath.setText(data.getData().toString());
//                         imageView.setImageURI(realPathFromUri);
                    } else {
                        Toast.makeText(this, "something wrong with the image , please re-select "
                                , Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    }


}
