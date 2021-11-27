package uk.co.alexbroadbent.jackson.dsl

import com.fasterxml.jackson.databind.node.ObjectNode
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.matchers.shouldBe

class BuilderSpec : ShouldSpec({
    context("Check simple builders") {
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

            json shouldBe JsonArray(expected)
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

            json shouldBe JsonObject(expected)
        }

        should("equate array toString values") {
            val json = array {
                string("foo")
                int(472)
                boolean(false)
            }

            json.toString() shouldBe """["foo",472,false]"""
        }

        should("equate object toString values") {
            val json = `object` {
                string("foo", "bar")
                int("ham", 472)
                boolean("spam", false)
            }

            json.toString() shouldBe """{"foo":"bar","ham":472,"spam":false}"""
        }
    }

    context("Check nested builders") {
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
    }
}) {

    override fun isolationMode() = IsolationMode.InstancePerTest

    override fun testCaseOrder() = TestCaseOrder.Random
}
