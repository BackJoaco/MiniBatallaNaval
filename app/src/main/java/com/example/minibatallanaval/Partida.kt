package com.example.minibatallanaval

import android.annotation.SuppressLint
import android.content.Intent
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
    lateinit var nombreView: TextView
    val botones = mutableListOf<Button>()
    var filas = 6
    var columnas = 6
    var totalBotones= 36
    var botonSize = 0
    var posBarcos = mutableListOf<Int>()
    var cantBarcos = 0
    var nombreJugador = ""
    var contadorMovimientos = 0
    var contadorAciertos = 0
    var barcosRestantes = 0  // Hay 15 variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_partida)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Observar el funcionamiento de esto
        nombreJugador = intent.getStringExtra("nombre") ?: "Jugador"
        filas = intent.getIntExtra("filas", 6)
        columnas = intent.getIntExtra("columnas", 6)
        totalBotones = filas * columnas

        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        botonSize = screenWidth / columnas - 10

        tablero = findViewById<GridLayout>(R.id.tablero)
        movimientos = findViewById<TextView>(R.id.textoMovimientos)
        aciertos = findViewById<TextView>(R.id.textoAciertos)
        restantes = findViewById<TextView>(R.id.textoRestantes)
        nombreView = findViewById(R.id.nombreJugador)
        nombreView.text = "Jugador: $nombreJugador"

        tablero.columnCount = columnas
        cargarTablero()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val menuHost: MenuHost = this
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu : Menu, menuInflater : MenuInflater) {
                menuInflater.inflate(R.menu.menu_partida, menu)
            }
            override fun onMenuItemSelected(menuItem : MenuItem) : Boolean {
                return when (menuItem.itemId) {
                    // boton para volver al menu principal
                    R.id.menuAyuda -> {
                        val i = Intent(this@Partida, Ayuda::class.java)
                        startActivity(i)
                        true
                    }
                    // boton de cerrar menu
                    R.id.menuSalir -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
        }, this);
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
        contadorMovimientos++
        contadorAciertos++
        barcosRestantes--

        movimientos.text = "Movimientos: $contadorMovimientos"
        aciertos.text = "Aciertos: $contadorAciertos"
        restantes.text = "Barcos restantes: $barcosRestantes"

        if (barcosRestantes == 0) {
            deshabilitarTablero()
            mostrarEstadisticas()
        }
    }

    @SuppressLint("SetTextI18n")
    fun falloDisparo(){
        contadorMovimientos++
        movimientos.text = "Movimientos: $contadorMovimientos"

        if (contadorMovimientos == totalBotones) {
            deshabilitarTablero()
        }
    }

    fun disparar(pos : Int): Int{
        return if (posBarcos.contains(pos)) {
            acertoDisparo()
            ContextCompat.getColor(this, R.color.brown) // hay barco
        } else {
            falloDisparo()
            ContextCompat.getColor(this, R.color.blue) // agua
        }
    }

    fun cargarTablero(){
        tablero.removeAllViews()
        botones.clear()

        cantBarcos = (10..15).random()
        posBarcos = generarPosiciones(cantBarcos).toMutableList()

        contadorMovimientos = 0
        contadorAciertos = 0
        barcosRestantes = cantBarcos

        movimientos.text = "Movimientos: 0"
        aciertos.text = "Aciertos: 0"
        restantes.text = "Barcos restantes: $barcosRestantes"

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
    }

    fun reiniciar(view : View){
        cargarTablero()
    }

    fun abrirAyuda(view: View) {
        val i = Intent(this, Ayuda::class.java)
        startActivity(i)
    }

    fun mostrarEstadisticas() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("¡Partida terminada!")

        val mensaje = "Jugador: $nombreJugador\n" +
                "Movimientos: $contadorMovimientos\n" +
                "Aciertos: $contadorAciertos\n" +
                "Porcentaje de aciertos: ${calcularPorcentaje()}%"

        builder.setMessage(mensaje)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setNegativeButton("Reiniciar partida") { _, _ ->
            cargarTablero()
        }

        builder.show()
    }

    fun calcularPorcentaje(): Int {
        return if (contadorMovimientos > 0) {
            (contadorAciertos * 100) / contadorMovimientos
        } else {
            0
        }
    }
}