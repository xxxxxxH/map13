package cn.nba.map13.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BasePage(id:Int):AppCompatActivity(id) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    abstract fun initView()
}