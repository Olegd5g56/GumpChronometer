package com.oleg.gump;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

public class MyChronometer {
    private static final String log_tag = "MyChronometerTAG";
    private long base_time;
    private Activity activity;
    private OnMyChronometerListener onMyChronometerListener;
    private boolean running;
    private Thread thread;
    MyChronometer(Activity activity){
        super();
        this.activity=activity;
        running=false;
    }

    public void start() throws Exception{
        start(SystemClock.elapsedRealtime());
    }
    public void start(long base_time) throws Exception {
        this.base_time=base_time;

        if(running) throw new Exception("Already running!");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (running) {
                        Thread.sleep(100);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onMyChronometerListener.onTick( (SystemClock.elapsedRealtime() - base_time) / 1000 );
                            }
                        });
                    }
                }catch(InterruptedException e){
                    Log.d(log_tag,e.getLocalizedMessage());
                }


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onMyChronometerListener.onStop();
                    }
                });

            }
        });
        thread.start();
        running=true;

    }
    public long getTime(){
        return (SystemClock.elapsedRealtime() - base_time) / 1000 ;
    }
    public long getBaseTime(){
        return base_time;
    }
    public boolean isRunning(){
        return running;
    }
    public void stop(){
        running=false;
        try{
            thread.join();
        }catch (Exception e){}
    }
    public void setOnMyChronometerListener(OnMyChronometerListener onMyChronometerListener){
        this.onMyChronometerListener = onMyChronometerListener;
    }

}
