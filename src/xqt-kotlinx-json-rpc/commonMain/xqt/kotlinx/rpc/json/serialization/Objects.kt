// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*
import xqt.kotlinx.rpc.json.serialization.types.JsonProperty
import xqt.kotlinx.rpc.json.serialization.types.JsonPropertyState
import xqt.kotlinx.rpc.json.serialization.types.JsonTypedArray
import xqt.kotlinx.rpc.json.serialization.types.JsonTypedObject

/**
 * Returns a new read-only JSON object with the specified contents, given as a
 * list of pairs where the first value is the key and the second is the value.
 *
 * If multiple pairs have the same key, the resulting map will contain the value
 * from the last of those pairs.
 *
 * Entries of the map are iterated in the order they were specified.
 */
fun jsonObjectOf(vararg pairs: Pair<String, JsonElement>): JsonObject = JsonObject(mapOf(*pairs))

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.get(key: String, serializer: JsonSerialization<T>): T {
    return serializer.deserialize(get(key) ?: missingKey(key))
}

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 *
 * @since 1.1.0
 */
fun <T> JsonObject.getProperty(key: String, serializer: JsonSerialization<T>): JsonProperty<T> {
    val value = get(key) ?: return JsonProperty.missing()
    return JsonProperty(serializer.deserializeOrNull(value))
}

/**
 * Deserialize the nullable data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.getNullable(key: String, serializer: JsonSerialization<T>): T? {
    return serializer.deserializeOrNull(get(key) ?: missingKey(key))
}

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.getOptional(key: String, serializer: JsonSerialization<T>): T? {
    return get(key)?.let { serializer.deserialize(it) }
}

/**
 * Deserialize the array of data types or objects from the `json` element.
 *
 * @param key the name of the optional key to deserialize.
 * @param serializer how to deserialize the JSON array value.
 */
fun <T> JsonObject.getOptional(key: String, serializer: JsonTypedArray<T>): List<T> {
    val data = get(key) ?: return listOf()
    return serializer.deserialize(data)
}

/**
 * Deserialize the map of data types from the `json` element.
 *
 * @param key the name of the optional key to deserialize.
 * @param serializer how to deserialize the JSON object value.
 */
fun <K, V> JsonObject.getOptional(key: String, serializer: JsonTypedObject<K, V>): Map<K, V> {
    val data = get(key) ?: return mapOf()
    return serializer.deserialize(data)
}

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
@Deprecated(
    message = "Use get with a JsonProperty type.",
    replaceWith = ReplaceWith("get", "xqt.kotlinx.rpc.json.serialization.get")
)
fun <T> JsonObject.getOptionalOrNullable(key: String, serializer: JsonSerialization<T>): T? {
    return get(key)?.let { serializer.deserializeOrNull(it) }
}

/**
 * Deserialize the array of data types or objects from the `json` element.
 *
 * @param key the name of the optional key to deserialize.
 * @param serializer how to deserialize the JSON array value.
 */
@Deprecated(
    message = "Use get with a JsonProperty type.",
    replaceWith = ReplaceWith("get", "xqt.kotlinx.rpc.json.serialization.get")
)
fun <T> JsonObject.getOptionalOrNullable(key: String, serializer: JsonTypedArray<T>): List<T> {
    val data = get(key) ?: return listOf()
    return serializer.deserializeOrNull(data) ?: listOf()
}

/**
 * Deserialize the map of data types from the `json` element.
 *
 * @param key the name of the optional key to deserialize.
 * @param serializer how to deserialize the JSON object value.
 */
@Deprecated(
    message = "Use get with a JsonProperty type.",
    replaceWith = ReplaceWith("get", "xqt.kotlinx.rpc.json.serialization.get")
)
fun <K, V> JsonObject.getOptionalOrNullable(key: String, serializer: JsonTypedObject<K, V>): Map<K, V> {
    val data = get(key) ?: return mapOf()
    return serializer.deserializeOrNull(data) ?: mapOf()
}

/**
 * Serialize the data type or object to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.put(key: String, value: T, serializer: JsonSerialization<T>) {
    put(key, serializer.serializeToJson(value))
}

/**
 * Serialize the data type or object to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 *
 * @since 1.1.0
 */
fun <T> JsonObjectBuilder.putProperty(key: String, value: JsonProperty<T>, serializer: JsonSerialization<T>) {
    if (value.state == JsonPropertyState.Present) {
        put(key, serializer.serializeToJsonOrNull(value.value))
    }
}

/**
 * Serialize the nullable data type or object to the JSON element.
 *
 * If the value is null, this will add it to the JSON element as a null.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.putNullable(key: String, value: T?, serializer: JsonSerialization<T>) {
    put(key, serializer.serializeToJsonOrNull(value))
}

/**
 * Serialize the data type or object to the JSON element.
 *
 * If the value is null, this will not add it to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.putOptional(key: String, value: T?, serializer: JsonSerialization<T>) {
    value?.let { put(key, serializer.serializeToJson(it)) }
}

/**
 * Serialize the array of data types to the JSON element.
 *
 * If the list is empty, this will not add the array to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON array value.
 */
fun <T> JsonObjectBuilder.putOptional(key: String, value: List<T>, serializer: JsonTypedArray<T>) {
    if (value.isNotEmpty()) {
        put(key, serializer.serializeToJson(value))
    }
}

/**
 * Serialize the map of data types to the JSON element.
 *
 * If the map is empty, this will not add the object to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the map to serialize to.
 * @param serializer how to serialize the JSON map value.
 */
fun <K, V> JsonObjectBuilder.putOptional(key: String, value: Map<K, V>, serializer: JsonTypedObject<K, V>) {
    if (value.isNotEmpty()) {
        put(key, serializer.serializeToJson(value))
    }
}

/**
 * Checks the value of the `kind` property of a JSON object.
 */
fun JsonElement.hasKindKey(value: String): Boolean = when (this) {
    is JsonObject -> this.hasKindKey(value)
    else -> false
}

/**
 * Checks the value of the `kind` property of a JSON object.
 */
fun JsonObject.hasKindKey(value: String): Boolean {
    val kind = get("kind")
    return when {
        kind == null -> false
        kind !is JsonPrimitive -> false
        !kind.isString -> false
        else -> kind.content == value
    }
}

/**
 * Checks the JSON object contains all the specified keys.
 */
fun JsonElement.containsKeys(vararg key: String): Boolean = when (this) {
    is JsonObject -> key.all { containsKey(it) }
    else -> false
}

/**
 * Checks the JSON object contains all the specified keys.
 */
fun JsonObject.containsKeys(vararg key: String): Boolean = key.all { containsKey(it) }
