// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*
import xqt.kotlinx.rpc.json.serialization.types.JsonString

/**
 * An error during `JsonSerialization.deserialize`.
 */
open class JsonDeserializationException(message: String) :
    RuntimeException(message)

/**
 * A non-empty JSON array is empty.
 */
class EmptyArrayException :
    JsonDeserializationException("The array is empty")

/**
 * A missing key in a JSON object.
 */
class MissingKeyException(key: String) :
    JsonDeserializationException("Missing $key key")

/**
 * A conflicting key in a JSON object.
 */
class ConflictingKeyException(key: String) :
    JsonDeserializationException("Conflicting $key key")

/**
 * The JSON kind type is not supported.
 */
class UnsupportedKindTypeException(kindType: KindType) :
    JsonDeserializationException("Unsupported kind type '${kindType.kindName}'")

/**
 * The `kind` key type is not supported.
 */
class UnsupportedKindValueException(kind: String) :
    JsonDeserializationException("Unsupported kind property value '$kind'")

/**
 * The `value` is out of range.
 */
class ValueOutOfRangeException(value: String) :
    JsonDeserializationException("The value '$value' is out of range")

/**
 * Throws a non-empty array is empty exception.
 */
fun emptyArray(): Nothing = throw EmptyArrayException()

/**
 * Throws a missing key in a JSON object exception.
 */
fun missingKey(vararg key: String): Nothing {
    val keyNames = key.withIndex().joinToString(", ") { (index, name) ->
        if (index == key.size - 1) "or '$name'" else "'$name'"
    }
    throw MissingKeyException(keyNames)
}

/**
 * Throws a missing key in a JSON object exception.
 */
fun missingKey(key: String): Nothing = throw MissingKeyException("'$key'")

/**
 * Throws a conflicting key in a JSON object exception.
 */
fun conflictingKey(vararg key: String): Nothing {
    val keyNames = key.withIndex().joinToString(", ") { (index, name) ->
        if (index == key.size - 1) "or '$name'" else "'$name'"
    }
    throw ConflictingKeyException(keyNames)
}

/**
 * Throws a JSON kind type is not supported exception.
 */
fun unsupportedKindType(json: JsonElement): Nothing = throw UnsupportedKindTypeException(json.kindType)

/**
 * Throws a JSON kind type is not supported exception.
 */
fun unsupportedKindType(kindType: KindType): Nothing = throw UnsupportedKindTypeException(kindType)

/**
 * Throws a kind key type is not supported exception.
 */
fun unsupportedKindValue(kind: String): Nothing = throw UnsupportedKindValueException(kind)

/**
 * Throws a kind key type is not supported exception.
 */
fun unsupportedKindValue(json: JsonObject): Nothing {
    unsupportedKindValue(json.get("kind", JsonString))
}

/**
 * Throws a value is out of range exception.
 */
fun <T> valueOutOfRange(value: T): Nothing = throw ValueOutOfRangeException(value.toString())

/**
 * Throws a value is out of range exception.
 */
fun valueOutOfRange(json: JsonPrimitive): Nothing = throw ValueOutOfRangeException(json.content)
