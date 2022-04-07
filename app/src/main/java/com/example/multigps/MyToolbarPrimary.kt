package com.example.multigps

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MyToolbarPrimary {
    fun show(activities:AppCompatActivity,view: View,title:String,upButton:Boolean, visible: Boolean)
    {
        var mToolbar: Toolbar = view.findViewById(R.id.toolbar)
        activities.setSupportActionBar(view.findViewById(R.id.toolbar))
        activities.supportActionBar?.title = title
        if(!visible)
        {
            mToolbar.visibility = View.GONE
        }
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
        activities.supportActionBar?.title = title
    }
}