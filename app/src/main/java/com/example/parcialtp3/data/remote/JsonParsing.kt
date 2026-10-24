package com.example.parcialtp3.data.remote

import com.google.gson.JsonElement

/*
 * Lectores tolerantes para campos del detalle de entrenamiento cuyo formato exacto todavia no esta
 * confirmado (por ejemplo, repeticiones como 12 o "8-12"). Cuando el contrato quede fijo en
 * WorkoutDto, estos helpers se pueden reemplazar por tipos simples (Int, String).
 */

/** Texto de un valor JSON: numeros enteros sin ".0", cadenas recortadas, listas unidas con coma. */
fun JsonElement?.asTextOrNull(): String? {
    if (this == null || this.isJsonNull) return null
    if (this.isJsonPrimitive) {
        val primitive = this.asJsonPrimitive
        if (primitive.isNumber) {
            val number = primitive.asDouble
            return if (number % 1.0 == 0.0) number.toLong().toString() else number.toString()
        }
        val text = primitive.asString.trim()
        return if (text.isEmpty()) null else text
    }
    if (this.isJsonArray) {
        val parts = this.asJsonArray.mapNotNull { it.asTextOrNull() }
        return if (parts.isEmpty()) null else parts.joinToString(", ")
    }
    return null
}

/** Entero de un valor JSON: acepta 90, 90.0 o "90s" (toma el primer numero que aparezca). */
fun JsonElement?.asIntOrNull(): Int? {
    if (this == null || this.isJsonNull || !this.isJsonPrimitive) return null
    val primitive = this.asJsonPrimitive
    if (primitive.isNumber) return primitive.asDouble.toInt()
    return Regex("\\d+").find(primitive.asString)?.value?.toIntOrNull()
}

/** Lista de pasos: acepta una lista de textos, una lista de objetos con "text" o un texto con saltos de linea. */
fun JsonElement?.asTextListOrEmpty(): List<String> {
    if (this == null || this.isJsonNull) return emptyList()
    if (this.isJsonArray) return this.asJsonArray.flatMap { it.asTextListOrEmpty() }
    if (this.isJsonObject) {
        val obj = this.asJsonObject
        for (key in listOf("text", "description", "instruction", "step")) {
            val value = obj.get(key).asTextOrNull()
            if (value != null) return listOf(value)
        }
        return emptyList()
    }
    val text = this.asTextOrNull() ?: return emptyList()
    return text.lines().map { it.trim() }.filter { it.isNotEmpty() }
}
