# Change Log
> Release notes for `xqt-kotlinx-json-rpc`.

This format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.2] - 2023-05-16

### Fixed

- Include all the supported JVM target variants in the `kotlinMultiplatform` module metadata.
- Include all the supported native target variants in the `kotlinMultiplatform` module metadata.

### Changed

- Switched to the [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) format
  for changelogs.

## [1.0.1] - 2023-05-09

### Fixed

- Improve the build infrastructure to support automated deployment.
- Support 32-bit native targets.

### Changed

- The names of the native artifacts now use the target name to support multiple
  native target maven artifact releases.
- The names of the JVM artifacts now use the JVM target version to support
  multiple target JVM maven artifact releases.

## [1.0.0] - 2023-04-15

### JSON-RPC
- `RequestObject`, `Notification`, and `ResponseObject` messages.
- Message processing DSL.
- Message sending APIs.

### I/O
- `stdin` and `stdout` binary I/O channels.

### Serialization
- `JsonSerialization` support for (de)serializing JSON types.
- `StringSerialization` support for (de)serializing JSON object key names.
- `JsonObjectType` support for object type checking/verification.
- JSON serialization helpers.

[Unreleased]: https://github.com/rhdunn/xqt-kotlinx-json-rpc/compare/1.0.2...HEAD
[1.0.2]: https://github.com/rhdunn/xqt-kotlinx-json-rpc/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/rhdunn/xqt-kotlinx-json-rpc/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/rhdunn/xqt-kotlinx-json-rpc/releases/tag/1.0.0
