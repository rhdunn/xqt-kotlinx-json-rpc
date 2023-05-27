// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class StringEnum(override val kind: String) : JsonEnumeration<String> {
    companion object {
        val One: StringEnum = StringEnum("1")
        val Two: StringEnum = StringEnum("2")
    }
}

@DisplayName("A String-based enumeration type")
class AStringBasedEnumerationType {
    @Test
    @DisplayName("supports new instances")
    fun supports_new_instances() {
        assertEquals(StringEnum.One, StringEnum("1"))
        assertEquals(StringEnum.Two, StringEnum("2"))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals("1", StringEnum.One.kind)
        assertEquals("2", StringEnum.Two.kind)
    }
}
