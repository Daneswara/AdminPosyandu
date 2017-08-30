package com.masbie.adminposyandu;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KirimNotifikasiKebeberapa extends AppCompatActivity {

    EditText textSMS, textPenerima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_notifikasi_kebeberapa);
        setTitle("Kirim Notifikasi ke Beberapa");
        Button send = (Button) findViewById(R.id.buttonSend);
        textSMS = (EditText) findViewById(R.id.editTextSMS);
        textPenerima = (EditText) findViewById(R.id.editTextPenerima);
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("loginadmin", 0);
        final String petugas = pref.getString("nama", "Tidak ada");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new SweetAlertDialog(KirimNotifikasiKebeberapa.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Apakah anda yakin?")
                            .setContentText("Anda akan dikenakan biaya sms yang diambil dari pulsa anda")
                            .setCancelText("Batal")
                            .setConfirmText("Yakin")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    String penerima = textPenerima.getText().toString();
                                    penerima = penerima.replace(" ", "");
                                    penerima = penerima.replace(".", ",");
                                    penerima = penerima.replace("+62", "0");
                                    penerima = penerima.replace("62", "0");
                                    String split[] = penerima.split(",");
                                    for (int i = 0; i < split.length; i++) {
                                        cobaKirim(split[i], textSMS.getText().toString());
                                        new AttemptSubmit(split[i], textSMS.getText().toString(), petugas).execute();
                                    }
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

    }
    public boolean validate() {
        boolean valid = true;

        String sms = textSMS.getText().toString();
        String penerima = textPenerima.getText().toString();

        if (sms.isEmpty()) {
            textSMS.setError("Notifikasi harus di isi");
            valid = false;
        } else {
            textSMS.setError(null);
        }
        if (penerima.isEmpty()) {
            textPenerima.setError("Penerima harus di isi");
            valid = false;
        } else {
            textPenerima.setError(null);
        }

        return valid;
    }
    public void cobaKirim(String phoneNumber, String smsBody) {
        // Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();

//        String phoneNumber = "03172799934";
//        String smsBody = "Daneswara Jauhari coba";

        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        ArrayList<String> smsBodyParts = smsManager.divideMessage(smsBody);
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();

        for (int i = 0; i < smsBodyParts.size(); i++) {
            sentPendingIntents.add(sentPendingIntent);
            deliveredPendingIntents.add(deliveredPendingIntent);
        }

// For when the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Notifications sent successfully", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

// For when the SMS has been delivered
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Notifications delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "Notifications not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED));

// Send a text based SMS
        smsManager.sendMultipartTextMessage(phoneNumber, null, smsBodyParts, sentPendingIntents, deliveredPendingIntents);

    }

    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String telp, sms, petugas;

        public AttemptSubmit(String telp, String sms, String petugas) {
            this.telp = telp;
            this.sms = sms;
            this.petugas = petugas;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("telp", telp));
            nameValuePairs.add(new BasicNameValuePair("sms", sms));
            nameValuePairs.add(new BasicNameValuePair("petugas", petugas));
            nameValuePairs.add(new BasicNameValuePair("tipe", "Beberapa"));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/kenanga/insert_notifikasi.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                String responseAsString = EntityUtils.toString(response.getEntity());
                return responseAsString;

            } catch (ClientProtocolException e) {
                return e.toString();
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

////            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//            if (result != null) {
//                Toast.makeText(KirimNotifikasiKebeberapa.this, result, Toast.LENGTH_LONG).show();
//            }
        }

    }
}
