plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

group = "com.github.SoluteToNight"
version = providers.gradleProperty("VERSION")
    .orElse(providers.environmentVariable("VERSION"))
    .getOrElse("0.0.0-SNAPSHOT")

android {
    namespace = "com.lcars.ui"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.animation)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.SoluteToNight"
                artifactId = "LCARS-Compose-UI"
                version = project.version.toString()

                pom {
                    name.set("LCARS Compose UI")
                    description.set("Android Jetpack Compose LCARS component library.")
                    url.set("https://github.com/SoluteToNight/LCARS-Compose-UI")
                    scm {
                        url.set("https://github.com/SoluteToNight/LCARS-Compose-UI")
                        connection.set("scm:git:https://github.com/SoluteToNight/LCARS-Compose-UI.git")
                        developerConnection.set("scm:git:ssh://git@github.com/SoluteToNight/LCARS-Compose-UI.git")
                    }
                }
            }
        }
    }
}
