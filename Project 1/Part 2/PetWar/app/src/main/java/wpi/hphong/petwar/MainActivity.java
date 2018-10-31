package wpi.hphong.petwar;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

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
        TextView name = (TextView)findViewById(R.id.kittenName);
        try {
            InputStream image_stream = getAssets().open("kitten_pictures/kitten_0.jpg");
            mKittenView.setImageBitmap(BitmapFactory.decodeStream(image_stream));
            name.setText(R.string.cat_name_0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickRandomButton(View v) {
        Random rand = new Random();
        prevNum = curNum;

        // Prevent randomizing into same image
        // as curNum is updated
        while (prevNum == curNum) {
            curNum = rand.nextInt(5);
        }

        // Update image
        updateKittenInfo();
    }

    public void onClickBackButton(View v) {
        prevNum = curNum;

        if (curNum == 0) curNum = 4;
        else curNum -= 1;

        // Update image
        updateKittenInfo();
    }

    public void onClickNextButton(View v) {
        prevNum = curNum;

        if (curNum == 4) curNum = 0;
        else curNum += 1;

        // Update image
        updateKittenInfo();
    }

    private void updateKittenInfo() {
        // Clear choice
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.clearCheck();

        // Clear text response
        TextView textView = (TextView)findViewById(R.id.fragment_response);
        textView.setText(null);

        ImageView mKittenView = (ImageView) findViewById(R.id.kittenView);
        TextView name = (TextView)findViewById(R.id.kittenName);
        String imgPath = "kitten_pictures/kitten_" + String.valueOf(curNum) + ".jpg";
        try {
            InputStream image_stream = getAssets().open(imgPath);
            mKittenView.setImageBitmap(BitmapFactory.decodeStream(image_stream));
            switch (curNum) {
                case 0:
                    name.setText(R.string.cat_name_0);
                    break;
                case 1:
                    name.setText(R.string.cat_name_1);
                    break;
                case 2:
                    name.setText(R.string.cat_name_2);
                    break;
                case 3:
                    name.setText(R.string.cat_name_3);
                    break;
                case 4:
                    name.setText(R.string.cat_name_4);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
