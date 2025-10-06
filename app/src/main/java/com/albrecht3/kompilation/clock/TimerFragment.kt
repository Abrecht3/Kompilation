package com.albrecht3.kompilation.clock

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import com.albrecht3.kompilation.R
import com.albrecht3.kompilation.databinding.FragmentTimerBinding
import java.util.Locale

class TimerFragment : Fragment() {

    private var binding: FragmentTimerBinding? = null

    private var timer: CountDownTimer? = null
    private var lapso = ""
    private lateinit var lapsoChunked: List<String>
    private var isClicked = false
    var valueTimer: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTimer()
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
        initTimer()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initTimer() {
        setTimeDefault()
        initKeyboard()
        binding?.fabStart?.setOnClickListener {
            isClicked = !isClicked
            Toast.makeText(requireContext(), valueTimer.toString(), Toast.LENGTH_LONG).show()
            if (isClicked) {
                startTimer(valueTimer)
                disableButtons()
            }
        }
        isClicked = false
    }

    private fun setTimeDefault() {
        val minute: Long = 60000
        binding?.btnSetTime1?.setOnClickListener {
            valueTimer = 0
            valueTimer = 5 * minute
            binding?.tvTmpMin?.text = "05"
            Toast.makeText(requireContext(), valueTimer.toString(), Toast.LENGTH_LONG).show()
        }
        binding?.btnSetTime2?.setOnClickListener {
            valueTimer = 0
            valueTimer = 10 * minute
            binding?.tvTmpMin?.text = "10"
            Toast.makeText(requireContext(), valueTimer.toString(), Toast.LENGTH_LONG).show()
        }
        binding?.btnSetTime3?.setOnClickListener {
            valueTimer = 0
            valueTimer = 15 * minute
            binding?.tvTmpMin?.text = "15"
            Toast.makeText(requireContext(), valueTimer.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun initKeyboard() {
        val second: Long = 1000
        binding?.gridKeyboard?.children?.filterIsInstance<Button>()?.forEach { button ->
            button.setOnClickListener {
                val btnText = button.text.toString()
                when {
                    btnText.matches("[0-9]".toRegex()) -> {
                        lapso += btnText
                        valueTimer = lapso.toLong() * second
                        Toast.makeText(requireContext(), lapso, Toast.LENGTH_SHORT).show()
                    }
                }
                lapsoChunked = lapso.chunked(2) { it.toString() }
            }
        }
    }

    private fun startTimer(p10: Long) {
        timer = object : CountDownTimer(p10, 1000) {
            override fun onTick(p0: Long) {
                val hours = (p0 / 1000) / 3600
                val minutes = ((p0 / 1000) % 3600) / 60
                val seconds = (p0 / 1000) % 60
                binding?.tvTmpHour?.text = makeTimeString(hours)
                binding?.tvTmpMin?.text = makeTimeString(minutes)
                binding?.tvTmpSec?.text = makeTimeString(seconds)
            }

            override fun onFinish() {
                binding?.tvTmpHour?.text = getString(R.string.tmp_default)
                binding?.tvTmpMin?.text = getString(R.string.tmp_default)
                binding?.tvTmpSec?.text = getString(R.string.tmp_default)
                Toast.makeText(requireContext(), getString(R.string.time_out), Toast.LENGTH_SHORT)
                    .show()
            }

        }.start()
    }

    private fun makeTimeString(time: Long): String {
        return String.format(Locale.getDefault(), "%02d", time)
    }

    private fun disableButtons() {
        binding?.btnSetTime1?.isEnabled = false
        binding?.btnSetTime2?.isEnabled = false
        binding?.btnSetTime3?.isEnabled = false
        binding?.gridKeyboard?.isEnabled = false
    }
}

