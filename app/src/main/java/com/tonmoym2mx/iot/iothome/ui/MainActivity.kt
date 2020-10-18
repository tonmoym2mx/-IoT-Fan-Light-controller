package com.tonmoym2mx.iot.iothome.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tonmoym2mx.iot.iothome.R
import com.tonmoym2mx.iot.iothome.databinding.ActivityMainBinding
import com.tonmoym2mx.iot.iothome.repository.HomeIoTRepository
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.java.KoinJavaComponent.inject

private lateinit var binding:ActivityMainBinding
private lateinit var factory: MainActivityViewModelFactory
private lateinit var viewModel: MainActivityViewModel
private val homeIoTRepository:HomeIoTRepository by inject(HomeIoTRepository::class.java)
private var speed:Int = 255
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupViewModel()
        binding.fan.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.fan(isChecked,speed).observe(this@MainActivity, Observer {
                binding.textView.text = it.toString()
            })
        }
        binding.light.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.light(isChecked).observe(this@MainActivity, Observer {
                binding.textView.text = it.toString()
            })
        }
        binding.seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.fan(true,progress).observe(this@MainActivity, Observer {
                    speed = progress
                    binding.textView.text = it.toString()
                })
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

    }

    private fun setupViewModel() {
        factory = MainActivityViewModelFactory(homeIoTRepository)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }
}