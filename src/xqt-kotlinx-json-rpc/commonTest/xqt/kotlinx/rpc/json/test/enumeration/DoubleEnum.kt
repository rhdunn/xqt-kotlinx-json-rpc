// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class DoubleEnum(override val kind: Double) : JsonEnumeration<Double> {
    companion object : JsonEnumerationType<DoubleEnum, Double> {
        override fun valueOf(value: Double): DoubleEnum = DoubleEnum(value)

        val One: DoubleEnum = DoubleEnum(1.0)
        val Two: DoubleEnum = DoubleEnum(2.0)
    }
}

@DisplayName("A Double-based enumeration type")
class ADoubleBasedEnumerationType {
    @Test
    @DisplayName("supports new instances via constructor")
    fun supports_new_instances_via_constructor() {
        assertEquals(DoubleEnum.One, DoubleEnum(1.0))
        assertEquals(DoubleEnum.Two, DoubleEnum(2.0))
    }

    @Test
    @DisplayName("supports new instances via valueOf")
    fun supports_new_instances_via_value_of() {
        assertEquals(DoubleEnum.One, DoubleEnum.valueOf(1.0))
        assertEquals(DoubleEnum.Two, DoubleEnum.valueOf(2.0))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals(1.0, DoubleEnum.One.kind)
        assertEquals(2.0, DoubleEnum.Two.kind)
    }

    @Test
    @DisplayName("supports deserializing Double to DoubleEnum")
    fun supports_deserializing_int_to_enum() {
        assertEquals(DoubleEnum.One, DoubleEnum.deserialize(1.0))
        assertEquals(DoubleEnum.Two, DoubleEnum.deserialize(2.0))
    }

    @Test
    @DisplayName("supports serializing DoubleEnum to Double")
    fun supports_serializing_enum_to_double() {
        assertEquals(1.0, DoubleEnum.serializeToValue(DoubleEnum.One))
        assertEquals(2.0, DoubleEnum.serializeToValue(DoubleEnum.Two))
    }
}
