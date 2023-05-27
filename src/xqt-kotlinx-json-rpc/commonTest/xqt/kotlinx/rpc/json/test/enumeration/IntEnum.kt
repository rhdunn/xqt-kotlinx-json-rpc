// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class IntEnum(override val kind: Int) : JsonEnumeration<Int> {
    companion object {
        val One: IntEnum = IntEnum(1)
        val Two: IntEnum = IntEnum(2)
    }
}

@DisplayName("An Int-based enumeration type")
class AnIntBasedEnumerationType {
    @Test
    @DisplayName("supports new instances")
    fun supports_new_instances() {
        assertEquals(IntEnum.One, IntEnum(1))
        assertEquals(IntEnum.Two, IntEnum(2))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals(1, IntEnum.One.kind)
        assertEquals(2, IntEnum.Two.kind)
    }
}
