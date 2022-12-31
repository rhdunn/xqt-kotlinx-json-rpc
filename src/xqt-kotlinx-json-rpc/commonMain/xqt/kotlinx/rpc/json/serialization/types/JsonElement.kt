// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import xqt.kotlinx.rpc.json.serialization.JsonSerialization

/**
 * Defines a JSON element.
 */
object JsonElement : JsonSerialization<JsonElement> {
    override fun serialize(value: JsonElement): JsonElement = value

    override fun deserialize(json: JsonElement): JsonElement = json
}
