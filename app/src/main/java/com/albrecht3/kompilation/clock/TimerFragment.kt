package com.albrecht3.kompilation.clock

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.albrecht3.kompilation.R
import com.albrecht3.kompilation.databinding.FragmentTimerBinding
import java.util.Locale

class TimerFragment : Fragment() {

    private var binding: FragmentTimerBinding? = null

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hourGlass()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        hourGlass()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourGlass()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun hourGlass() {
        var setTimer: Long = 0
        binding?.btnSetTime1?.setOnClickListener {
            setTimer = 300000
            binding?.tvTmpMin?.text = "05"
            binding?.fabStart?.setOnClickListener { startTimer(setTimer) }
        }
        binding?.btnSetTime2?.setOnClickListener {
            setTimer = 600000
            binding?.tvTmpMin?.text = "10"
            binding?.fabStart?.setOnClickListener { startTimer(setTimer) }
        }
        binding?.btnSetTime3?.setOnClickListener {
            setTimer = 900000
            binding?.tvTmpMin?.text = "15"
            binding?.fabStart?.setOnClickListener { startTimer(setTimer) }
        }

    }


    private fun startTimer(p10: Long) {
        timer = object : CountDownTimer(p10,1000){
            override fun onTick(p0: Long) {
                val hours = (p0/1000) / 3600
                val minutes = ((p0/1000) % 3600) / 60
                val seconds = (p0/1000) % 60
                binding?.tvTmpHour?.text = makeTimeString(hours)
                binding?.tvTmpMin?.text = makeTimeString(minutes)
                binding?.tvTmpSec?.text = makeTimeString(seconds)
            }

            override fun onFinish() {
                binding?.tvTmpHour?.text = getString(R.string.tmp_default)
                binding?.tvTmpMin?.text = getString(R.string.tmp_default)
                binding?.tvTmpSec?.text = getString(R.string.tmp_default)
                Toast.makeText(requireContext(), getString(R.string.time_out), Toast.LENGTH_SHORT).show()
            }

            private fun makeTimeString(time: Long): String {
                return String.format(Locale.getDefault(),"%02d",time)
            }
        }.start()
    }
}