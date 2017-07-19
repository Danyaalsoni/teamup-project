package com.abourahal.michael.uottawa.teamup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


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
