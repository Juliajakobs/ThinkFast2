package com.example.thinkFast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "HeaderActivity";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void bottomNavigation(){
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        // Set Account selected
        bottomNavigationView.setSelectedItemId(R.id.account);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.account:
                        if(sharedpreferences.getString(AccountActivity.uName,"null")!="null"){
                            //startActivity(new Intent(BaseActivity.this, SetupActivity.class));
                            Intent in = new Intent(BaseActivity.this, SetupActivity.class);
                            in.putExtra("wasLoggedIn",false);
                            startActivity(in);
                        }
                        else startActivity(new Intent(BaseActivity.this, AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.quiz:
                        // Only perform if logged in
                        if(sharedpreferences.getString(AccountActivity.uName,"null")!="null"){
                          //  startActivity(new Intent(BaseActivity.this, SetupActivity.class));
                            Intent in = new Intent(BaseActivity.this, SetupActivity.class);
                            in.putExtra("wasLoggedIn",true);
                            startActivity(in);

                        }
                        else {
                            startActivity(new Intent(BaseActivity.this,AccountActivity.class));
                            Toast.makeText(BaseActivity.this, "You need to login in to play quiz",Toast.LENGTH_SHORT).show();
                        }
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.scoreboard:
                            startActivity(new Intent(BaseActivity.this, ScoreboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void logout(){

        SharedPreferences sharedpreferences = getSharedPreferences(AccountActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.d(TAG,"SharedPrefs: " + sharedpreferences.getString(AccountActivity.uName,"Should have a username"));
        editor.clear();
        editor.commit();
        Log.d(TAG,"SharedPrefs: " + sharedpreferences.getString(AccountActivity.uName,"null"));
        // Redirect to login account page
        startActivity(new Intent(BaseActivity.this,AccountActivity.class));
    }
}