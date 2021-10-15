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
    - comprobnate de pago - buscar imagen

    node js digitalOcean $5 USD al mes

    ===== BACKLOG ======= prioridad =========
    *** app ios
        - mvvm
        - cuenta ios???

    *** reportes bicis
        *** FOTOS DE reporte de robo
        - abrir detalle y obtener las fotos (liga iduser) o deberai ser id de

        *** generalizar el rollo de las fotos...
        - para personaliza falta, ya quedo en reporte

        * crearemos services para subir reportes (solo si login ojo)
           - robo bici 1
           - averia 2
           - ciclovia 3
           - apoyo 4
           - panic 5

        * service para visualizar reportes bicis robadas

    *** server nodeJS
        * al login again - si ya existe, solo ponerlo de nuevo en true
        * logout - quiza poner el usuario en active:false al cerrar sesion
        * contacto msg

    *** app android
        login
            - fondo rojo en progress...
        recycler view - refresh
        * agregar imagne para comprobante / subir nueva foto / mostrar foto
        * password login
            - quiza un tutorial mas a doc
        * cancelar view

    *** web admin
        - angular/react
        - login
        - put contract

 */
