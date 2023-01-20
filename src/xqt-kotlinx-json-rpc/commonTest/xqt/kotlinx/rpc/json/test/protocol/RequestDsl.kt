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

@DisplayName("The request DSL")
class TheRequestDSL {
    @Test
    @DisplayName("supports requests without parameters")
    fun supports_requests_without_parameters() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            request {
                assertEquals(1, called)

                assertEquals("2.0", jsonrpc)
                assertEquals("test", method)
                assertEquals(JsonIntOrString.IntegerValue(1), id)
                assertEquals(null, params)
            }
        }

        assertEquals(1, called, "The request DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports requests with parameters")
    fun supports_requests_with_parameters() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1),
                "params" to jsonArrayOf(JsonPrimitive(1))
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            request {
                assertEquals(1, called)

                assertEquals("2.0", jsonrpc)
                assertEquals("test", method)
                assertEquals(JsonIntOrString.IntegerValue(1), id)
                assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
            }
        }

        assertEquals(1, called, "The request DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports multiple request messages")
    fun supports_multiple_request_messages() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem"),
                "id" to JsonPrimitive(1)
            )
        )
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(1)),
                "id" to JsonPrimitive(2)
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            request {
                when (method) {
                    "lorem" -> {
                        assertEquals(1, called)

                        assertEquals(JsonIntOrString.IntegerValue(1), id)
                        assertEquals("2.0", jsonrpc)
                        assertEquals(null, params)
                    }

                    "ipsum" -> {
                        assertEquals(2, called)

                        assertEquals(JsonIntOrString.IntegerValue(2), id)
                        assertEquals("2.0", jsonrpc)
                        assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
                    }

                    else -> assertTrue(false, "Unknown request event: $method")
                }
            }
        }

        assertEquals(2, called, "The request DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports batched request messages")
    fun supports_batched_request_messages() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonArrayOf(
                jsonObjectOf(
                    "jsonrpc" to JsonPrimitive("2.0"),
                    "method" to JsonPrimitive("lorem"),
                    "id" to JsonPrimitive(1)
                ),
                jsonObjectOf(
                    "jsonrpc" to JsonPrimitive("2.0"),
                    "method" to JsonPrimitive("ipsum"),
                    "params" to jsonArrayOf(JsonPrimitive(1)),
                    "id" to JsonPrimitive(2)
                )
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            request {
                when (method) {
                    "lorem" -> {
                        assertEquals(1, called)

                        assertEquals(JsonIntOrString.IntegerValue(1), id)
                        assertEquals("2.0", jsonrpc)
                        assertEquals(null, params)
                    }

                    "ipsum" -> {
                        assertEquals(2, called)

                        assertEquals(JsonIntOrString.IntegerValue(2), id)
                        assertEquals("2.0", jsonrpc)
                        assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
                    }

                    else -> assertTrue(false, "Unknown request event: $method")
                }
            }
        }

        assertEquals(2, called, "The request DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports sending notifications without parameters")
    fun supports_sending_notifications_without_parameters() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
    @DisplayName("supports sending responses with results")
    fun supports_sending_responses_with_results() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
                sendResponse(ResponseObject(id = id, result = JsonPrimitive("lorem")))
            }
        }

        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "result" to JsonPrimitive("lorem")
            ),
            channel.output[0]
        )
    }

    @Test
    @DisplayName("supports sending responses with null results")
    fun supports_sending_responses_with_null_results() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
                sendResponse(ResponseObject(id = id, result = JsonNull))
            }
        }

        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "result" to JsonNull
            ),
            channel.output[0]
        )
    }

    @Test
    @DisplayName("supports sending responses with errors without data")
    fun supports_sending_responses_with_errors_without_data() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
                sendResponse(
                    ResponseObject(
                        id = id,
                        error = ErrorObject(code = ErrorCode.ParseError, message = "Parse Error")
                    )
                )
            }
        }

        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(-32700),
                    "message" to JsonPrimitive("Parse Error")
                )
            ),
            channel.output[0]
        )
    }

    @Test
    @DisplayName("supports sending responses with errors with data")
    fun supports_sending_responses_with_errors_with_data() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
                sendResponse(
                    ResponseObject(
                        id = id,
                        error = ErrorObject(
                            code = ErrorCode.ParseError,
                            message = "Parse Error",
                            data = JsonPrimitive(123)
                        )
                    )
                )
            }
        }

        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(-32700),
                    "message" to JsonPrimitive("Parse Error"),
                    "data" to JsonPrimitive(123)
                )
            ),
            channel.output[0]
        )
    }

    @Test
    @DisplayName("supports sending requests without parameters for integer|string ids")
    fun supports_sending_requests_without_parameters_for_integer_or_string_ids() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        channel.jsonRpc {
            request {
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
