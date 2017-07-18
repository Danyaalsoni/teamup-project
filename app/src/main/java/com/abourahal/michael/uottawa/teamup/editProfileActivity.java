package com.abourahal.michael.uottawa.teamup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfileActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST=1;
    private Button date;
    private int year,month,day;
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
}
