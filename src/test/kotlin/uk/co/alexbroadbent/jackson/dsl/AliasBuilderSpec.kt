package uk.co.alexbroadbent.jackson.dsl

import io.kotest.matchers.shouldBe

class AliasBuilderSpec : BaseSpec({

    context("object.put") {
        should("accept String parameter") {
            obj { put("foo", "bar") }.node shouldBe mapper.createObjectNode().put("foo", "bar")
        }

        should("accept Int parameter") {
            obj { put("foo", 123) }.node shouldBe mapper.createObjectNode().put("foo", 123)
        }

        should("accept Long parameter") {
            obj { put("foo", 567L) }.node shouldBe mapper.createObjectNode().put("foo", 567L)
        }

        should("accept Double parameter") {
            obj { put("foo", 8.901) }.node shouldBe mapper.createObjectNode().put("foo", 8.901)
        }

        should("accept Boolean parameter") {
            obj { put("foo", false) }.node shouldBe mapper.createObjectNode().put("foo", false)
        }
    }

    context("array.add") {
        should("accept String parameter") {
            arr { add("foo") }.node shouldBe mapper.createArrayNode().add("foo")
        }

        should("accept Int parameter") {
            arr { add(123) }.node shouldBe mapper.createArrayNode().add(123)
        }

        should("accept Long parameter") {
            arr { add(567L) }.node shouldBe mapper.createArrayNode().add(567L)
        }

        should("accept Double parameter") {
            arr { add(8.901) }.node shouldBe mapper.createArrayNode().add(8.901)
        }

        should("accept Boolean parameter") {
            arr { add(false) }.node shouldBe mapper.createArrayNode().add(false)
        }
    }
})
