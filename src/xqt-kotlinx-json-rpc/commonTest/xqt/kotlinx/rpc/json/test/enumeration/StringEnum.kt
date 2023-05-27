// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class StringEnum(override val kind: String) : JsonEnumeration<String> {
    companion object : JsonEnumerationType<StringEnum, String> {
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
    @DisplayName("supports deserializing String to StringEnum")
    fun supports_deserializing_string_to_enum() {
        assertEquals(StringEnum.One, StringEnum.deserialize("1"))
        assertEquals(StringEnum.Two, StringEnum.deserialize("2"))
    }

    @Test
    @DisplayName("supports serializing StringEnum to String")
    fun supports_serializing_enum_to_string() {
        assertEquals("1", StringEnum.serializeToValue(StringEnum.One))
        assertEquals("2", StringEnum.serializeToValue(StringEnum.Two))
    }
}
