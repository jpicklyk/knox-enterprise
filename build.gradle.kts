import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.ksp)
}

extensions.configure<LibraryExtension> {
    namespace = "net.sfelabs.knox_enterprise"

    lint {
        // Disable resource prefix check for this external module
        disable += "ResourceName"
    }
}

ksp {
    arg("android.namespace", extensions.getByType<LibraryExtension>().namespace ?: "")
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.coroutines.core)
    api(projects.knoxCore.usecaseExecutor)
    api(projects.knoxCore.android)
    api(projects.knoxCore.feature)
    implementation(projects.knoxCore.featureProcessor)
    implementation(libs.spongycastle.prov)
    implementation(libs.commons.lang)
    // Knox SDK is compileOnly - consumers must provide their own SDK JAR at runtime
    compileOnly(files("libs/knoxsdk_ver38.jar"))

    ksp(projects.knoxCore.featureProcessor)
}
