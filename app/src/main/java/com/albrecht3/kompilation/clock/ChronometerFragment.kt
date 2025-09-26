package com.albrecht3.kompilation.clock

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.albrecht3.kompilation.databinding.FragmentChronometerBinding
import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.content.ContextCompat
import com.albrecht3.kompilation.R
import kotlin.math.roundToInt


class ChronometerFragment : Fragment() {

    private var binding: FragmentChronometerBinding? = null

    private var timeStarted: Boolean = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChronometerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        timer()
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(updateTime)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer()
    }

    private fun timer(){
        initListeners()
        serviceIntent = Intent(requireContext(), TimerService::class.java)
        ContextCompat.registerReceiver(
            requireActivity(),
            updateTime,
            IntentFilter(TimerService.TIMER_UPDATED),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun initListeners() {
        binding?.btnPlayPause?.setOnClickListener { timerStartStop() }
        binding?.btnReset?.setOnClickListener { timerReset() }
    }

    private fun timerStartStop() {
        if (timeStarted) {
            startTimer()
        } else
            stopTimer()
    }

    private fun startTimer(){
        serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
        requireActivity().startService(serviceIntent)
        binding?.btnPlayPause?.text = getString(R.string.timer_stop)
        binding?.btnPlayPause?.icon = ContextCompat.getDrawable(requireContext(),R.drawable.timer_pause)
        timeStarted = true
    }

    private fun stopTimer() {
        requireActivity().stopService(serviceIntent)
        binding?.btnPlayPause?.text = getString(R.string.timer_start)
        binding?.btnPlayPause?.icon = ContextCompat.getDrawable(requireContext(),R.drawable.timer_play)
        timeStarted = false
    }

    private fun timerReset() {
        stopTimer()
        time = 0.0
        binding?.tvTimer?.text = getTimeStringFromDouble(time)
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null) {
                time = p1.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
                binding?.tvTimer?.text = getTimeStringFromDouble(time)
            }
        }
    }
    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours,minutes,seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String {
        return String.format("%02d:%02d:%02d",hour,min,sec)
    }
}



