<<<<<<< HEAD
// MainActivity.kt
package com.example.minibatallanaval

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        var nombreJugador: String = ""
        var filas = 6
        var columnas = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonesTamanio = listOf(
            findViewById<Button>(R.id.btn6x6),
            findViewById<Button>(R.id.btn8x8),
            findViewById<Button>(R.id.btn10x10)
        )

        botonesTamanio.forEach { it.setBackgroundColor(Color.LTGRAY) }
        findViewById<Button>(R.id.btn6x6).setBackgroundColor(Color.GREEN)
    }

    fun seleccionarTamanio(view: View) {
        val botones = listOf(
            findViewById<Button>(R.id.btn6x6),
            findViewById<Button>(R.id.btn8x8),
            findViewById<Button>(R.id.btn10x10)
        )

        botones.forEach { it.setBackgroundColor(Color.LTGRAY) }
        val botonSeleccionado = view as Button
        botonSeleccionado.setBackgroundColor(Color.GREEN)

        when (botonSeleccionado.id) {
            R.id.btn6x6 -> {
                filas = 6
                columnas = 6
            }
            R.id.btn8x8 -> {
                filas = 8
                columnas = 8
            }
            R.id.btn10x10 -> {
                filas = 10
                columnas = 10
            }
        }
    }

    fun iniciarPartida(view: View) {
        val inputNombre = findViewById<EditText>(R.id.inputNombre)
        nombreJugador = inputNombre.text.toString()
        val i = Intent(this, Partida::class.java)
        startActivity(i)
    }

    fun abrirAyuda(view: View) {
        val i = Intent(this, ayuda::class.java)
        startActivity(i)
    }
=======
package com.example.minibatallanaval

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    fun iniciarPartida(view : View){
        val i = Intent(this, Partida::class.java)
        startActivity(i)
    }
>>>>>>> 1f9fed1f1996a27acb2cf8ecb613ca9575b70069
}