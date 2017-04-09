package gigster.com.holdsum.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gigster.com.holdsum.R;

/**
 * Created by tpaczesny on 2016-09-12.
 */
public class TakePhotoHelper {

    private final Activity mActivity;
    private int mRequestId;
    private String mRequestName;
    private PhotoReadyListener mListener;
    private String mTempPhotoPath;


    private static final int PERMISSION_REQUEST = 0;

    public TakePhotoHelper(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * Initiates process of taking photo. Photo will be stored
     * @param requestId internal id used to identify this photo request
     * @param requestName name used in photo filename
     */
    public void takePhoto(int requestId, String requestName, PhotoReadyListener listener) {
        this.mRequestId = requestId;
        this.mRequestName = requestName;
        this.mListener = listener;
        this.mTempPhotoPath = null;
        dispatchTakePictureIntent();
    }


    private File createImageFile(int paystubOrId) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_"+mRequestName;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public boolean checkPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionList.isEmpty()) {
            return true;
        }

        ActivityCompat.requestPermissions(mActivity,
                permissionList.toArray(new String[permissionList.size()]),
                PERMISSION_REQUEST);
        return false;
    }

    private void dispatchTakePictureIntent() {

        if (!checkPermissions()) {
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(mRequestId);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(mActivity, "Failed to create picture file.", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                mActivity.startActivityForResult(takePictureIntent, mRequestId);
            }
        }
    }

    /**
     * Must be called from activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allsuccess = true;

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults.length < 1 || grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allsuccess = false;
                break;
            }
        }

        if (allsuccess) {
            Toast.makeText(mActivity, R.string.permission_granted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, R.string.permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Must be called from activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == mRequestId) {
            downSizePhoto(mTempPhotoPath);
            mListener.photoReady(mTempPhotoPath);
        }
    }

    private void downSizePhoto(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null)
            return;
        Bitmap scaled;
        try {
            scaled = Bitmap.createScaledBitmap(bitmap, 1536, 2048, false);
        } catch (OutOfMemoryError oom) {
            return;
        }


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            scaled.compress(Bitmap.CompressFormat.JPEG, 95, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
            scaled.recycle();
            Utils.closeQuietly(out);
        }
    }

    public interface PhotoReadyListener {
        void photoReady(String filename);
    }
}
