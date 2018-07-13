package com.team4.soundsgo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button play, stop, begin, create;
    private MediaRecorder rec;
    private String file;
    final Context context = this;
    private String result;

    int ID;
    String latitude, longitude;
    TextView city;
    String cityName;
    EditText edit;
    Location loc;
    final String EXTRA_LATITUDE = "latitude";
    final String EXTRA_LONGITUDE = "longitude";
    final String EXTRA_NAME = "name";
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button GoMap = (Button) findViewById(R.id.goMap);
        GoMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("INFO", "latitude = " + latitude + " longitude = " + longitude + " nom = " + result);

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra(EXTRA_LATITUDE, latitude);
                intent.putExtra(EXTRA_LONGITUDE, longitude);
                intent.putExtra(EXTRA_NAME, result);

                startActivity(intent);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        latitude = Double.toString(location.getLatitude());
                        longitude = Double.toString(location.getLongitude());
                        try {
                            Geocoder gcd = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                            if (addresses.size() > 0) {
                                cityName = addresses.get(0).getLocality();
                            } else {
                                // do your stuff
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });


            city = (TextView)findViewById(R.id.cityTW);
            edit = (EditText)findViewById(R.id.Name);

        ///// RECORDING /////

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        begin = (Button) findViewById(R.id.record);
        create = (Button) findViewById(R.id.create);

        stop.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        create.setVisibility(View.GONE);



        rec = new MediaRecorder();

        rec.setAudioSource(MediaRecorder.AudioSource.MIC);  //to use the mic
        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// to record in 3gp
        rec.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);




        begin.setOnClickListener(new View.OnClickListener() { //to start to record
            @Override
            public void onClick(View v) {
                try {
                    ID++;
                    file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
                    rec.setOutputFile(file);
                    rec.prepare();
                    rec.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                begin.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Recording launched", Toast.LENGTH_LONG).show();
            }


        });


        stop.setOnClickListener(new View.OnClickListener() { //to stop and save the audio
            @Override
            public void onClick(View v) {
                rec.stop();
                rec.release();
                rec = null;
                begin.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                city.setText("City : " + cityName);
                Toast.makeText(getApplicationContext(), "Audio Recorded successfully", Toast.LENGTH_LONG).show();
                try {

                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() { // to listen the sound
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(file);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    create.setVisibility(View.VISIBLE);


                    Toast.makeText(getApplicationContext(), "Listening Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() { //to create the record

            @Override
            public void onClick(View v) {

                try {
                result = edit.getText().toString();

                Toast.makeText(getApplicationContext(), "sound created", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
            }
            });
    }
}

