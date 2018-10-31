package wpi.hphong.petwar;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Global variables
    int prevNum = 0;
    int curNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView mKittenView = (ImageView) findViewById(R.id.kittenView);
        try {
            InputStream image_stream = getAssets().open("kitten_pictures/kitten_0.jpg");
            mKittenView.setImageBitmap(BitmapFactory.decodeStream(image_stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickGoButton(View v) {
        ImageView mKittenView = (ImageView) findViewById(R.id.kittenView);
        Random rand = new Random();
        prevNum = curNum;

        // Prevent randomizing into same image
        // as curNum is updated
        while (prevNum == curNum) {
            curNum = rand.nextInt(5);
        }

        String imgPath = "kitten_pictures/kitten_" + String.valueOf(curNum) + ".jpg";
        try {
            InputStream image_stream = getAssets().open(imgPath);
            mKittenView.setImageBitmap(BitmapFactory.decodeStream(image_stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
