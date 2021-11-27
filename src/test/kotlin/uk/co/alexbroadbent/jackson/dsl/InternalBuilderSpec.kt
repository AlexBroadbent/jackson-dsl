package uk.co.alexbroadbent.jackson.dsl

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class InternalBuilderSpec : BaseSpec({
    should("JsonObject hashcode equality") {
        val obj1 = `object` { string("foo", "bar") }
        val obj2 = `object` { string("foo", "bar") }

        obj1.hashCode() shouldBe obj2.hashCode()
    }

    should("JsonObject equality branches") {
        val obj1 = `object` { string("foo", "bar") }
        val obj2 = `object` { string("bar", "foo") }
        val arr1 = array { string("foo") }

        (obj1 == obj1) shouldBe true
        obj1 shouldNotBe obj2
        obj1 shouldNotBe null
        obj1 shouldNotBe "foo"
        obj1 shouldNotBe arr1
    }
})
