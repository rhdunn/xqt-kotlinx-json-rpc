// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.enumeration.JsonDoubleEnumerationType
import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@JvmInline
private value class DoubleEnum(override val kind: Double) : JsonEnumeration<Double> {
    companion object : JsonDoubleEnumerationType<DoubleEnum>() {
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
    @DisplayName("supports deserializing from Double")
    fun supports_deserializing_from_double() {
        assertEquals(DoubleEnum.One, DoubleEnum.deserialize(1.0))
        assertEquals(DoubleEnum.Two, DoubleEnum.deserialize(2.0))
    }

    @Test
    @DisplayName("supports serializing to Double")
    fun supports_serializing_to_double() {
        assertEquals(1.0, DoubleEnum.serializeToValue(DoubleEnum.One))
        assertEquals(2.0, DoubleEnum.serializeToValue(DoubleEnum.Two))
    }

    @Test
    @DisplayName("supports deserializing from JSON integer")
    fun supports_deserializing_from_json_integer() {
        assertEquals(DoubleEnum.One, DoubleEnum.deserialize(JsonPrimitive(1)))
        assertEquals(DoubleEnum.Two, DoubleEnum.deserialize(JsonPrimitive(2)))
    }

    @Test
    @DisplayName("supports deserializing from JSON decimal")
    fun supports_deserializing_from_json_decimal() {
        assertEquals(DoubleEnum.One, DoubleEnum.deserialize(JsonPrimitive(1.0)))
        assertEquals(DoubleEnum.Two, DoubleEnum.deserialize(JsonPrimitive(2.0)))
    }

    @Test
    @DisplayName("supports serializing to JSON")
    fun supports_serializing_to_json() {
        assertEquals(JsonPrimitive(1.0), DoubleEnum.serializeToJson(DoubleEnum.One))
        assertEquals(JsonPrimitive(2.0), DoubleEnum.serializeToJson(DoubleEnum.Two))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { DoubleEnum.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { DoubleEnum.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { DoubleEnum.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { DoubleEnum.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { DoubleEnum.deserialize(JsonPrimitive("1")) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'string'", e5.message)
    }
}
