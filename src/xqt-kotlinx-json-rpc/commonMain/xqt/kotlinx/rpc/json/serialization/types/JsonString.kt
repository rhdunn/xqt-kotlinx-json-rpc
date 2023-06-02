// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import xqt.kotlinx.rpc.json.serialization.StringSerialization

/**
 * Defines a string.
 *
 * @since 1.0.0
 */
object JsonString : StringSerialization<String> {
    override fun deserialize(value: String): String = value
}
