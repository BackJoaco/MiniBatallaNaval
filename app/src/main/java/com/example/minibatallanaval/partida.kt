// Partida.kt
package com.example.minibatallanaval

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Partida : AppCompatActivity() {

    lateinit var tablero: GridLayout
    lateinit var restantes: TextView
    lateinit var movimientos: TextView
    lateinit var aciertos: TextView
    val botones = mutableListOf<Button>()
    var filas = MainActivity.filas
    var columnas = MainActivity.columnas
    var totalBotones = filas * columnas
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels
    var botonSize = screenWidth / columnas - 10
    var posBarcos = mutableListOf<Int>()
    var cantBarcos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partida)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        filas = MainActivity.filas
        columnas = MainActivity.columnas
        totalBotones = filas * columnas
        botonSize = screenWidth / columnas - 10

        tablero = findViewById(R.id.tablero)
        movimientos = findViewById(R.id.infoMovimientos)
        aciertos = findViewById(R.id.infoAciertos)
        restantes = findViewById(R.id.infoRestantes)
        val nombreJugadorTextView = findViewById<TextView>(R.id.nombreJugador)
        nombreJugadorTextView.text = "Jugador: ${MainActivity.nombreJugador}"

        tablero.columnCount = columnas

        cargarTablero()
    }


    fun generarPosiciones(c: Int): List<Int> {
        return (0 until totalBotones).shuffled().take(c)
    }

    val listener = View.OnClickListener { v ->
        val boton = v as Button
        val pos = boton.text.toString().toInt() - 1
        boton.setBackgroundColor(disparar(pos))
        boton.isEnabled = false
    }

    fun deshabilitarTablero() {
        botones.forEach { it.isEnabled = false }
    }

    fun acertoDisparo() {
        movimientos.text = (movimientos.text.toString().toInt() + 1).toString()
        aciertos.text = (aciertos.text.toString().toInt() + 1).toString()
        val cant = restantes.text.toString().toInt()
        restantes.text = (cant - 1).toString()
        if (cant == 1) {
            deshabilitarTablero()
            mostrarResumen()
        }
    }


    fun falloDisparo() {
        val valor = movimientos.text.toString().toInt() + 1
        movimientos.text = valor.toString()
        if (valor == totalBotones) deshabilitarTablero()
    }

    fun disparar(pos: Int): Int {
        return if (posBarcos.contains(pos)) {
            acertoDisparo()
            ContextCompat.getColor(this, R.color.brown)
        } else {
            falloDisparo()
            ContextCompat.getColor(this, R.color.blue)
        }
    }

    fun cargarTablero() {
        tablero.removeAllViews()
        botones.clear()
        totalBotones = filas * columnas
        botonSize = screenWidth / columnas - 10
        cantBarcos = ((totalBotones / 3)..(totalBotones / 2)).random()
        posBarcos = generarPosiciones(cantBarcos).toMutableList()

        for (i in 1..totalBotones) {
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

    fun reiniciar(view: View) {
        cargarTablero()
    }

    fun volverAlMenu(view: View) {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }

    fun abrirAyuda(view: View) {
        val i = Intent(this, ayuda::class.java)
        startActivity(i)
    }
    fun mostrarResumen() {
        val totalMovimientos = movimientos.text.toString()
        val totalAciertos = aciertos.text.toString()
        val totalRestantes = restantes.text.toString()

        val mensaje = """
        Aciertos: $totalAciertos
        Fallos: ${totalMovimientos.toInt() - totalAciertos.toInt()}
        Movimientos: $totalMovimientos
        Barcos hundidos: ${cantBarcos - totalRestantes.toInt()} / $cantBarcos
    """.trimIndent()

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Resumen de la partida")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .create()

        dialog.show()
    }

}
