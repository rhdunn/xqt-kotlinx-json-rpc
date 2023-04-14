@Suppress("MemberVisibilityCanBePrivate")
object ProjectMetadata {
    object Build {
        /**
         * The semantic version of the current version.
         */
        const val VersionTag = "0.1"

        /**
         * The build type of this project.
         */
        val Type = BuildType.Snapshot

        /**
         * The artifact version ID.
         */
        val Version = "$VersionTag${Type.suffix}"
    }

    /**
     * The ID of the GitHub account to which this project belongs.
     */
    const val githubId = "rhdunn"

    /**
     * The copyright year range for the project.
     */
    const val copyrightYear = "2022-2023"

    /**
     * The person or organization owning the copyright for the project.
     */
    const val copyrightOwner = "Reece H. Dunn"

    /**
     * The Maven group ID.
     */
    const val groupId = "io.github.$githubId"
}
