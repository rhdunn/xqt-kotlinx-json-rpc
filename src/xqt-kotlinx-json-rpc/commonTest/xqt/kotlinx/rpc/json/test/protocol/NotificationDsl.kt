// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.*
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("The notification DSL")
class TheNotificationDSL {
    @Test
    @DisplayName("reports ErrorCode.ParseError on invalid JSON")
    fun reports_parse_error_on_invalid_json() {
        val channel = TestJsonRpcChannel()
        channel.push("{\"jsonrpc\":\"2.0\",method:\"test\"}")

        var called = 0
        channel.jsonRpc {
            ++called
        }

        assertEquals(0, called, "The jsonRpc DSL should not have been called.")
        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonNull,
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(ErrorCode.ParseError.code),
                    "message" to JsonPrimitive("Unexpected JSON token at offset 22: Expected quotation mark '\"', but had 'd' instead at path: \$")
                )
            ),
            channel.output[0]
        )
    }

    @Test
    @DisplayName("reports ErrorCode.InvalidRequest on an invalid Notification")
    fun reports_invalid_request_on_an_invalid_notification() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive(1)
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
        }

        assertEquals(0, called, "The jsonRpc DSL should not have been called.")
        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonNull,
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(ErrorCode.InvalidRequest.code),
                    "message" to JsonPrimitive("Unsupported kind type 'integer'")
                )
            ),
            channel.output[0]
        )
    }

    @Test
    @DisplayName("supports notifications without parameters")
    fun supports_notifications_without_parameters() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                assertEquals(1, called)

                assertEquals("2.0", jsonrpc)
                assertEquals("test", method)
                assertEquals(null, params)
            }
        }

        assertEquals(1, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports notifications with parameters")
    fun supports_notifications_with_parameters() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "params" to jsonArrayOf(JsonPrimitive(1))
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                assertEquals(1, called)

                assertEquals("2.0", jsonrpc)
                assertEquals("test", method)
                assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
            }
        }

        assertEquals(1, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports multiple notification messages")
    fun supports_multiple_notification_messages() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem")
            )
        )
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(1))
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                when (method) {
                    "lorem" -> {
                        assertEquals(1, called)

                        assertEquals("2.0", jsonrpc)
                        assertEquals(null, params)
                    }

                    "ipsum" -> {
                        assertEquals(2, called)

                        assertEquals("2.0", jsonrpc)
                        assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
                    }

                    else -> assertTrue(false, "Unknown notification event: $method")
                }
            }
        }

        assertEquals(2, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports batched notification messages")
    fun supports_batched_notification_messages() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonArrayOf(
                jsonObjectOf(
                    "jsonrpc" to JsonPrimitive("2.0"),
                    "method" to JsonPrimitive("lorem")
                ),
                jsonObjectOf(
                    "jsonrpc" to JsonPrimitive("2.0"),
                    "method" to JsonPrimitive("ipsum"),
                    "params" to jsonArrayOf(JsonPrimitive(1))
                )
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                when (method) {
                    "lorem" -> {
                        assertEquals(1, called)

                        assertEquals("2.0", jsonrpc)
                        assertEquals(null, params)
                    }

                    "ipsum" -> {
                        assertEquals(2, called)

                        assertEquals("2.0", jsonrpc)
                        assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
                    }

                    else -> assertTrue(false, "Unknown notification event: $method")
                }
            }
        }

        assertEquals(2, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports sending notifications without parameters")
    fun supports_sending_notifications_without_parameters() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendNotification(Notification(method = "lorem/ipsum"))
                sendNotification(method = "notify/test")
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem/ipsum")
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("notify/test")
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending notifications with parameters")
    fun supports_sending_notifications_with_parameters() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendNotification(Notification(method = "lorem/ipsum", params = jsonArrayOf(JsonPrimitive(5))))
                sendNotification(method = "notify/test", params = jsonArrayOf(JsonPrimitive(123)))
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem/ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(5))
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("notify/test"),
                "params" to jsonArrayOf(JsonPrimitive(123))
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending requests without parameters for integer|string ids")
    fun supports_sending_requests_without_parameters_for_integer_or_string_ids() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = JsonIntOrString.IntegerValue(1)
                    )
                )
                sendRequest(
                    method = "notify/test",
                    id = JsonIntOrString.IntegerValue(2)
                )
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "method" to JsonPrimitive("lorem/ipsum")
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(2),
                "method" to JsonPrimitive("notify/test")
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending requests without parameters for integer ids")
    fun supports_sending_requests_without_parameters_for_integer_ids() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = JsonIntOrString.IntegerValue(1)
                    )
                )
                sendRequest(
                    method = "notify/test",
                    id = JsonIntOrString.IntegerValue(2)
                )
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "method" to JsonPrimitive("lorem/ipsum")
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(2),
                "method" to JsonPrimitive("notify/test")
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending requests without parameters for string ids")
    fun supports_sending_requests_without_parameters_for_string_ids() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = "one"
                    )
                )
                sendRequest(
                    method = "notify/test",
                    id = "two"
                )
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive("one"),
                "method" to JsonPrimitive("lorem/ipsum")
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive("two"),
                "method" to JsonPrimitive("notify/test")
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending requests with parameters for integer|string ids")
    fun supports_sending_requests_with_parameters_for_integer_or_string_ids() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = JsonIntOrString.IntegerValue(1),
                        params = jsonArrayOf(JsonPrimitive(5))
                    )
                )
                sendRequest(
                    method = "notify/test",
                    id = JsonIntOrString.IntegerValue(2),
                    params = jsonArrayOf(JsonPrimitive(123))
                )
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "method" to JsonPrimitive("lorem/ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(5))
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(2),
                "method" to JsonPrimitive("notify/test"),
                "params" to jsonArrayOf(JsonPrimitive(123))
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending requests with parameters for integer ids")
    fun supports_sending_requests_with_parameters_for_integer_ids() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = 1,
                        params = jsonArrayOf(JsonPrimitive(5))
                    )
                )
                sendRequest(
                    method = "notify/test",
                    id = 2,
                    params = jsonArrayOf(JsonPrimitive(123))
                )
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "method" to JsonPrimitive("lorem/ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(5))
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(2),
                "method" to JsonPrimitive("notify/test"),
                "params" to jsonArrayOf(JsonPrimitive(123))
            ),
            channel.output[1]
        )
    }

    @Test
    @DisplayName("supports sending requests with parameters for string ids")
    fun supports_sending_requests_with_parameters_for_string_ids() {
        val channel = TestJsonRpcChannel()
        channel.push(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        channel.jsonRpc {
            notification {
                sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = "one",
                        params = jsonArrayOf(JsonPrimitive(5))
                    )
                )
                sendRequest(
                    method = "notify/test",
                    id = "two",
                    params = jsonArrayOf(JsonPrimitive(123))
                )
            }
        }

        assertEquals(2, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive("one"),
                "method" to JsonPrimitive("lorem/ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(5))
            ),
            channel.output[0]
        )
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive("two"),
                "method" to JsonPrimitive("notify/test"),
                "params" to jsonArrayOf(JsonPrimitive(123))
            ),
            channel.output[1]
        )
    }
}
