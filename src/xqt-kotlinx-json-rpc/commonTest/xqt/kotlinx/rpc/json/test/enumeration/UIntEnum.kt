// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.enumeration.JsonUIntEnumerationType
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@JvmInline
private value class UIntEnum(override val kind: UInt) : JsonEnumeration<UInt> {
    companion object : JsonUIntEnumerationType<UIntEnum>() {
        override fun valueOf(value: UInt): UIntEnum = UIntEnum(value)

        val One: UIntEnum = UIntEnum(1u)
        val Two: UIntEnum = UIntEnum(2u)
    }
}

@DisplayName("A UInt-based enumeration type")
@OptIn(ExperimentalSerializationApi::class)
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
    @DisplayName("supports deserializing from UInt")
    fun supports_deserializing_from_uint() {
        assertEquals(UIntEnum.One, UIntEnum.deserialize(1u))
        assertEquals(UIntEnum.Two, UIntEnum.deserialize(2u))
    }

    @Test
    @DisplayName("supports serializing to UInt")
    fun supports_serializing_to_uint() {
        assertEquals(1u, UIntEnum.serializeToValue(UIntEnum.One))
        assertEquals(2u, UIntEnum.serializeToValue(UIntEnum.Two))
    }

    @Test
    @DisplayName("supports deserializing from JSON")
    fun supports_deserializing_from_json() {
        assertEquals(UIntEnum.One, UIntEnum.deserialize(JsonPrimitive(1u)))
        assertEquals(UIntEnum.Two, UIntEnum.deserialize(JsonPrimitive(2u)))
    }

    @Test
    @DisplayName("supports serializing to JSON")
    fun supports_serializing_to_json() {
        assertEquals(JsonPrimitive(1u), UIntEnum.serializeToJson(UIntEnum.One))
        assertEquals(JsonPrimitive(2u), UIntEnum.serializeToJson(UIntEnum.Two))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { UIntEnum.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { UIntEnum.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { UIntEnum.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { UIntEnum.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { UIntEnum.deserialize(JsonPrimitive("1")) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'string'", e5.message)

        val e6 = assertFails { UIntEnum.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
