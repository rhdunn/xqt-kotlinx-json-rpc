// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class UIntEnum(override val kind: UInt) : JsonEnumeration<UInt> {
    companion object : JsonEnumerationType<UIntEnum, UInt> {
        override fun valueOf(value: UInt): UIntEnum = UIntEnum(value)

        val One: UIntEnum = UIntEnum(1u)
        val Two: UIntEnum = UIntEnum(2u)
    }
}

@DisplayName("A UInt-based enumeration type")
class AUIntBasedEnumerationType {
    @Test
    @DisplayName("supports new instances via constructor")
    fun supports_new_instances_via_constructor() {
        assertEquals(UIntEnum.One, UIntEnum(1u))
        assertEquals(UIntEnum.Two, UIntEnum(2u))
    }

    @Test
    @DisplayName("supports new instances via valueOf")
    fun supports_new_instances_via_value_of() {
        assertEquals(UIntEnum.One, UIntEnum.valueOf(1u))
        assertEquals(UIntEnum.Two, UIntEnum.valueOf(2u))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals(1u, UIntEnum.One.kind)
        assertEquals(2u, UIntEnum.Two.kind)
    }

    @Test
    @DisplayName("supports deserializing UInt to UIntEnum")
    fun supports_deserializing_uint_to_enum() {
        assertEquals(UIntEnum.One, UIntEnum.deserialize(1u))
        assertEquals(UIntEnum.Two, UIntEnum.deserialize(2u))
    }

    @Test
    @DisplayName("supports serializing IntEnum to UInt")
    fun supports_serializing_enum_to_uint() {
        assertEquals(1u, UIntEnum.serializeToValue(UIntEnum.One))
        assertEquals(2u, UIntEnum.serializeToValue(UIntEnum.Two))
    }
}
