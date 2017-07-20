package com.abourahal.michael.uottawa.teamup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfileActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST=1;
    private Button date;
    private EditText nameEdit,phoneNumberEdit,emailEdit,descriptionEdit,sportEdit;
    private RadioButton maleRadio,femaleRadio;
    private boolean maleR,femaleR,competitive;
    private CheckBox competitiveCheck;
    private String name,phoneNumber,email,description,dateText,sport;
    private int year,month,day;
    public static final String PROFILEPREFERENCES="profilePreference";
    public static final String SPORTSKEY="sportKey",NAMEKEY="nameKey",PHONEKEY="phoneKey",EMAILKEY="emailKey",DESCRIPTIONKEY="descriptionKey",DATEKEY="dateKey",MALEKEY="maleKey",FEMALEKEY="femaleKey",COMPETITIVEKEY="competitiveKey";
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        CircleImageView image=(CircleImageView) findViewById(R.id.editProfile_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        date=(Button)findViewById(R.id.dateButton);
        nameEdit=(EditText)findViewById(R.id.nameEditText);
        phoneNumberEdit=(EditText)findViewById(R.id.numberEditText);
        emailEdit=(EditText)findViewById(R.id.emailEditText);
        descriptionEdit=(EditText)findViewById(R.id.descriptionEditText);
        maleRadio=(RadioButton)findViewById(R.id.maleRadio);
        femaleRadio=(RadioButton)findViewById(R.id.femaleRadio);
        competitiveCheck=(CheckBox)findViewById(R.id.checkBox);
        sportEdit=(EditText)findViewById(R.id.sportsEditText);

        sharedPreferences = getSharedPreferences(PROFILEPREFERENCES,Context.MODE_PRIVATE);
        name=(sharedPreferences.getString(NAMEKEY,""));
        if(name!=""){
            nameEdit.setText(name);
        }
        dateText=(sharedPreferences.getString(DATEKEY,""));
        if(dateText!=""){
            date.setText(dateText);
        }
        phoneNumber=(sharedPreferences.getString(PHONEKEY,""));
        if(phoneNumber!=""){
            phoneNumberEdit.setText(phoneNumber);
        }
        email=(sharedPreferences.getString(EMAILKEY,""));
        if(email!=""){
            emailEdit.setText(email);
        }
        description=(sharedPreferences.getString(DESCRIPTIONKEY,""));
        if(description!=""){
            descriptionEdit.setText(description);
        }
        sport=(sharedPreferences.getString(SPORTSKEY,""));
        if(sport!=""){
            sportEdit.setText(sport);
        }
        if(sharedPreferences.getBoolean(MALEKEY,false)==true){
           maleRadio.setChecked(true);
        }else{
            maleRadio.setChecked(false);
            femaleRadio.setChecked(true);
        }
        if(sharedPreferences.getBoolean(COMPETITIVEKEY,false)==true){
            competitiveCheck.setChecked(true);
        }else{
            competitiveCheck.setChecked(false);
        }

        fab=(FloatingActionButton)findViewById(R.id.saveButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            boolean r=  validate1();
                if(r==true){
                    finish();
                }
            }
        });

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
            dialog.setMessage(Html.fromHtml("Here you can edit your profile information <br/> Please enter all relevent information"));
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
    public void launchDatepicker(View view){
        final Calendar c=Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                CircleImageView image=(CircleImageView)findViewById(R.id.profile_image);
                image.setImageBitmap(bitmap);
                System.out.println(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(editProfileActivity.this);
        dialog.setMessage("You have made some changes, do you want to save them");
        dialog.setTitle("Save Changes");
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean r=validate1();
                if(r) {
                    finish();
                }
            }
        });
        dialog.setNegativeButton("Discard", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public boolean validate1(){
        sharedPreferences=getSharedPreferences(PROFILEPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        if(nameEdit.getText().toString().equals("")){
            AlertDialog.Builder dialog=new AlertDialog.Builder(editProfileActivity.this);
            dialog.setMessage("Please enter your Name");
            dialog.setTitle("Missing information");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nameEdit.requestFocus();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        }
        else if(phoneNumberEdit.getText().toString().equals("")){
            AlertDialog.Builder dialog=new AlertDialog.Builder(editProfileActivity.this);
            dialog.setMessage("Please enter your Phone Number");
            dialog.setTitle("Missing information");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    phoneNumberEdit.requestFocus();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        }
       else if(emailEdit.getText().toString().equals("")){
            AlertDialog.Builder dialog=new AlertDialog.Builder(editProfileActivity.this);
            dialog.setMessage("Please enter your email");
            dialog.setTitle("Missing information");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    emailEdit.requestFocus();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        }
        else if(date.getText().toString().equals("")) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(editProfileActivity.this);
            dialog.setMessage("Please enter your Date of Birth");
            dialog.setTitle("Missing information");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    date.requestFocus();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        }
        else if(nameEdit.getText().toString().equals("")&&phoneNumberEdit.getText().toString().equals("")&&date.getText().toString().equals("")
                &&emailEdit.getText().toString().equals("")){
            AlertDialog.Builder dialog=new AlertDialog.Builder(editProfileActivity.this);
            dialog.setMessage("Please enter all the missing information");
            dialog.setTitle("Missing information");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nameEdit.requestFocus();
                }
            });
            dialog.setCancelable(false);
            dialog.create().show();
        }
        else {
            name = nameEdit.getText().toString();
            editor.putString(NAMEKEY,name);
            phoneNumber = phoneNumberEdit.getText().toString();
            editor.putString(PHONEKEY,phoneNumber);
            email = emailEdit.getText().toString();
            editor.putString(EMAILKEY,email);
            description=descriptionEdit.getText().toString();
            editor.putString(DESCRIPTIONKEY,description);
            dateText = date.getText().toString();
            editor.putString(DATEKEY,dateText);
            editor.putString(SPORTSKEY, sportEdit.getText().toString());
            maleR = maleRadio.isChecked();
            editor.putBoolean(MALEKEY, maleR);
            femaleR = femaleRadio.isChecked();
            editor.putBoolean(FEMALEKEY, femaleR);
            competitive = competitiveCheck.isChecked();//v
            editor.putBoolean(COMPETITIVEKEY, competitive);
            editor.commit();
            return true;
        }
    return false;
    }
}
