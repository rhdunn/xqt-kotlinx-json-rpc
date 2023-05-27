// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.enumeration.JsonStringEnumerationType
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@JvmInline
private value class StringEnum(override val kind: String) : JsonEnumeration<String> {
    override fun toString(): String = kind

    companion object : JsonStringEnumerationType<StringEnum>() {
        override fun valueOf(value: String): StringEnum = StringEnum(value)

        val One: StringEnum = StringEnum("1")
        val Two: StringEnum = StringEnum("2")
    }
}

@DisplayName("A String-based enumeration type")
class AStringBasedEnumerationType {
    @Test
    @DisplayName("supports new instances via constructor")
    fun supports_new_instances_via_constructor() {
        assertEquals(StringEnum.One, StringEnum("1"))
        assertEquals(StringEnum.Two, StringEnum("2"))
    }

    @Test
    @DisplayName("supports new instances via valueOf")
    fun supports_new_instances_via_value_of() {
        assertEquals(StringEnum.One, StringEnum.valueOf("1"))
        assertEquals(StringEnum.Two, StringEnum.valueOf("2"))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals("1", StringEnum.One.kind)
        assertEquals("2", StringEnum.Two.kind)
    }

    @Test
    @DisplayName("supports deserializing from String")
    fun supports_deserializing_from_string() {
        assertEquals(StringEnum.One, StringEnum.deserialize("1"))
        assertEquals(StringEnum.Two, StringEnum.deserialize("2"))
    }

    @Test
    @DisplayName("supports serializing to String")
    fun supports_serializing_to_string() {
        assertEquals("1", StringEnum.serializeToValue(StringEnum.One))
        assertEquals("2", StringEnum.serializeToValue(StringEnum.Two))
    }

    @Test
    @DisplayName("supports deserializing from JSON")
    fun supports_deserializing_from_json() {
        assertEquals(StringEnum.One, StringEnum.deserialize(JsonPrimitive("1")))
        assertEquals(StringEnum.Two, StringEnum.deserialize(JsonPrimitive("2")))
    }

    @Test
    @DisplayName("supports serializing to JSON")
    fun supports_serializing_to_json() {
        assertEquals(JsonPrimitive("1"), StringEnum.serializeToJson(StringEnum.One))
        assertEquals(JsonPrimitive("2"), StringEnum.serializeToJson(StringEnum.Two))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { StringEnum.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { StringEnum.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { StringEnum.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { StringEnum.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { StringEnum.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { StringEnum.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
