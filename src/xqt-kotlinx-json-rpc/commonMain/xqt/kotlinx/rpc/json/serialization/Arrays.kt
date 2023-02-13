// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

/**
 * Returns a new read-only list of given elements.
 */
fun jsonArrayOf(vararg elements: JsonElement): JsonArray = JsonArray(listOf(*elements))
