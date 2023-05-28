// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.enumeration.JsonIntEnumerationType
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@JvmInline
private value class IntEnum(override val kind: Int) : JsonEnumeration<Int> {
    companion object : JsonIntEnumerationType<IntEnum>() {
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
    @DisplayName("supports deserializing from Int")
    fun supports_deserializing_from_int() {
        assertEquals(IntEnum.One, IntEnum.deserialize(1))
        assertEquals(IntEnum.Two, IntEnum.deserialize(2))
    }

    @Test
    @DisplayName("supports serializing to Int")
    fun supports_serializing_to_int() {
        assertEquals(1, IntEnum.serializeToValue(IntEnum.One))
        assertEquals(2, IntEnum.serializeToValue(IntEnum.Two))
    }

    @Test
    @DisplayName("supports deserializing from JSON")
    fun supports_deserializing_from_json() {
        assertEquals(IntEnum.One, IntEnum.deserialize(JsonPrimitive(1)))
        assertEquals(IntEnum.Two, IntEnum.deserialize(JsonPrimitive(2)))
    }

    @Test
    @DisplayName("supports serializing to JSON")
    fun supports_serializing_to_json() {
        assertEquals(JsonPrimitive(1), IntEnum.serializeToJson(IntEnum.One))
        assertEquals(JsonPrimitive(2), IntEnum.serializeToJson(IntEnum.Two))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { IntEnum.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { IntEnum.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { IntEnum.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { IntEnum.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { IntEnum.deserialize(JsonPrimitive("1")) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'string'", e5.message)

        val e6 = assertFails { IntEnum.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
