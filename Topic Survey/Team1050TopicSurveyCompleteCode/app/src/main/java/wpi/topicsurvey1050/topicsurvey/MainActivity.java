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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

    public void encrypt(View view){
        //get the text and key from the view
        EditText text = findViewById(R.id.messageText);
        String message = text.getText().toString();

        EditText keyText = findViewById(R.id.keyField);
        String keyString = keyText.getText().toString();

        try {
            if(keyString.length() != 16){
                return;//cannot encrypt without a keyString of this length
            }

            SecretKey key = new SecretKeySpec(keyString.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] output = cipher.doFinal(message.getBytes());

            //we need to do this because when we send teh SMS message, it must be
            // converted into a string and that messes up the encryption if it stays as a byte[] when sent
            String result = byte2hex(output);

            text.setText(result);
        }
        catch(Exception e){
            Log.d("TOPICSURVEY", "Exception: " + e);
        }
    }

    public void decrypt(View view) {
        //get text to decrypt
        TextView text = findViewById(R.id.receivedMessage);
        String message = text.getText().toString();

        //get the key
        TextView keyText = findViewById(R.id.keyField);
        String keyStr = keyText.getText().toString();

        try {
            //get the actual key

            if (keyStr.length() != 16) {
                return;
            }

            SecretKey key = new SecretKeySpec(keyStr.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] bytes = hex2byte(message.getBytes());//have to convert it back, now that we need the byte[] again
            byte[] decr = cipher.doFinal(bytes);

            String decrypted = new String(decr);
            text.setText(decrypted);

        } catch (Exception e) {
            Log.d("TOPICSURVEY", "Exception: " + e);
        }
    }

    //used to convert a byte[] into a string of hexidecimal characters representing the bytes
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs += ("0" + stmp);
            else
                hs += stmp;
        }
        return hs.toUpperCase();
    }

    //used to convert hexidecimal characters back into their original bytes
    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("hello");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}

