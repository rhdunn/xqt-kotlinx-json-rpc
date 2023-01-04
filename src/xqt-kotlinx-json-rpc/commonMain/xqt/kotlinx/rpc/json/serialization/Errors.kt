// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*
import xqt.kotlinx.rpc.json.serialization.types.JsonString

/**
 * An error during `JsonSerialization.deserialize`.
 */
open class JsonDeserializationException(message: String) : RuntimeException(message)

/**
 * A non-empty JSON array is empty.
 */
class EmptyArrayException : JsonDeserializationException("The array is empty")

/**
 * Throws a non-empty array is empty exception.
 */
fun emptyArray(): Nothing = throw EmptyArrayException()

/**
 * A helper function for reporting a missing key in a JSON object.
 */
fun missingKey(vararg key: String): Nothing {
    val keyNames = key.withIndex().joinToString(", ") { (index, name) ->
        if (index == key.size - 1) "or '$name'" else "'$name'"
    }
    throw IllegalArgumentException("Missing $keyNames key")
}

/**
 * A helper function for reporting a missing key in a JSON object.
 */
fun missingKey(key: String): Nothing {
    throw IllegalArgumentException("Missing '$key' key")
}

/**
 * A helper function for reporting unsupported JSON types during deserialization.
 */
fun unsupportedKindType(json: JsonElement): Nothing = unsupportedKindType(json.kindType)

/**
 * A helper function for reporting unsupported JSON types during deserialization.
 */
fun unsupportedKindType(kindType: KindType): Nothing {
    throw IllegalArgumentException("Unsupported kind type '${kindType.kindName}'")
}

/**
 * A helper function for reporting unsupported JSON types during deserialization.
 */
fun unsupportedKindValue(kind: String): Nothing {
    throw IllegalArgumentException("Unsupported kind property value '$kind'")
}

/**
 * A helper function for reporting unsupported JSON types during deserialization.
 */
fun unsupportedKindValue(json: JsonObject): Nothing {
    unsupportedKindValue(json.get("kind", JsonString))
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
