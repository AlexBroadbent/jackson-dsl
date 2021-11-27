package uk.co.alexbroadbent.jackson.dsl

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.TestCaseOrder

abstract class BaseSpec(
    body: ShouldSpec.() -> Unit
) : ShouldSpec(body) {

    override fun isolationMode() = IsolationMode.InstancePerTest
    override fun testCaseOrder() = TestCaseOrder.Random
}
