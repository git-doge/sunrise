package com.example.sunrise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class Questions3 extends AppCompatActivity {

    EditText showerLength;
    EditText showersWeek;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions3);
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        showerLength = (EditText) findViewById(R.id.showerDay);
        showersWeek = (EditText) findViewById(R.id.sWeek);
        //Toast.makeText(this, miles, Toast.LENGTH_LONG).show();
    }
    public void next3(View view) {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("showerLength", showerLength.getText().toString());
        editor.putString("showersWeek", showersWeek.getText().toString());
        editor.apply();
        Intent intent = new Intent(this, Questions4.class);
        startActivity(intent);
    }

}
