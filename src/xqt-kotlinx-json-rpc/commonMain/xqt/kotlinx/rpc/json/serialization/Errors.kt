// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*

/**
 * A helper function for reporting unsupported JSON types during deserialization.
 */
fun emptyArray(): Nothing {
    throw IllegalArgumentException("The array is empty")
}

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
