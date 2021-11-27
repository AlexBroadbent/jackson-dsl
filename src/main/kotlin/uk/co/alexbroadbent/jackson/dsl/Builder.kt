package uk.co.alexbroadbent.jackson.dsl

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper = jacksonObjectMapper()

@DslMarker
annotation class JsonMarker

@JsonMarker
abstract class JacksonObject(val node: JsonNode) {

    override fun hashCode(): Int = node.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return (node == (other as JacksonObject).node)
    }

    override fun toString(): String = mapper.writeValueAsString(node)
}

class JsonObject(node: ObjectNode = mapper.createObjectNode()) : JacksonObject(node) {

    fun `object`(key: String, value: JsonObject.() -> Unit) = set(key, JsonObject().apply(value).node)

    fun obj(key: String, value: JsonObject.() -> Unit) = `object`(key, value)

    fun array(key: String, value: JsonArray.() -> Unit) = set(key, JsonArray().apply(value).node)

    fun arr(key: String, value: JsonArray.() -> Unit) = array(key, value)

    fun string(key: String, value: String) = set(key, TextNode(value))

    fun int(key: String, value: Int) = set(key, IntNode(value))

    fun long(key: String, value: Long) = set(key, LongNode(value))

    fun double(key: String, value: Double) = set(key, DoubleNode(value))

    fun boolean(key: String, value: Boolean) = set(key, BooleanNode.valueOf(value))

    private fun set(key: String, value: JsonNode) {
        (node as ObjectNode).replace(key, value)
    }
}

class JsonArray(array: ArrayNode = mapper.createArrayNode()) : JacksonObject(array) {

    fun `object`(value: JsonObject.() -> Unit) = add(JsonObject().apply(value).node)

    fun obj(value: JsonObject.() -> Unit) = `object`(value)

    fun array(value: JsonArray.() -> Unit) = add(JsonArray().apply(value).node)

    fun arr(value: JsonArray.() -> Unit) = array(value)

    fun string(value: String) = add(TextNode(value))

    fun int(value: Int) = add(IntNode(value))

    fun long(value: Long) = add(LongNode(value))

    fun double(value: Double) = add(DoubleNode(value))

    fun boolean(value: Boolean) = add(BooleanNode.valueOf(value))

    private fun add(node: JsonNode) {
        (this.node as ArrayNode).add(node)
    }
}

fun `object`(init: JsonObject.() -> Unit): JsonObject = JsonObject().apply(init)

fun obj(init: JsonObject.() -> Unit): JsonObject =
    `object`(init)

fun array(init: JsonArray.() -> Unit): JsonArray = JsonArray().apply(init)

fun arr(init: JsonArray.() -> Unit): JsonArray =
    array(init)
