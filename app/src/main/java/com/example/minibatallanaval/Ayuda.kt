package com.example.minibatallanaval

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class Ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)
    }
    fun cerrarAyuda(view: View) {
        finish()
    }
}