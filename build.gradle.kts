
plugins {
    id("sfelabs.android.feature")
}

android {
    namespace = "net.sfelabs.knox_enterprise"
}

dependencies {
    implementation(project(":knox-core:usecase-executor"))
    implementation(project(":knox-core:android"))
    implementation(libs.spongycastle.prov)
    implementation(libs.commons.lang)
    // Knox SDK is compileOnly - consumers must provide their own SDK JAR
    // This avoids conflicts when knox-tactical provides its Tactical SDK
    compileOnly(files("libs/knoxsdk_ver38.jar"))
}