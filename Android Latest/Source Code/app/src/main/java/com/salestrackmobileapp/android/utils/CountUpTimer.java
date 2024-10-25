package com.salestrackmobileapp.android.utils;

import android.os.CountDownTimer;

/**
 * Created by UserPC on 22-Feb-18.
 */

public class CountUpTimer  {
 //   private MyBinder binder = new MyBinder();



    public interface TimerDelegate {
         void onTick(long millisUntilFinished);

         void onFinish();
    }

    public CountUpTimer(int end, int tick, final TimerDelegate delegate) {
        this.delegate = delegate;
        this.tick = tick;
        // for some reason the last 2 ticks aren't counted
        this.end = end + 2000;
        timer = new CountDownTimer(end, tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeToGo = millisUntilFinished;
                delegate.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                delegate.onFinish();
            }
        };
    }
    public void start(){
        if(timer == null)
            timer = new CountDownTimer(this.timeToGo, tick) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeToGo = millisUntilFinished;
                    delegate.onTick(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    delegate.onFinish();
                }
            };
        this.timer.start();
    }
    public void pause(){
        if(this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }
    public void restart(){
        this.timer = new CountDownTimer(end,tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeToGo = millisUntilFinished;
                delegate.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                delegate.onFinish();

            }
        };
        start();
    }
    public String getTimeString() {
        long timeMilli = getTimeToGo();
        int time = (int) timeMilli / 1000;
        int minutes = time / 60;
        int seconds = time - (minutes * 60);
        String returnString = minutes + ":" + seconds;
        return returnString;
    }

    public long getTimeToGo(){
        long returnValue = end - this.timeToGo;
        return returnValue;
    }

    private CountDownTimer timer;
    private TimerDelegate delegate;
    private long tick;
    private long timeToGo;
    private int end;
}
