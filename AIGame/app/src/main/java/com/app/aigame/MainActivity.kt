package com.app.aigame

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = listOf("Player", "Computer")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        textInputLayout.adapter = adapter
        StartGame.setOnClickListener {
            val i = Intent(this, TestActivity::class.java)
            i.putExtra("cp", textInputLayout.selectedItem.toString())
            startActivity(i)
        }


    }
}