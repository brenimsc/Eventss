package com.example.events.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.events.R
import com.example.events.databinding.ActivityMainBinding
import com.example.events.viewmodel.EventsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: EventsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentEvents = FragmentEvents()
        val fragmentFavorites = FragmentFavorites()
        val fragmentCheckins = FragmentCheckins()


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.id_events -> {
                    viewModel.goFragmentEvents()
                }
                R.id.id_favorites -> {
                    viewModel.goFragmentFavorites()
                }
                else -> {
                    viewModel.goFragmentsCheckins()
                }
            }
            true
        }

        viewModel.currentStep.observe(this) {
            when (it) {
                0 -> {
                    setCurrentFragment(fragmentEvents)
                }
                1 -> {
                    setCurrentFragment(fragmentFavorites)
                }
                2 -> {
                    setCurrentFragment(fragmentCheckins)
                }
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_Fragment, fragment)
            commit()
        }
    }
}