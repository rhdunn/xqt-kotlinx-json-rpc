// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import xqt.kotlinx.rpc.json.serialization.*
import xqt.kotlinx.rpc.json.serialization.types.JsonArrayOrObject
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.rpc.json.serialization.types.JsonString

/**
 * A request message to describe a request between the client and the server.
 *
 * Every processed request must send a response back to the sender of the request.
 *
 * @see <a href="https://www.jsonrpc.org/specification#request_object">JSON-RPC 2.0 Request object</a>
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#requestMessage">LSP 3.17 RequestMessage</a>
 */
data class RequestObject(
    /**
     * The method to be invoked.
     *
     * Method names that begin with the word rpc followed by a period character
     * (`.`) are reserved for rpc-internal methods and extensions and *must not*
     * be used for anything else.
     */
    val method: String,

    /**
     * The request id.
     */
    val id: JsonIntOrString,

    /**
     * The method's parameters.
     */
    val params: JsonElement? = null,

    override val jsonrpc: String = Message.JSON_RPC_2_0
) : Message {
    constructor(method: String, id: Int, params: JsonElement? = null) :
            this(method, JsonIntOrString.IntegerValue(id), params)

    constructor(method: String, id: String, params: JsonElement? = null) :
            this(method, JsonIntOrString.StringValue(id), params)

    /**
     * The JSON-RPC channel that the message originated from.
     */
    var channel: JsonRpcServer? = null

    companion object : JsonObjectType<RequestObject> {
        override fun instanceOf(json: JsonElement): Boolean {
            return json.containsKeys("jsonrpc", "method", "id") && (json as JsonObject)["id"] != JsonNull
        }

        override fun serializeToJson(value: RequestObject): JsonElement = buildJsonObject {
            put("jsonrpc", value.jsonrpc, JsonString)
            put("method", value.method, JsonString)
            put("id", value.id, JsonIntOrString)
            putOptional("params", value.params, JsonArrayOrObject)
        }

        override fun deserialize(json: JsonElement): RequestObject = when (json) {
            !is JsonObject -> unsupportedKindType(json)
            else -> RequestObject(
                jsonrpc = json.get("jsonrpc", JsonString),
                method = json.get("method", JsonString),
                id = json.get("id", JsonIntOrString),
                params = json.getOptional("params", JsonArrayOrObject)
            )
        }
    }
}

/**
 * Parse the request parameters.
 *
 * @throws InvalidParams if the `params` property is missing, or cannot be converted to
 *         the specified type
 * @param serializer how to deserialize the JSON element value
 * @return the deserialized JSON object
 */
fun <T> RequestObject.params(serializer: JsonSerialization<T>): T {
    if (params == null) throw InvalidParams("Missing params object")
    try {
        return serializer.deserialize(params)
    } catch (e: JsonDeserializationException) {
        throw InvalidParams(e.message)
    }
}

/**
 * Send a request to the channel the message originated from.
 */
fun JsonRpcServer.sendRequest(request: RequestObject) {
    send(RequestObject.serializeToJson(request))
}

/**
 * Send a request to the channel the message originated from.
 *
 * @param method the method to be invoked
 * @param params the method's parameters
 */
fun JsonRpcServer.sendRequest(method: String, params: JsonElement? = null) {
    val request = RequestObject(
        method = method,
        id = nextRequestId,
        params = params
    )
    send(RequestObject.serializeToJson(request))
}

/**
 * Processes a JSON-RPC request message.
 */
fun Message.request(handler: RequestObject.() -> Unit) {
    if (this is RequestObject) {
        this.handler()
    }
}

/**
 * A notification message.
 *
 * A processed notification message must not send a response back. They work like events.
 *
 * @see <a href="https://www.jsonrpc.org/specification#notification">JSON-RPC 2.0 Notification</a>
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#notificationMessage">LSP 3.17 NotificationMessage</a>
 */
data class Notification(
    /**
     * The method to be invoked.
     *
     * Method names that begin with the word rpc followed by a period character
     * (`.`) are reserved for rpc-internal methods and extensions and *must not*
     * be used for anything else.
     */
    val method: String,

    /**
     * The method's parameters.
     */
    val params: JsonElement? = null,

    override val jsonrpc: String = Message.JSON_RPC_2_0
) : Message {
    companion object : JsonObjectType<Notification> {
        override fun instanceOf(json: JsonElement): Boolean = when {
            json.containsKeys("id") -> {
                (json as JsonObject)["id"] == JsonNull && json.containsKeys("jsonrpc", "method")
            }

            else -> json.containsKeys("jsonrpc", "method")
        }

        override fun serializeToJson(value: Notification): JsonElement = buildJsonObject {
            put("jsonrpc", value.jsonrpc, JsonString)
            put("method", value.method, JsonString)
            putOptional("params", value.params, JsonArrayOrObject)
        }

        override fun deserialize(json: JsonElement): Notification = when (json) {
            !is JsonObject -> unsupportedKindType(json)
            else -> Notification(
                jsonrpc = json.get("jsonrpc", JsonString),
                method = json.get("method", JsonString),
                params = json.getOptional("params", JsonArrayOrObject)
            )
        }
    }
}

/**
 * Parse the notification parameters.
 *
 * @throws InvalidParams if the `params` property is missing, or cannot be converted to
 *         the specified type
 * @param serializer how to deserialize the JSON element value
 * @return the deserialized JSON object
 */
fun <T> Notification.params(serializer: JsonSerialization<T>): T {
    if (params == null) throw InvalidParams("Missing params object")
    try {
        return serializer.deserialize(params)
    } catch (e: JsonDeserializationException) {
        throw InvalidParams(e.message)
    }
}

/**
 * Send a notification to the channel the message originated from.
 */
fun JsonRpcServer.sendNotification(notification: Notification) {
    send(Notification.serializeToJson(notification))
}

/**
 * Send a notification to the channel the message originated from.
 *
 * Method names that begin with the word rpc followed by a period character
 * (`.`) are reserved for rpc-internal methods and extensions and *must not*
 * be used for anything else.
 *
 * @param method the method to be invoked
 * @param params the method's parameters
 */
fun JsonRpcServer.sendNotification(method: String, params: JsonElement? = null) {
    val notification = Notification(
        method = method,
        params = params
    )
    send(Notification.serializeToJson(notification))
}

/**
 * Processes a JSON-RPC notification message.
 */
fun Message.notification(handler: Notification.() -> Unit) {
    if (this is Notification) {
        this.handler()
    }
}
