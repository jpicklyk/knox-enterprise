import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.convention.android.library)
}

extensions.configure<LibraryExtension> {
    namespace = "net.sfelabs.knox_enterprise"

    lint {
        // Disable resource prefix check for this external module
        disable += "ResourceName"
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(projects.knoxCore.usecaseExecutor)
    implementation(projects.knoxCore.android)
    implementation(projects.knoxCore.feature)
    implementation(libs.spongycastle.prov)
    implementation(libs.commons.lang)
    // Knox SDK is compileOnly - consumers must provide their own SDK JAR
    // This avoids conflicts when knox-tactical provides its Tactical SDK
    compileOnly(files("libs/knoxsdk_ver38.jar"))
}
