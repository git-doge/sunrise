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

public class Questions1 extends AppCompatActivity {
    String transitType;
    EditText mileNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions1);

        mileNum = (EditText) findViewById(R.id.miles);
    }

    public void next1(View view) {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("transit", transitType);
        editor.putString("distance", mileNum.getText().toString());
        editor.apply();
        Intent intent = new Intent(this, Questions2.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.carRadio:
                if (checked)
                    transitType = "car";
                    break;
            case R.id.walkRadio:
                if (checked)
                    transitType = "none";
                    break;
            case R.id.busRadio:
                if (checked)
                    transitType = "none";
                    break;
        }
    }

}
