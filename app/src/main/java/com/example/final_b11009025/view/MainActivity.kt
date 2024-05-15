package com.example.final_b11009025.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.final_b11009025.R
import com.example.final_b11009025.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var calendarFragment: CalendarFragment
    private lateinit var todolistFragment: TodolistFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    //viewBinding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //更改標題列
        getSupportActionBar()!!.title = "排程小助手"

        //建立fragmentManager
        fragmentManager = supportFragmentManager

        //透過BottomNavigationView切換Fragment
        binding.navigation.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId
            fragmentTransaction = fragmentManager.beginTransaction()

            when (id) {
                R.id.calendar -> {
                    calendarFragment = CalendarFragment()
                    fragmentTransaction.replace(R.id.fragmentContainerView, calendarFragment)
                    fragmentTransaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.todolist -> {
                    todolistFragment = TodolistFragment()
                    fragmentTransaction.replace(R.id.fragmentContainerView, todolistFragment)
                    fragmentTransaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

    }


    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()
    }

    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                // 同意
                Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                //拒絕
                Toast.makeText(this, "未取得相機權限，如要更改，請至設定開啟權限", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // 第一次請求權限，直接詢問
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "已取得相機權限", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "未取得相機權限", Toast.LENGTH_SHORT).show()
        }
    }

}