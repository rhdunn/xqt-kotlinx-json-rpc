// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import xqt.kotlinx.rpc.json.serialization.StringSerialization

/**
 * Defines a string.
 */
object JsonString : StringSerialization<String> {
    override fun serializeToString(value: String): String = value

    override fun deserialize(value: String): String = value
}
