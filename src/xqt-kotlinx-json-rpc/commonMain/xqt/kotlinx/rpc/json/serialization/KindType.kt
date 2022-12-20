// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*

/**
 * The type of the JSON element.
 *
 * @param kindName the display name of the kind type
 */
enum class KindType(val kindName: kotlin.String) {
    /**
     * A JSON array.
     */
    Array("array"),

    /**
     * A JSON boolean primitive.
     */
    Boolean("boolean"),

    /**
     * A JSON decimal primitive number.
     */
    Decimal("decimal"),

    /**
     * A JSON integer primitive number.
     */
    Integer("integer"),

    /**
     * A JSON null.
     */
    Null("null"),

    /**
     * A JSON object.
     */
    Object("object"),

    /**
     * A JSON string.
     */
    String("string"),
}

/**
 * Returns the type of the JSON primitive.
 */
val JsonPrimitive.kindType: KindType
    get() = when {
        this === JsonNull -> KindType.Null
        isString -> KindType.String
        content == "true" || content == "false" -> KindType.Boolean
        '.' in content -> KindType.Decimal
        else -> KindType.Integer
    }

/**
 * Returns the type of the JSON element.
 */
val JsonElement.kindType: KindType
    get() = when (this) {
        is JsonObject -> KindType.Object
        is JsonArray -> KindType.Array
        is JsonPrimitive -> kindType
    }
