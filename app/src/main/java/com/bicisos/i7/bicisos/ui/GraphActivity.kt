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
    volver a agregar segundo titular
    quitar rfc y acomoda la fecha nacimienot
    agregar imagenes sillin, pedal, etc
    loading al darle click en finish
    agregar imagne para comprobante / subir nueva foto / mostrar foto
    colocar monto anualidad

    back:
    - guardar json de la contratacion...

    necesitmoas una lista de polizas...
 */