pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AzHua"

// ========================
// App Module
// ========================
include(":app")

// ========================
// Core Modules
// ========================
include(":core:core-common")
include(":core:core-ui")
include(":core:core-model")
include(":core:core-database")
include(":core:core-network")

// ========================
// Feature Modules
// ========================
include(":feature:feature-library")
include(":feature:feature-discover")
include(":feature:feature-recents")
include(":feature:feature-extensions")
include(":feature:feature-detail")
include(":feature:feature-player")
include(":feature:feature-settings")

// ========================
// Data Modules
// ========================
include(":data:data-repository")

// ========================
// Extension API
// ========================
include(":extension-api")

// ========================
// Extensions
// ========================
include(":ext-anichin")
