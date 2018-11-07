package wpi.hphong.cameratutorial;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;
import com.github.nisrulz.sensey.FlipDetector.FlipListener;
import com.github.nisrulz.sensey.LightDetector.LightListener;
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements OnCheckedChangeListener, ShakeListener, FlipListener,
        LightListener, OrientationListener, ProximityListener {
    private int REQUEST_IMAGE_CAPTURE = 100;
    private String mCurrentPhotoPath = "";
    private String mCurrentPhotoName = "";
    private String formattedDate = "";
    private String formattedTime = "";

    private Switch switch1, switch2, switch3, switch4, switch5;
    private int shakeVal = -1;
    private int flipVal = -1;
    private int orientVal = -1;
    private int proxVal = -1;
    private int lightVal = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Sensey
        Sensey.getInstance().init(this);

        // Initialize listener to all switches
        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
        switch1.setChecked(false);

        switch2 = findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener(this);
        switch2.setChecked(false);

        switch3 = findViewById(R.id.switch3);
        switch3.setOnCheckedChangeListener(this);
        switch3.setChecked(false);

        switch4 = findViewById(R.id.switch4);
        switch4.setOnCheckedChangeListener(this);
        switch4.setChecked(false);

        switch5 = findViewById(R.id.switch5);
        switch5.setOnCheckedChangeListener(this);
        switch5.setChecked(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton switchBtn, boolean isChecked) {
        switch (switchBtn.getId()) {
            case R.id.switch1:
                if (isChecked) {    // SHAKE
                    shakeVal = 0;
                    Sensey.getInstance().startShakeDetection(10, 2000, this);
                } else {
                    Sensey.getInstance().stopShakeDetection(this);
                    shakeVal = -1;
                }
                break;
            case R.id.switch2:
                if (isChecked) {    // FLIP
                    flipVal = 1;
                    Sensey.getInstance().startFlipDetection(this);
                } else {
                    Sensey.getInstance().stopFlipDetection(this);
                    flipVal = -1;
                }
                break;
            case R.id.switch3:      // ORIENTATION
                if (isChecked) {
                    orientVal = 0;
                    Sensey.getInstance().startOrientationDetection(this);
                } else {
                    Sensey.getInstance().stopOrientationDetection(this);
                    orientVal = -1;
                }
                break;
            case R.id.switch4:      // PROXIMITY
                if (isChecked) {
                    proxVal = 0;
                    Sensey.getInstance().startProximityDetection(this);
                } else {
                    Sensey.getInstance().stopProximityDetection(this);
                    proxVal = -1;
                }
                break;
            case R.id.switch5:      // LIGHT
                if (isChecked) {
                    lightVal = 0;
                    Sensey.getInstance().startLightDetection(10, this);
                } else {
                    Sensey.getInstance().stopLightDetection(this);
                    lightVal = -1;
                }
                break;
            default:                // DO NOTHING
                break;
        }
    }

    // SHAKE
    @Override
    public void onShakeDetected() {
        // Shake detected, do something
        shakeVal = 1;
    }

    @Override
    public void onShakeStopped() {
        // Shake stopped, do something
        shakeVal = 0;
    }

    // FLIP
    @Override
    public void onFaceDown() {
        // Device Facing down
        flipVal = 0;
    }

    @Override
    public void onFaceUp() {
        // Device Facing up
        flipVal = 1;
    }

    // ORIENTATION
    @Override
    public void onTopSideUp() {
        // Top side of device is up
        orientVal = 0;
    }

    @Override
    public void onBottomSideUp() {
        // Bottom side of device is up
        orientVal = 1;
    }

    @Override
    public void onRightSideUp() {
        // Right side of device is up
        orientVal = 2;
    }

    @Override
    public void onLeftSideUp() {
        // Left side of device is up
        orientVal = 3;
    }

    // PROXIMITY
    @Override
    public void onNear() {
        // Near to device
        proxVal = 0;
    }

    @Override
    public void onFar() {
        // Far from device
        proxVal = 1;
    }

    // LIGHT
    @Override
    public void onDark() {
        // Dark
        lightVal = 0;
    }

    @Override
    public void onLight() {
        // Not Dark
        lightVal = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop Sensey
        Sensey.getInstance().stop();
    }

    public void onClickCameraButton(View v) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null){
            // Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "wpi.hphong.cameratutorial.fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView cameraImg = (ImageView) findViewById(R.id.cameraImageView);
        TextView imgDir = (TextView) findViewById(R.id.imgText);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cameraImg.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            imgDir.setText(mCurrentPhotoName);

            // Additional information attached as part of app extension
            // Date and Time
            TextView dateTxt = (TextView) findViewById(R.id.infoDate);
            dateTxt.setText(formattedDate);
            TextView timeTxt = (TextView) findViewById(R.id.infoTime);
            timeTxt.setText(formattedTime);
            // Shake
            TextView shakeInfo = (TextView) findViewById(R.id.infoShake);
            switch (shakeVal) {
                case -1:
                    shakeInfo.setText(R.string.unknown);
                    break;
                case 0:
                    shakeInfo.setText("Shake Stopped!");
                    break;
                case 1:
                    shakeInfo.setText("Shake Detected!");
                    break;
            }

            // Flip
            TextView flipInfo = (TextView) findViewById(R.id.infoFlip);
            switch (flipVal) {
                case -1:
                    flipInfo.setText(R.string.unknown);
                    break;
                case 0:
                    flipInfo.setText("Face Down!");
                    break;
                case 1:
                    flipInfo.setText("Face Up!");
                    break;
            }

            // Orientation
            TextView orientInfo = (TextView) findViewById(R.id.infoOrientation);
            switch (orientVal) {
                case -1:
                    orientInfo.setText(R.string.unknown);
                    break;
                case 0:
                    orientInfo.setText("Top Side UP!");
                    break;
                case 1:
                    orientInfo.setText("Bottom Side UP!");
                    break;
                case 2:
                    orientInfo.setText("Right Side UP!");
                    break;
                case 3:
                    orientInfo.setText("Left Side UP!");
                    break;
            }

            // Proximity
            TextView proxInfo = (TextView) findViewById(R.id.infoProximity);
            switch (proxVal) {
                case -1:
                    proxInfo.setText(R.string.unknown);
                    break;
                case 0:
                    proxInfo.setText("Near!");
                    break;
                case 1:
                    proxInfo.setText("Far!");
                    break;
            }

            // Light
            TextView lightInfo = (TextView) findViewById(R.id.infoLight);
            switch (lightVal) {
                case -1:
                    lightInfo.setText(R.string.unknown);
                    break;
                case 0:
                    lightInfo.setText("Dark!");
                    break;
                case 1:
                    lightInfo.setText("Bright!");
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        formattedDate = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault()).format(new Date());
        formattedTime = new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        mCurrentPhotoName = image.getName();
        return image;
    }
}

