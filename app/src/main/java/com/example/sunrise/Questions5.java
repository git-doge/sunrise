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
import android.widget.RadioButton;
import android.widget.TextView;

public class Questions5 extends AppCompatActivity {

    String pool;
    EditText people;
    TextView peopleTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions5);
        people = (EditText) findViewById(R.id.peoplePool);
        peopleTxt = findViewById(R.id.textView7);
        people.setVisibility(View.GONE);
        peopleTxt.setVisibility(View.GONE);
    }

    public void next5(View view) {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("carpool", pool);
        editor.putString("people", people.getText().toString());
        editor.putString("stop", "stop");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yesPool:
                if (checked)
                    pool = "true";
                people.setVisibility(View.VISIBLE);
                peopleTxt.setVisibility(View.VISIBLE);
                break;
            case R.id.noPool:
                if (checked)
                    pool = "false";
                    people.setVisibility(View.GONE);
                    peopleTxt.setVisibility(View.GONE);
                break;
        }
    }

}
