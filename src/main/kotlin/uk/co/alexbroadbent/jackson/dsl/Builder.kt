package uk.co.alexbroadbent.jackson.dsl

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper = jacksonObjectMapper()

@DslMarker
annotation class JsonMarker

@JsonMarker
abstract class JacksonObject(val node: JsonNode) {

    override fun hashCode(): Int = node.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JacksonObject) return false
        return node == other.node
    }

    override fun toString(): String = mapper.writeValueAsString(node)
}

class JsonObject(node: ObjectNode = mapper.createObjectNode()) : JacksonObject(node) {

    fun `object`(key: String, value: JsonObject.() -> Unit) = set(key, JsonObject().apply(value).node)
    fun obj(key: String, value: JsonObject.() -> Unit) = `object`(key, value)

    fun array(key: String, value: JsonArray.() -> Unit) = set(key, JsonArray().apply(value).node)
    fun arr(key: String, value: JsonArray.() -> Unit) = array(key, value)

    fun string(key: String, value: String) = put(key, value)
    fun int(key: String, value: Int) = put(key, value)
    fun long(key: String, value: Long) = put(key, value)
    fun double(key: String, value: Double) = put(key, value)
    fun boolean(key: String, value: Boolean) = put(key, value)

    fun put(key: String, value: String) = set(key, TextNode(value))
    fun put(key: String, value: Int) = set(key, IntNode(value))
    fun put(key: String, value: Long) = set(key, LongNode(value))
    fun put(key: String, value: Double) = set(key, DoubleNode(value))
    fun put(key: String, value: Boolean) = set(key, BooleanNode.valueOf(value))

    private fun set(key: String, value: JsonNode) {
        (node as ObjectNode).replace(key, value)
    }
}


class JsonArray(array: ArrayNode = mapper.createArrayNode()) : JacksonObject(array) {

    fun `object`(value: JsonObject.() -> Unit) = add(JsonObject().apply(value).node)
    fun obj(value: JsonObject.() -> Unit) = `object`(value)

    fun array(value: JsonArray.() -> Unit) = add(JsonArray().apply(value).node)
    fun arr(value: JsonArray.() -> Unit) = array(value)

    // @Deprecated(message = "Changing in favour of add(String)", replaceWith = ReplaceWith("add(value)"))
    fun string(value: String) = add(value)
    fun int(value: Int) = add(value)
    fun long(value: Long) = add(value)
    fun double(value: Double) = add(value)
    fun boolean(value: Boolean) = add(value)

    fun add(value: String) = add(TextNode(value))
    fun add(value: Int) = add(IntNode(value))
    fun add(value: Long) = add(LongNode(value))
    fun add(value: Double) = add(DoubleNode(value))
    fun add(value: Boolean) = add(BooleanNode.valueOf(value))

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
