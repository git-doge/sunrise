package com.example.sunrise;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    public static int value = 0;
    private Timer myTimer;

    public static double graphLastX = 1;
    public static double trendLastX = 1;
    public static TextView num;
    public static LineGraphSeries<DataPoint> series;
    public static LineGraphSeries<DataPoint> cumulative;
    public static ArrayList<String> tasks = new ArrayList<String>();
    public BarGraphSeries<DataPoint> data;
    public Button trendButt;
    public Button saveButt;
    public Button compButt;
    static GraphView graph;
    public int initialPub;
    GraphView bars;
    GraphView total;
    int n = 1;

    boolean waterOff = false;
    boolean showerMin = false;
    boolean comSleep = false;
    boolean noTv = false;

   public void update() {
        updateTrend((int) Math.round(calcImpact()));
        updateBar((int) Math.round(calcImpact()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String stop = sharedPref.getString("stop", "");
        if (!(stop.equals("stop"))) {
            editor.putString("stop", "stop");
            editor.apply();
            Intent intent = new Intent(this, Questions1.class);
            startActivity(intent);
        }
        else {

            trendButt = findViewById(R.id.trendButtonOn);
            saveButt = findViewById(R.id.savingsButtonOn);
            compButt = findViewById(R.id.compButtonOn);
            trendButt.setVisibility(View.VISIBLE);
            compButt.setVisibility(View.INVISIBLE);
            saveButt.setVisibility(View.INVISIBLE);
            num = (TextView) findViewById(R.id.number);

            double value = calcImpact();
            int rounded = (int) Math.round(value);

            updateNum(0, rounded);
            initGraph(0, rounded);
            graph.setVisibility(View.VISIBLE);
            bars.setVisibility(View.INVISIBLE);
            total.setVisibility(View.INVISIBLE);

            Intent i = new Intent(this, NotificationService.class);
            startService(i);
            final Handler handler = new Handler();
            final int delay = 5000; //milliseconds

            handler.postDelayed(new Runnable(){
                public void run(){
                    update();

                    handler.postDelayed(this, delay);
                }
            }, delay);
        }
    }

    public double calcImpact() {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        double sum = 0;
        String recyclePlastic = sharedPref.getString("plastic", "");
        String recyclePaper = sharedPref.getString("paper", "");
        String recycleAluminum = sharedPref.getString("aluminum", "");
        String transitType = sharedPref.getString("transit", "");
        String miles = sharedPref.getString("distance", "");
        String showerLength = sharedPref.getString("showerLength", "");
        String showersWeek = sharedPref.getString("showersWeek", "");
        String diet = sharedPref.getString("diet", "");
        String power = sharedPref.getString("energy", "");
        String pool = sharedPref.getString("carpool", "");
        String people = sharedPref.getString("people", "");

        if (recyclePlastic.equals("no")) sum += 0.052d;
        if (recyclePaper.equals("no")) sum += 0.24d;
        if (recycleAluminum.equals("no")) sum += 0.25d;
        if (transitType.equals("car")) sum += Integer.parseInt(miles) * (5 / 7);
        else sum -= Integer.parseInt(miles) * (5 / 7);
        if(showersWeek != null && showerLength!= null) {
            System.out.println("random" + showerLength);
            sum += 0.57 * Integer.parseInt(showerLength) * (Integer.parseInt(showersWeek) / 7);
        }
        if (diet.equals("vegetarian")) sum += 9.32;
        if (diet.equals("vegan")) sum += 8.08;
        if (diet.equals("allMeat")) sum += 13.7;
        if (diet.equals("noBeef")) sum += 10.41;
        if(power != null) {
            sum += Integer.parseInt(power) * 2.07;
        }
        if(pool != null && transitType !=null && miles != null) {
            if (pool.equals("true") && transitType.equals("car")) {
                sum -= Integer.parseInt(miles) * (5 / 7);
                sum += (Integer.parseInt(miles) * (5 / 7)) / Integer.parseInt(people);
            }
        }
        if (waterOff) sum -= 0.75;
        if (comSleep) sum -= 0.98;
        if (showerMin) sum -= 0.57 * (5 / Integer.parseInt(showersWeek));
        if (noTv) sum -= 0.13;

        sum += 1.9;

        return sum;
    }

    public static void updateTrend(int y) {
        updateNum(value, y);
        series.appendData(new DataPoint((int)trendLastX, y), true, 50);
        trendLastX += 1d;
        graph.removeAllSeries();
        graph.addSeries(series);
    }

    public void updateTotal(int y) {
        cumulative.appendData(new DataPoint(graphLastX, y), true, 50);
        graphLastX ++;
        total.removeAllSeries();
        total.addSeries(cumulative);
    }

    public void updateBar(int y) {
        data.appendData(new DataPoint(40, y), true, 50);
        data.resetData(new DataPoint[] {
                new DataPoint(0, initialPub),
                new DataPoint(40, 50)
        });
        bars.removeAllSeries();
        bars.addSeries(data);
    }

    private void initGraph(int x, int y) {
        final int initial = (int) Math.round(calcImpact());
        initialPub = initial;
        System.out.println("Impact: " + calcImpact());
        //final int initial=0;
        graph = (GraphView) findViewById(R.id.graph);
        total = (GraphView) findViewById(R.id.total);
        bars = (GraphView) findViewById(R.id.bars);
        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(x, y)
        });
        cumulative = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 10),
                new DataPoint(6, 56),
                new DataPoint(8, 89),
        });
        data = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, initial),
                new DataPoint(40, 50)
        });
        bars.addSeries(data);
        graph.addSeries(series);
        total.addSeries(cumulative);

        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getViewport().setScalable(false);
        graph.getGridLabelRenderer().setLabelsSpace(75);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        series.setColor(Color.WHITE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);

        total.getGridLabelRenderer().setGridColor(Color.WHITE);
        total.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        total.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        total.getViewport().setScalable(false);
        total.getGridLabelRenderer().setLabelsSpace(75);
        total.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        total.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        cumulative.setColor(Color.WHITE);
        cumulative.setDrawDataPoints(true);
        cumulative.setDataPointsRadius(10);
        cumulative.setThickness(8);
        total.getViewport().setXAxisBoundsManual(true);
        total.getViewport().setMinX(0);
        total.getViewport().setMaxX(50);

        bars.getGridLabelRenderer().setGridColor(Color.WHITE);
        bars.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        bars.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        bars.getViewport().setScalable(false);
        bars.getGridLabelRenderer().setLabelsSpace(75);
        bars.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        bars.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        bars.getGridLabelRenderer().setPadding(20);
        data.setColor(Color.WHITE);
        data.setSpacing(10);
        data.setDrawValuesOnTop(true);
        data.setValuesOnTopColor(Color.WHITE);
//        bars.setVisibility(View.INVISIBLE);
    }

    public void trendOn(View view) {
        trendButt.setVisibility(View.VISIBLE);
        compButt.setVisibility(View.INVISIBLE);
        saveButt.setVisibility(View.INVISIBLE);
        graph.setVisibility(View.VISIBLE);
        bars.setVisibility(View.INVISIBLE);
        total.setVisibility(View.INVISIBLE);
    }

    public void saveOn(View view) {
        saveButt.setVisibility(View.VISIBLE);
        compButt.setVisibility(View.INVISIBLE);
        trendButt.setVisibility(View.INVISIBLE);
        graph.setVisibility(View.INVISIBLE);
        bars.setVisibility(View.INVISIBLE);
        total.setVisibility(View.VISIBLE);
    }

    public void compOn(View view) {
        compButt.setVisibility(View.VISIBLE);
        saveButt.setVisibility(View.INVISIBLE);
        trendButt.setVisibility(View.INVISIBLE);
        graph.setVisibility(View.INVISIBLE);
        bars.setVisibility(View.VISIBLE);
        total.setVisibility(View.INVISIBLE);

    }
    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            System.out.println("Timer has done!");
            update();

        }
    };
    private static void updateNum(int initial, int end) {
        int diff = Math.abs(end - initial);
        if (diff >= 100) { startCountAnimation(2500, initial, end); }
        else if (diff >= 25) {
            startCountAnimation(1000, initial, end);
        }
        else startCountAnimation(750, initial, end);
        value = end;
    }

    private static void startCountAnimation(int speed, int initial, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(initial, end);
        animator.setDuration(speed);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                num.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    public void increment(View view) {
        Intent intent = new Intent(this, TaskView.class);
        startActivity(intent);
    }

    public void addOffWater (View view) {
        Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG).show();
        //Add task
        tasks.add("Make sure to turn off the faucet when brushing your teeth!");
        LinearLayout linearLayout = findViewById(R.id.turnOffWater);
        linearLayout.setVisibility(View.GONE);
        waterOff = true;
    }

    public void addComSleep (View view) {
        Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG).show();
        //Add task
        tasks.add("Don't forget to put your computer in sleep mode!");
        LinearLayout linearLayout = findViewById(R.id.computerSleep);
        linearLayout.setVisibility(View.GONE);
        comSleep = true;
    }

    public void addShowerOne (View view) {
        Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG).show();
        //Add task
        tasks.add("Reduce your time in the shower by one minute!");
        LinearLayout linearLayout = findViewById(R.id.showerOneMin);
        linearLayout.setVisibility(View.GONE);
        showerMin = true;
    }

    public void delOffWater (View view) {
        LinearLayout linearLayout = findViewById(R.id.turnOffWater);
        linearLayout.setVisibility(View.GONE);
    }

    public void delComSleep (View view) {
        LinearLayout linearLayout = findViewById(R.id.computerSleep);
        linearLayout.setVisibility(View.GONE);
    }

    public void delShowerOne (View view) {
        LinearLayout linearLayout = findViewById(R.id.showerOneMin);
        linearLayout.setVisibility(View.GONE);
    }

    public void addNoTv (View view) {
        Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG).show();
        //Add task
        tasks.add("Don't forget to turn off the TV!");
        LinearLayout linearLayout = findViewById(R.id.turnOffTv);
        linearLayout.setVisibility(View.GONE);
        noTv = true;
    }

    public void addPubTrans (View view) {
        Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG).show();
        //Add task
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("transit", "none");
        editor.apply();
        tasks.add("Try to take public transit as your commute!");
        LinearLayout linearLayout = findViewById(R.id.pubTrans);
        linearLayout.setVisibility(View.GONE);
    }

    public void addNoBeef (View view) {
        Toast.makeText(this, "Task Added!", Toast.LENGTH_LONG).show();
        //Add task
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("diet", "noBeef");
        editor.apply();
        tasks.add("Take out beef from your daily diet!");
        LinearLayout linearLayout = findViewById(R.id.noBeef);
        linearLayout.setVisibility(View.GONE);

    }

    public void delNoTv (View view) {
        LinearLayout linearLayout = findViewById(R.id.turnOffTv);
        linearLayout.setVisibility(View.GONE);
    }

    public void delPubTrans (View view) {
        LinearLayout linearLayout = findViewById(R.id.pubTrans);
        linearLayout.setVisibility(View.GONE);
    }

    public void delNoBeef (View view) {
        LinearLayout linearLayout = findViewById(R.id.noBeef);
        linearLayout.setVisibility(View.GONE);
    }
}

