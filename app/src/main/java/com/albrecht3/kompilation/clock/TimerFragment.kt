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
            if (isClicked && valueTimer > 0) {
                startTimer(valueTimer)
                disableButtons()
            }
        }
    }

    private fun setTimeDefault() {
        val minute: Long = 60000
        binding?.btnSetTime1?.setOnClickListener {
            lapso = "05"
            valueTimer = lapso.toLong() * minute
            binding?.tvTmpMin?.text = lapso
        }
        binding?.btnSetTime2?.setOnClickListener {
            lapso = "10"
            valueTimer = lapso.toLong() * minute
            binding?.tvTmpMin?.text = lapso
        }
        binding?.btnSetTime3?.setOnClickListener {
            lapso = "15"
            valueTimer = lapso.toLong() * minute
            binding?.tvTmpMin?.text = lapso
        }
    }

    private fun initKeyboard() {
        val second: Long = 1000
        var sec = ""
        var min = ""
        var hrs = ""
        binding?.gridKeyboard?.children?.filterIsInstance<Button>()?.forEach { button ->
            button.setOnClickListener {
                val btnText = button.text.toString()
                when {
                    btnText.matches("[0-9]".toRegex()) -> {
                        lapso += btnText
                        lapsoChunked = lapso.reversed().chunked(2) { it.toString() }
                        when (lapsoChunked.size) {
                            1 -> {
                                binding?.tvTmpSec?.text = makeTimeString(lapso.toLong())
                                valueTimer = lapso.toLong() * second
                            }

                            2 -> {
                                sec = lapsoChunked[0].reversed()
                                min = lapsoChunked[1].reversed()
                                binding?.tvTmpSec?.text = makeTimeString(sec.toLong())
                                binding?.tvTmpMin?.text = makeTimeString(min.toLong())
                                valueTimer =
                                    (sec.toLong() * second) + ((min.toLong() * second) * 60)
                            }

                            else -> {
                                sec = lapsoChunked[0].reversed()
                                min = lapsoChunked[1].reversed()
                                hrs = lapsoChunked[2].reversed()
                                binding?.tvTmpSec?.text = makeTimeString(sec.toLong())
                                binding?.tvTmpMin?.text = makeTimeString(min.toLong())
                                binding?.tvTmpHour?.text = makeTimeString(hrs.toLong())
                                valueTimer =
                                    (sec.toLong() * second) + (min.toLong() * second * 60) + (hrs.toLong() * second * 3600)
                            }
                        }
                    }
                    btnText == "C" -> {
                        reset()
                    }
                }
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
                enableButtons()
                reset()
            }
        }.start()
    }

    private fun makeTimeString(time: Long): String {
        return String.format(Locale.getDefault(), "%02d", time)
    }

    private fun reset() {
        val resetTime: Long = 0
        binding?.tvTmpHour?.text = makeTimeString(resetTime)
        binding?.tvTmpMin?.text = makeTimeString(resetTime)
        binding?.tvTmpSec?.text = makeTimeString(resetTime)
        valueTimer = resetTime
        lapso = ""
        isClicked = false
    }

    private fun enableButtons() {
        binding?.btnSetTime1?.isEnabled = true
        binding?.btnSetTime2?.isEnabled = true
        binding?.btnSetTime3?.isEnabled = true
        binding?.gridKeyboard?.children?.filterIsInstance<Button>()?.forEach {
            it.isEnabled = true
        }
    }

    private fun disableButtons() {
        binding?.btnSetTime1?.isEnabled = false
        binding?.btnSetTime2?.isEnabled = false
        binding?.btnSetTime3?.isEnabled = false
        binding?.gridKeyboard?.children?.filterIsInstance<Button>()?.forEach {
            it.isEnabled = false
        }
    }
}

