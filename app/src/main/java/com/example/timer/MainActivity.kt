package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.example.timer.util.PrefUtil

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerlengthSeconds = 0L
    private var timerState = TimerState.Stopped

    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "      Timer"

        fab_play.setOnClickListener{v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener{v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener{v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()

        //TODO : remove background timer, hide notification
    }

    override fun onPause() {
        super.onPause()

        if(timerState == TimerState.Running){
            timer.cancel()
            //TODO: start background timer and show notification
        }
        else if(timerState == TimerState.Paused){
            //TODO show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerlengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)

        if(timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerlengthSeconds

        //TODO: change seconds remaining according to where the background timer stopped

        //Reumse where left off
        if (timerState == TimerState.Running)
            startTimer()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
