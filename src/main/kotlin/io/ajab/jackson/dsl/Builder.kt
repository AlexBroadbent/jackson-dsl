package io.ajab.jackson.dsl

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
        if (javaClass != other?.javaClass) return false
        return (node == (other as JacksonObject).node)
    }

    override fun toString(): String = mapper.writeValueAsString(node)
}

class JsonObject(node: ObjectNode = mapper.createObjectNode()) : JacksonObject(node) {

    fun array(key: String, value: JsonArray.() -> Unit) = set(key, JsonArray().apply(value))

    fun `object`(key: String, value: JsonObject.() -> Unit) = set(key, JsonObject().apply(value))

    fun string(key: String, value: String) = set(key, StringValue(value))

    fun int(key: String, value: Int) = set(key, IntegerValue(value))

    fun long(key: String, value: Long) = set(key, LongValue(value))

    fun double(key: String, value: Double) = set(key, DoubleValue(value))

    fun boolean(key: String, value: Boolean) = set(key, BooleanValue(value))

    private fun set(key: String, value: JacksonObject) {
        (node as ObjectNode).replace(key, value.node)
    }
}

class JsonArray(array: ArrayNode = mapper.createArrayNode()) : JacksonObject(array) {

    fun `object`(value: JsonObject.() -> Unit) = add(JsonObject().apply(value))

    fun array(value: JsonArray.() -> Unit) = add(JsonArray().apply(value))

    fun string(value: String) = add(StringValue(value))

    fun int(value: Int) = add(IntegerValue(value))

    fun long(value: Long) = add(LongValue(value))

    fun double(value: Double) = add(DoubleValue(value))

    fun boolean(value: Boolean) = add(BooleanValue(value))

    private fun add(node: JacksonObject) {
        (this.node as ArrayNode).add(node.node)
    }
}

abstract class JsonPrimitive(baseNode: JsonNode) : JacksonObject(baseNode)

class StringValue(value: String) : JsonPrimitive(TextNode(value))

class IntegerValue(value: Int) : JsonPrimitive(IntNode(value))

class LongValue(value: Long) : JsonPrimitive(LongNode(value))

class DoubleValue(value: Double) : JsonPrimitive(DoubleNode(value))

class BooleanValue(value: Boolean) : JsonPrimitive(BooleanNode.valueOf(value))

fun `object`(init: JsonObject.() -> Unit): JsonObject = JsonObject().apply(init)

fun array(init: JsonArray.() -> Unit): JsonArray = JsonArray().apply(init)
