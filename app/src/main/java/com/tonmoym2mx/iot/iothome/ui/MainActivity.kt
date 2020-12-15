package com.tonmoym2mx.iot.iothome.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tonmoym2mx.iot.iothome.R
import com.tonmoym2mx.iot.iothome.databinding.ActivityMainBinding
import com.tonmoym2mx.iot.iothome.repository.HomeIoTRepository
import com.tonmoym2mx.iot.iothome.ui.custom_view.Croller
import com.tonmoym2mx.iot.iothome.ui.custom_view.OnCrollerChangeListener
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
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupViewModel()
        viewModel.status()
        setupObserver()
        uiSetup()
    }

    private fun setupObserver() {
        viewModel.boardStatus.observe(this, Observer {
            binding.croller.progress = it.fanSpeed?:0
            if(it.isLightOneOn==1){
                binding.lightGroup.selectButton(R.id.lightButton)
                binding.lightButton.isSelected = true
            }
            if(it.isFanOn ==1){
                binding.fanGroup.selectButton(R.id.fanButton)
                binding.fanButton.isSelected = true
            }


        })
    }

    private fun uiSetup() {

        binding.refresh.setOnClickListener {
            viewModel.status()
        }


        binding.croller.setOnCrollerChangeListener(object :OnCrollerChangeListener{
            override fun onProgressChanged(croller: Croller?, progress: Int) {
                speed = progress
                viewModel.fan(viewModel.isFanOnLiveData.value==true,progress)
            }

            override fun onStartTrackingTouch(croller: Croller?) {

            }

            override fun onStopTrackingTouch(croller: Croller?) {

            }
        })

        binding.lightGroup.setOnSelectListener { themedButton ->
            if(viewModel.isLoading.value == false) {
                viewModel.light(themedButton.isSelected)
            }
        }
        binding.fanGroup.setOnSelectListener {themedButton ->
            if(viewModel.isLoading.value == false) {
                viewModel.fan(themedButton.isSelected, speed)
            }

        }
    }

    private fun setupViewModel() {
        factory = MainActivityViewModelFactory(homeIoTRepository)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }
}