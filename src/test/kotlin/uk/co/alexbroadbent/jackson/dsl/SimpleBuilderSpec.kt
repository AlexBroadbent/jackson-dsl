package uk.co.alexbroadbent.jackson.dsl

import io.kotest.matchers.shouldBe

class SimpleBuilderSpec : BaseSpec({
    should("build array") {
        val json = array {
            add("foo")
            add(3)
            add(124789124768219L)
            add(3.14)
            add(false)
        }

        val expected = mapper.createArrayNode()
            .add("foo")
            .add(3)
            .add(124789124768219L)
            .add(3.14)
            .add(false)

        json shouldBe JsonArray(expected)
    }

    should("build object") {
        val json = `object` {
            put("string", "foobar")
            put("int", 123)
            put("long", 718492718496124L)
            put("double", 3.14)
            put("boolean", true)
        }

        val expected = mapper.createObjectNode()
            .put("string", "foobar")
            .put("int", 123)
            .put("long", 718492718496124L)
            .put("double", 3.14)
            .put("boolean", true)

        json shouldBe JsonObject(expected)
    }

    should("equate array toString values") {
        val json = array {
            add("foo")
            add(472)
            add(false)
        }

        json.toString() shouldBe """["foo",472,false]"""
    }

    should("equate object toString values") {
        val json = `object` {
            put("foo", "bar")
            put("ham", 472)
            put("spam", false)
        }

        json.toString() shouldBe """{"foo":"bar","ham":472,"spam":false}"""
    }

    context("deprecated methods") {
        should("build array") {
            val json = array {
                string("foo")
                int(3)
                long(124789124768219L)
                double(3.14)
                boolean(false)
            }

            val expected = mapper.createArrayNode()
                .add("foo")
                .add(3)
                .add(124789124768219L)
                .add(3.14)
                .add(false)

            json.node shouldBe expected
        }

        should("build object") {
            val json = `object` {
                string("string", "foobar")
                int("int", 123)
                long("long", 718492718496124L)
                double("double", 3.14)
                boolean("boolean", true)
            }

            val expected = mapper.createObjectNode()
                .put("string", "foobar")
                .put("int", 123)
                .put("long", 718492718496124L)
                .put("double", 3.14)
                .put("boolean", true)

            json.node shouldBe expected
        }
    }
})
