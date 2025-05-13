package com.example.minibatallanaval

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider


class Partida : AppCompatActivity() {

    lateinit var tablero : GridLayout
    lateinit var restantes : TextView
    lateinit var movimientos : TextView
    lateinit var aciertos : TextView
    val botones = mutableListOf<Button>()
    var filas = 6
    var columnas = 6
    var totalBotones= 36
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels
    var botonSize = screenWidth / columnas - 10
    var posBarcos = mutableListOf<Int>()
    var cantBarcos = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_partida)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        tablero = findViewById<GridLayout>(R.id.tablero)
        movimientos = findViewById<TextView>(R.id.infoMovimientos)
        aciertos = findViewById<TextView>(R.id.infoAciertos)
        restantes = findViewById<TextView>(R.id.infoRestantes)
        cargarTablero()

    }

    fun generarPosiciones(c : Int): List<Int>{
        return (0 until totalBotones).shuffled().take(c)

    }
    val listener = View.OnClickListener { v ->
        val boton = v as Button
        val pos = boton.text.toString().toInt() - 1
        boton.setBackgroundColor(disparar(pos))
        boton.isEnabled = false
    }
    fun deshabilitarTablero(){
        botones.forEach { it.isEnabled = false }
    }
    @SuppressLint("SetTextI18n")
    fun acertoDisparo(){
        movimientos.text = (movimientos.text.toString().toInt() +1).toString()
        aciertos.text = (aciertos.text.toString().toInt() +1).toString()
        val cant = restantes.text.toString()
        restantes.text = (cant.toInt() -1).toString()
        if (cant == "1") {
            deshabilitarTablero()
        }
    }
    @SuppressLint("SetTextI18n")
    fun falloDisparo(){
        val valor = movimientos.text.toString().toInt() + 1
        movimientos.text = valor.toString()
        if (valor == 36 ) {
            deshabilitarTablero()
        }
    }

    fun disparar(pos : Int): Int{
        if (posBarcos.contains(pos)) {
            acertoDisparo()
            return ContextCompat.getColor(this, R.color.brown) // hay barco
        } else {
            falloDisparo()
            return ContextCompat.getColor(this, R.color.blue) // agua
        }
    }
    fun cargarTablero(){
        tablero.removeAllViews()
        botones.clear()
        cantBarcos = (10..15).random()
        posBarcos = generarPosiciones(cantBarcos).toMutableList()
        for (i in 1 .. 36) {
            val boton = Button(this)
            boton.text = i.toString()


            val params = GridLayout.LayoutParams()
            params.width = botonSize
            params.height = botonSize
            boton.layoutParams = params
            boton.setBackgroundResource(R.drawable.boton_cuadrado)
            boton.setOnClickListener(listener)

            botones.add(boton)
            tablero.addView(boton)
        }
        movimientos.text = "0"
        aciertos.text = "0"
        restantes.text = "$cantBarcos"
    }
    fun reiniciar(view : View){
        cargarTablero()
    }
}