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

/**
 * A helper function for reporting unsupported JSON types during deserialization.
 */
fun unsupportedKindType(json: JsonElement): Nothing {
    throw IllegalArgumentException("Unsupported kind type '${json.kindType}'")
}

/**
 * A helper function for reporting numeric values outside supported bounds.
 */
fun <T> valueOutOfRange(value: T): Nothing {
    throw IllegalArgumentException("The value '$value' is out of range")
}

/**
 * A helper function for reporting numeric values outside supported bounds.
 */
fun valueOutOfRange(json: JsonPrimitive): Nothing {
    throw IllegalArgumentException("The value '${json.content}' is out of range")
}

/**
 * A helper function for reporting a missing key in a JSON object.
 */
fun missingKey(key: String): Nothing {
    throw IllegalArgumentException("Missing '$key' key")
}
