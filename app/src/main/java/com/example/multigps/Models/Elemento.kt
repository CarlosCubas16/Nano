package com.example.multigps.Models

import java.io.Serializable

class Elemento : Serializable {
    var id: String? = null
    var tipo_elemento_id: String? = null
    var cliente_id: String? = null
    var nombre: String? = null
    var descripcion: String? = null
    var peso: String? = null
    var edad: String? = null
    var fecha_nacimiento: String? = null
    var altura: String? = null
    var estado: String? = null
    var url_imagen: String? = null
    var nombre_imagen: String? = null
    var estado_asignacion: String? = null
    var tipo_elemento: TipoElemento? = null

    constructor(id:String, tipo_elemento_id:String, cliente_id:String, nombre:String, descripcion:String, peso:String, edad:String, fecha_nacimiento:String, altura:String, estado: String, url_imagen:String, nombre_imagen: String?, estado_asignacion:String, tipo_elemento: TipoElemento)
    {
        this.id = id
        this.tipo_elemento_id = tipo_elemento_id
        this.cliente_id = cliente_id
        this.nombre = nombre
        this.descripcion = descripcion
        this.peso = peso
        this.edad = edad
        this.fecha_nacimiento = fecha_nacimiento
        this.altura = altura
        this.estado = estado
        this.url_imagen = url_imagen
        this.nombre_imagen = nombre_imagen
        this.estado_asignacion = estado_asignacion
        this.tipo_elemento = tipo_elemento
    }
}