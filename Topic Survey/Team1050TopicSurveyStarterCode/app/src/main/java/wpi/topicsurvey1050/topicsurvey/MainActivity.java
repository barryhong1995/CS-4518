package wpi.topicsurvey1050.topicsurvey;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final int SEND_REQ_CODE = 1;
    private final int REC_REQ_CODE = 2;

    //this is how our app receives SMS messages. It gets the contents and sets it to the receivedMessage text field.
    private BroadcastReceiver msgReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            TextView text = findViewById(R.id.receivedMessage);

            if(intent != null) {
                Bundle extras = intent.getExtras();
                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                String msg = "";

                for (int i = 0; i < messages.length; i++) {
                    msg += messages[i].getMessageBody();
                }
                text.setText(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart(){
        super.onStart();

        //get permission to receive SMS messages
        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            //ask permission if we do not have it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REC_REQ_CODE);
        }
        else {
            registerReceiver(msgReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }

    public void sendMessage(View view){
        //send the message to the other emulator
        //check that we have permissions, and get them if we do not
        if(checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //ask permission if we do not have it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_REQ_CODE);
        }
        else {
            //send the message
            EditText text = findViewById(R.id.messageText);
            String message = text.getText().toString();

            EditText number = findViewById(R.id.emulatorNumber);
            String dest = number.getText().toString();

            //send the SMS message
            if(dest.length() > 0 && message.length() > 0) {
                SmsManager.getDefault().sendTextMessage(dest, null, message, null, null);
            }
        }
    }

        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //checks if this is the callback for permission to send or receive SMS messages.
        if(requestCode == SEND_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage(null);
        }
        else if(requestCode == REC_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            registerReceiver(msgReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }
}

