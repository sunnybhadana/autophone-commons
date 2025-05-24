plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.kotlinAndroid)
    `maven-publish`
}

group = "com.revaltronics.commons"
version = "6.1.1"

android {
    namespace = "com.revaltronics.strings"
    compileSdk = libs.versions.app.build.compileSDKVersion.get().toInt()
}

publishing.publications {
    create<MavenPublication>("release") {
        afterEvaluate {
            from(components["release"])
        }
    }
}
