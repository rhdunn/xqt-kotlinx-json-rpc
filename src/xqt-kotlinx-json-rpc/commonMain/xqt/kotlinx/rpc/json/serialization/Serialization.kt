// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*

/**
 * Returns the type of the JSON primitive.
 */
val JsonPrimitive.kindType: String
    get() = when {
        this === JsonNull -> "null"
        isString -> "string"
        content == "true" || content == "false" -> "boolean"
        '.' in content -> "decimal"
        else -> "integer"
    }

/**
 * Returns the type of the JSON element.
 */
val JsonElement.kindType: String
    get() = when (this) {
        is JsonObject -> "object"
        is JsonArray -> "array"
        is JsonPrimitive -> kindType
    }
