package com.example.proyectotfg

import java.io.Serializable

data class Equipos (
    var Nombre:String = "",
    var Sede:String = "",
    var PaginaWeb:String ="",
    var Foto:String = ""
) : Serializable