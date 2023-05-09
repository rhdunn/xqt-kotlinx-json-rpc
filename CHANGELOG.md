# Change Log

## 1.0.1 - 2023-05-09
1. Improve the build infrastructure to support automated deployment.
2. Support 32-bit native targets.
3. Change the names of the native artifacts to support multiple native target
   maven artifacts.
4. Change the names of the JVM artifacts to support multiple JVM target maven
   artifacts.

## 1.0.0 - 2023-04-15

### JSON-RPC
1. `RequestObject`, `Notification`, and `ResponseObject` messages.
2. Message processing DSL.
3. Message sending APIs.

### I/O
1. `stdin` and `stdout` binary I/O channels.

### Serialization
1. `JsonSerialization` support for (de)serializing JSON types.
2. `StringSerialization` support for (de)serializing JSON object key names.
3. `JsonObjectType` support for object type checking/verification.
4. JSON serialization helpers.
