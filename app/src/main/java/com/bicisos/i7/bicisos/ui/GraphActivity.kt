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

    *** server nodeJS
        * Login google / facebook / mail&pass node.js
            - igual con pass y correo (registro)
            - jwa token
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
