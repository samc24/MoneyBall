package com.example.moneyball;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class DareActivity extends AppCompatActivity implements Animation.AnimationListener {
    boolean blnButtonRotation = true;
    int intNumber = 6;
    long lngDegrees = 0;
    Button btn_upload;
    SharedPreferences sharedPreferences;
    Button b_start;
    ImageView selected, imageRoulette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare);
        b_start = findViewById(R.id.buttonStart);
        selected = findViewById(R.id.imageSelected);
        imageRoulette = findViewById(R.id.rouletteImage);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.intNumber = this.sharedPreferences.getInt("INT_NUMBER", 6);
        btn_upload = findViewById(R.id.btn_upload);
    }



    @Override
    public void onAnimationStart(Animation animation) {
        this.blnButtonRotation=false;
        b_start.setVisibility(View.VISIBLE);


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        @SuppressLint("WrongConstant") Toast toast = Toast.makeText(this, " "+ String.valueOf((int)(((double) this.intNumber) - Math.floor(((double) this.lngDegrees)
                / (360.0d/ ((double)this.intNumber)))))+ " ",0);
        toast.setGravity(49,0,0);
        this.blnButtonRotation=true;
        b_start.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void onClickButtonRotation(View v){
        if(this.blnButtonRotation){
            int ran = new Random().nextInt(360)+3600;
            RotateAnimation rotateAnimation = new RotateAnimation((float) this.lngDegrees,(float)
                    (this.lngDegrees +((long)ran)),1,0.5f,1,0.5f);
            this.lngDegrees=(this.lngDegrees + ((long)ran)) % 360;
            rotateAnimation.setDuration((long)ran);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setAnimationListener(this);
            imageRoulette.setAnimation(rotateAnimation);
            imageRoulette.startAnimation(rotateAnimation);

        }

    }


    public void onButtonClick(View view) {
        Intent myIntent = new Intent(DareActivity.this,  UploadActivity.class);
        Toast.makeText(getApplicationContext(),"Complete DARE", Toast.LENGTH_LONG).show();
        startActivity(myIntent);
    }
}


