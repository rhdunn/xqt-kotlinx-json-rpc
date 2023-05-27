// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class DoubleEnum(override val kind: Double) : JsonEnumeration<Double> {
    companion object {
        val One: DoubleEnum = DoubleEnum(1.0)
        val Two: DoubleEnum = DoubleEnum(2.0)
    }
}

@DisplayName("A Double-based enumeration type")
class ADoubleBasedEnumerationType {
    @Test
    @DisplayName("supports new instances")
    fun supports_new_instances() {
        assertEquals(DoubleEnum.One, DoubleEnum(1.0))
        assertEquals(DoubleEnum.Two, DoubleEnum(2.0))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals(1.0, DoubleEnum.One.kind)
        assertEquals(2.0, DoubleEnum.Two.kind)
    }
}
