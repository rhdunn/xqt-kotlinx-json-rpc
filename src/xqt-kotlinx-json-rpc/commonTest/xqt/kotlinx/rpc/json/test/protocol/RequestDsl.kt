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
    @DisplayName("reports ErrorCode.ParseError on invalid JSON")
    fun reports_parse_error_on_invalid_json() = testJsonRpc {
        client.send("{\"jsonrpc\":\"2.0\",method:\"test\",\"id\":1}")

        var called = 0
        server.jsonRpc {
            ++called
        }

        assertEquals(0, called, "The jsonRpc DSL should not have been called.")

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonNull,
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(ErrorCode.ParseError.code),
                    "message" to JsonPrimitive("Unexpected JSON token at offset 22: Expected quotation mark '\"', but had 'd' instead at path: \$")
                )
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("reports ErrorCode.InvalidRequest on an invalid RequestObject")
    fun reports_invalid_request_on_an_invalid_request_object() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive(1),
                "id" to JsonPrimitive(1234)
            )
        )

        var called = 0
        server.jsonRpc {
            ++called
        }

        assertEquals(0, called, "The jsonRpc DSL should not have been called.")

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonNull,
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(ErrorCode.InvalidRequest.code),
                    "message" to JsonPrimitive("Unsupported kind type 'integer'")
                )
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("reports ErrorCode.InternalError for generic exceptions")
    fun reports_internal_error_for_generic_exceptions() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                throw RuntimeException("Lorem ipsum")
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(ErrorCode.InternalError.code),
                    "message" to JsonPrimitive("Lorem ipsum")
                )
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports requests without parameters")
    fun supports_requests_without_parameters() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        var called = 0
        server.jsonRpc {
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
    }

    @Test
    @DisplayName("supports requests with parameters")
    fun supports_requests_with_parameters() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1),
                "params" to jsonArrayOf(JsonPrimitive(1))
            )
        )

        var called = 0
        server.jsonRpc {
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
    }

    @Test
    @DisplayName("supports multiple request messages")
    fun supports_multiple_request_messages() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem"),
                "id" to JsonPrimitive(1)
            )
        )
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(1)),
                "id" to JsonPrimitive(2)
            )
        )

        var called = 0
        server.jsonRpc {
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
    }

    @Test
    @DisplayName("supports batched request messages")
    fun supports_batched_request_messages() = testJsonRpc {
        client.send(
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
        server.jsonRpc {
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
    }

    @Test
    @DisplayName("supports sending notifications without parameters")
    fun supports_sending_notifications_without_parameters() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                server.sendNotification(Notification(method = "lorem/ipsum"))
                server.sendNotification(method = "notify/test")
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem/ipsum")
            ),
            client.receive()
        )

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("notify/test")
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending notifications with parameters")
    fun supports_sending_notifications_with_parameters() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                server.sendNotification(Notification(method = "lorem/ipsum", params = jsonArrayOf(JsonPrimitive(5))))
                server.sendNotification(method = "notify/test", params = jsonArrayOf(JsonPrimitive(123)))
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem/ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(5))
            ),
            client.receive()
        )

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("notify/test"),
                "params" to jsonArrayOf(JsonPrimitive(123))
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending responses with results")
    fun supports_sending_responses_with_results() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                sendResponse(ResponseObject(id = id, result = JsonPrimitive("lorem")))
                sendResult(result = JsonPrimitive("ipsum"))
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "result" to JsonPrimitive("lorem")
            ),
            client.receive()
        )

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "result" to JsonPrimitive("ipsum")
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending responses with null results")
    fun supports_sending_responses_with_null_results() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                sendResponse(ResponseObject(id = id, result = JsonNull))
                sendResult(result = null)
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "result" to JsonNull
            ),
            client.receive()
        )

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "result" to JsonNull
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending responses with errors without data")
    fun supports_sending_responses_with_errors_without_data() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                throw InvalidParams("Lorem ipsum")
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(-32602),
                    "message" to JsonPrimitive("Lorem ipsum")
                )
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending responses with errors with data")
    fun supports_sending_responses_with_errors_with_data() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                throw InvalidParams("Lorem ipsum", data = JsonPrimitive(123))
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(-32602),
                    "message" to JsonPrimitive("Lorem ipsum"),
                    "data" to JsonPrimitive(123)
                )
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending requests without parameters")
    fun supports_sending_requests_without_parameters() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                server.sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = server.nextRequestId
                    )
                )
                server.sendRequest("notify/test")
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "method" to JsonPrimitive("lorem/ipsum")
            ),
            client.receive()
        )

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(2),
                "method" to JsonPrimitive("notify/test")
            ),
            client.receive()
        )
    }

    @Test
    @DisplayName("supports sending requests with parameters")
    fun supports_sending_requests_with_parameters() = testJsonRpc {
        client.send(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "id" to JsonPrimitive(1)
            )
        )

        server.jsonRpc {
            request {
                server.sendRequest(
                    RequestObject(
                        method = "lorem/ipsum",
                        id = server.nextRequestId,
                        params = jsonArrayOf(JsonPrimitive(5))
                    )
                )
                server.sendRequest(
                    method = "notify/test",
                    params = jsonArrayOf(JsonPrimitive(123))
                )
            }
        }

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(1),
                "method" to JsonPrimitive("lorem/ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(5))
            ),
            client.receive()
        )

        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonPrimitive(2),
                "method" to JsonPrimitive("notify/test"),
                "params" to jsonArrayOf(JsonPrimitive(123))
            ),
            client.receive()
        )
    }
}
