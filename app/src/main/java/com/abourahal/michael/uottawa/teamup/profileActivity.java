package com.abourahal.michael.uottawa.teamup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.BoolRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {
    public static final String PROFILEPREFERENCES="profilePreference";
    public static final String SPORTSKEY="sportKey", NAMEKEY="nameKey",PHONEKEY="phoneKey",EMAILKEY="emailKey",DESCRIPTIONKEY="descriptionKey",DATEKEY="dateKey",MALEKEY="maleKey",COMPETITIVEKEY="competitiveKey";
    SharedPreferences sharedPreferences;
    private TextView date;
    private TextView name,phoneNumber,email,description,gender,competitive,sport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=(TextView)findViewById(R.id.nameTextView);
        date=(TextView) findViewById(R.id.dateTextView);
        phoneNumber=(TextView)findViewById(R.id.phoneTextView);
        email=(TextView)findViewById(R.id.emailTextView);
        description=(TextView)findViewById(R.id.descriptionTextView);
        gender=(TextView)findViewById(R.id.genderTextView);
        competitive=(TextView)findViewById(R.id.competitiveTextView);
        sport=(TextView)findViewById(R.id.sportTextView);
        load();

    }
    public void load(){
            sharedPreferences = getSharedPreferences(PROFILEPREFERENCES,Context.MODE_PRIVATE);
            name.setText(sharedPreferences.getString(NAMEKEY,name.getText().toString()));
            date.setText(sharedPreferences.getString(DATEKEY,date.getText().toString()));
            phoneNumber.setText(sharedPreferences.getString(PHONEKEY,phoneNumber.getText().toString()));
            email.setText(sharedPreferences.getString(EMAILKEY,email.getText().toString()));
            description.setText(sharedPreferences.getString(DESCRIPTIONKEY,description.getText().toString()));
            sport.setText(sharedPreferences.getString(SPORTSKEY,sport.getText().toString()));
            if(sharedPreferences.getBoolean(MALEKEY,false)==true){
                gender.setText("Male");
            }else{
                gender.setText("Female");
            }
            if(sharedPreferences.getBoolean(COMPETITIVEKEY,false)==true){
                competitive.setText("Competitive");
            }else{
                competitive.setText("Casual");
            }


    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Refresh Activity
        load();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_settings){
//            Intent intent=new Intent(this,settingsActivity.class);
//            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.action_help){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Help");
            dialog.setMessage(Html.fromHtml("Here you can view your profile information <br/><br/> <b>How to edit your Profile? </b> <br/>1) Click on the edit button on the bottom right and enter all relevent information in the next screen"));
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(false);
            dialog.create().show();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
    public void launchEditProfileActivity(View view){
        Intent intent = new Intent(getApplicationContext(),editProfileActivity.class);
        startActivity(intent);
    }

}
