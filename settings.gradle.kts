pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven ( url  = "https://artifactory.appodeal.com/appodeal" )
    }
}
rootProject.name = "sovchilar"
include (":app")
include(":domain")
include(":comm")
include(":data")
include(":featureRemoteApi")
