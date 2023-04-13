object BuildConfiguration {
    /**
     * The version of the JVM to target by the Kotlin compiler.
     */
    val jvmTarget = System.getProperty("jvm.target") ?: "11"
}
