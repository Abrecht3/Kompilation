package com.albrecht3.kompilation.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.albrecht3.kompilation.R
import com.albrecht3.kompilation.databinding.FragmentBatteryBinding


class BatteryFragment : Fragment() {

    private var binding: FragmentBatteryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBatteryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        requireActivity().registerReceiver(batteryInfoReceiver,filter)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(batteryInfoReceiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun registerReceiver(){
        requireActivity().registerReceiver(batteryInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private var batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val batteryLevel = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val batteryIsCharging = p1?.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            val batteryTemperature = p1?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)?.div(10)
            val batteryVoltage = p1?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)?.div(1000)
            val batteryTechnology = p1?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            batteryLevel?.let {
                binding?.batteryProgress?.setProgress(100 - it)
                binding?.tvBatteryPercent?.text = "$batteryLevel%"
            }
            batteryVoltage.let {
                binding?.tvVoltValue?.text = "$batteryVoltage V"
            }
            batteryTechnology.let {
                binding?.tvTechValue?.text = batteryTechnology.toString()
            }
            batteryIsCharging.let {
                if (batteryIsCharging == 0) binding?.tvConnectStatus?.text = getString(R.string.plug_off)
                else binding?.tvConnectStatus?.text = getString(R.string.plug_on)
            }
            batteryTemperature.let {
                binding?.tvTemperatureValue?.text = "$batteryTemperature °C"
            }
        }
    }
}