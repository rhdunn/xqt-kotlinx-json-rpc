// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.enumeration

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.test.DisplayName
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals

@JvmInline
private value class UIntEnum(override val kind: UInt) : JsonEnumeration<UInt> {
    companion object {
        val One: UIntEnum = UIntEnum(1u)
        val Two: UIntEnum = UIntEnum(2u)
    }
}

@DisplayName("A UInt-based enumeration type")
class AUIntBasedEnumerationType {
    @Test
    @DisplayName("supports new instances")
    fun supports_new_instances() {
        assertEquals(UIntEnum.One, UIntEnum(1u))
        assertEquals(UIntEnum.Two, UIntEnum(2u))
    }

    @Test
    @DisplayName("supports the kind property")
    fun supports_the_kind_property() {
        assertEquals(1u, UIntEnum.One.kind)
        assertEquals(2u, UIntEnum.Two.kind)
    }
}
