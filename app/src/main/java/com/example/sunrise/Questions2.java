package com.example.sunrise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class Questions2 extends AppCompatActivity {
    String recyclePlastic;
    String recyclePaper;
    String recycleAluminum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions2);
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //String transitType = sharedPref.getString("transit", "");
        //Toast.makeText(this, miles, Toast.LENGTH_LONG).show();
    }
    public void next2(View view) {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("plastic", recyclePlastic);
        editor.putString("paper", recyclePaper);
        editor.putString("aluminum", recycleAluminum);
        editor.apply();
        Intent intent = new Intent(this, Questions3.class);
        startActivity(intent);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.plasticYesRadio:
                if (checked)
                    recyclePlastic = "yes";
                break;
            case R.id.plasticNoRadio:
                if (checked)
                    recyclePlastic = "no";
                break;
            case R.id.paperYesRadio:
                if (checked)
                    recyclePaper = "yes";
                break;
            case R.id.paperNoRadio:
                if (checked)
                    recyclePaper = "no";
                break;
            case R.id.aluminumYesRadio:
                if (checked)
                    recycleAluminum = "yes";
                break;
            case R.id.aluminumNoRadio:
                if (checked)
                    recycleAluminum = "no";
                break;
        }
    }
}
