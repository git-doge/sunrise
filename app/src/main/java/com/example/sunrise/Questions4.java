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

public class Questions4 extends AppCompatActivity {

    String diet;
    EditText energy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions4);
        energy = (EditText) findViewById(R.id.energyy);
    }

    public void next4(View view) {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("diet", diet);
        editor.putString("energy", energy.getText().toString());
        editor.apply();
        Intent intent = new Intent(this, Questions5.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.vegetarianRadio:
                if (checked)
                    diet = "vegetarian";
                break;
            case R.id.veganRadio:
                if (checked)
                    diet = "vegan";
                break;
            case R.id.noBeefRadio:
                if(checked)
                    diet = "noBeef";
                break;
            case R.id.allMeatRadio:
                if (checked)
                    diet = "allMeat";
                break;

        }
    }
}
