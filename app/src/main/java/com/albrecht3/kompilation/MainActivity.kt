package com.albrecht3.kompilation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.albrecht3.kompilation.clock.ChronometerFragment
import com.albrecht3.kompilation.clock.TimerFragment
import com.albrecht3.kompilation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(ChronometerFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.itemChrono->replaceFragment(ChronometerFragment())
                R.id.itemTimer->replaceFragment(TimerFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commit()
    }
}