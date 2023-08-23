package com.oleg.gump;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    MyChronometer myChronometer;
    ArrayList<Long> rounds;
    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        rounds = new ArrayList<>();

        TextView round_tv = (TextView) findViewById(R.id.round_tv);
        TextView time_tv = (TextView) findViewById(R.id.time_tv);
        l1 = (LinearLayout) findViewById(R.id.layout1);

        myChronometer = new MyChronometer(this);
        myChronometer.setOnMyChronometerListener(new OnMyChronometerListener() {
            @Override
            public void onTick(long seconds) {
                if( (seconds / 60 / 60) == 0) {
                    time_tv.setText(SecondsToString(seconds));
                    round_tv.setText(SecondsToString(seconds - rounds.get(rounds.size() - 1)));
                }else{
                    myChronometer.stop();
                    Toast.makeText(MainActivity.this,"Секундомер остановлен так как прошёл час!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStop() {
                rounds.clear();
            }
        });

        Button round_b = (Button) findViewById(R.id.round_button);
        round_b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                OnClick(findViewById(R.id.stop_button));
                return false;
            }
        });

        Button stop_b = (Button) findViewById(R.id.stop_button);
        stop_b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!myChronometer.isRunning()){
                    draw();
                    time_tv.setText("00:00");
                    round_tv.setText("00:00");
                }
                return false;
            }
        });

    }

    private void draw(){
        l1.removeAllViews();

        for (int i = rounds.size()-1; i > 0; i--){
            TextView tv = new TextView(this);
            tv.setText(i+" - "+SecondsToString(rounds.get(i) - rounds.get(i-1)) );
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextSize(34);
            tv.setLines(1);
            l1.addView(tv);
        }
    }

    public String SecondsToString(long seconds) {
        long minutes = seconds / 60;
        long free_seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, free_seconds);
    }

    public void OnClick(View v){
        if (v.getId() == R.id.start_button && !myChronometer.isRunning()){
            try {
                myChronometer.start();
                rounds.add((long)0);
                draw();
            }catch (Exception e){
                Toast.makeText(this,"Уже запущен!",Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId() == R.id.round_button && myChronometer.isRunning()){
            rounds.add(myChronometer.getTime());
            draw();
        }else if (v.getId() == R.id.stop_button && myChronometer.isRunning()){
            rounds.add(myChronometer.getTime());
            draw();
            myChronometer.stop();
        }
    }
}