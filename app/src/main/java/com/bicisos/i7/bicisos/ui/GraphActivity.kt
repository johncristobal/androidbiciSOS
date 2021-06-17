package com.bicisos.i7.bicisos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.bicisos.i7.bicisos.Fragments.PhotosBikerFragment
import com.bicisos.i7.bicisos.R

class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
    }
}

/*
    falta detalles de diseno y ajuste precios
    loading al darle click en finish
    agregar imagne para comprobante / subir nueva foto / mostrar foto

    login que obtenga polizas o muestre error
        progress

    vista con lista de polizas...
        imagen top
        estas son tus polizas
        click en ojo para ver pdf
        boton bottom emergencia
        ocultar polizas no activas
        formato fecha
        contraseña => numero de folio de la poliza
            tutorial
 */
