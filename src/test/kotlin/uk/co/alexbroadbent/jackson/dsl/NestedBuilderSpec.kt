package uk.co.alexbroadbent.jackson.dsl

import com.fasterxml.jackson.databind.node.ObjectNode
import io.kotest.matchers.shouldBe

class NestedBuilderSpec : BaseSpec({
    should("build mixed array") {
        val json = array {
            string("foo")
            array {
                string("bar")
                `object` {
                    string("two", "three")
                }
            }
        }

        val expected = mapper.createArrayNode()
            .add("foo")
            .add(
                mapper.createArrayNode()
                    .add("bar")
                    .add(
                        mapper.createObjectNode()
                            .put("two", "three")
                    )
            )

        json shouldBe JsonArray(expected)
    }

    should("build mixed object") {
        val json = `object` {
            int("one", 987)
            `object`("two") {
                boolean("three", false)
                array("four") {
                    string("foobar")
                }
            }
        }

        val expected = mapper.createObjectNode()
            .put("one", 987)
            .set<ObjectNode>(
                "two", mapper.createObjectNode()
                    .put("three", false)
                    .set("four", mapper.createArrayNode().add("foobar"))
            )

        json shouldBe JsonObject(expected as ObjectNode)
    }

    should("build with shortened names object") {
        val json = obj {
            int("one", 213)
            arr("two") {
                boolean(false)
                obj {
                    string("three", "bar")
                }
            }
            obj("four") {
                double("five", 3.14)
            }
        }

        val expected = mapper.createObjectNode()
            .put("one", 213)
            .set<ObjectNode>(
                "two", mapper.createArrayNode()
                    .add(false)
                    .add(
                        mapper.createObjectNode()
                            .put("three", "bar")
                    )
            )
            .set<ObjectNode>(
                "four", mapper.createObjectNode()
                    .put("five", 3.14)
            )

        json shouldBe JsonObject(expected)
    }

    should("build with shortened names array") {
        val json = arr {
            int(213)
            arr {
                boolean(false)
            }
            obj {
                double("one", 3.14)
            }
        }

        val expected = mapper.createArrayNode()
            .add(213)
            .add(
                mapper.createArrayNode()
                    .add(false)
            )
            .add(
                mapper.createObjectNode()
                    .put("one", 3.14)
            )

        json shouldBe JsonArray(expected)
    }
})
