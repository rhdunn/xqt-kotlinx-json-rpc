object BuildConfiguration {
    /**
     * The version of the JVM to target by the Kotlin compiler.
     */
    val jvmTarget = System.getProperty("jvm.target") ?: "11"

    /**
     * Should the build process download node-js if it is not present? (default: true)
     */
    val downloadNodeJs = System.getProperty("nodejs.download") != "false"

    /**
     * The Operating System the build is running on.
     */
    val hostOs = HostOs(System.getProperty("os.name") ?: "Unknown")

    /**
     * The headless web browser to run the JS tests on.
     */
    val jsBrowser = JsBrowser(System.getProperty("js.browser"), hostOs)
}
