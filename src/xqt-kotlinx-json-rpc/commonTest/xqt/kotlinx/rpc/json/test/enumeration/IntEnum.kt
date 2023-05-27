// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class IntEnum(override val kind: Int) : JsonEnumeration<Int> {
    companion object : JsonEnumerationType<IntEnum, Int> {
        override fun valueOf(value: Int): IntEnum = IntEnum(value)

        val One: IntEnum = IntEnum(1)
        val Two: IntEnum = IntEnum(2)
    }
}

@DisplayName("An Int-based enumeration type")
class AnIntBasedEnumerationType {
    @Test
    @DisplayName("supports new instances via constructor")
    fun supports_new_instances_via_constructor() {
        assertEquals(IntEnum.One, IntEnum(1))
        assertEquals(IntEnum.Two, IntEnum(2))
    }

    @Test
    @DisplayName("supports new instances via valueOf")
    fun supports_new_instances_via_value_of() {
        assertEquals(IntEnum.One, IntEnum.valueOf(1))
        assertEquals(IntEnum.Two, IntEnum.valueOf(2))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals(1, IntEnum.One.kind)
        assertEquals(2, IntEnum.Two.kind)
    }

    @Test
    @DisplayName("supports deserializing Int to IntEnum")
    fun supports_deserializing_int_to_enum() {
        assertEquals(IntEnum.One, IntEnum.deserialize(1))
        assertEquals(IntEnum.Two, IntEnum.deserialize(2))
    }

    @Test
    @DisplayName("supports serializing IntEnum to Int")
    fun supports_serializing_enum_to_int() {
        assertEquals(1, IntEnum.serializeToValue(IntEnum.One))
        assertEquals(2, IntEnum.serializeToValue(IntEnum.Two))
    }
}
