package net.sfelabs.knox_enterprise.domain.model

/**
 * Known AI and cloud-processing applications that should be blocked per STIG V-268932 (CAT I).
 *
 * STIG V-268932 requires excluding AI apps that process data in the cloud.
 * These apps send data to cloud servers for AI/ML processing, which violates
 * data handling requirements for sensitive environments.
 *
 * Usage:
 * ```kotlin
 * // Block all known AI apps
 * addPackagesToPreventStartBlocklistUseCase(StigBlockedApps.AI_CLOUD_PROCESSING_APPS)
 * ```
 */
object StigBlockedApps {

    /**
     * Google AI/ML apps that process data in the cloud.
     */
    val GOOGLE_AI_APPS = listOf(
        "com.google.android.apps.bard",           // Google Gemini (formerly Bard)
        "com.google.android.apps.googleassistant", // Google Assistant
        "com.google.android.googlequicksearchbox", // Google Search with AI features
    )

    /**
     * Samsung AI apps that may process data in the cloud.
     */
    val SAMSUNG_AI_APPS = listOf(
        "com.samsung.android.bixby.agent",        // Bixby Voice
        "com.samsung.android.visionintelligence", // Bixby Vision
        "com.samsung.android.bixby.service",      // Bixby Service
    )

    /**
     * Third-party AI assistant apps.
     */
    val THIRD_PARTY_AI_APPS = listOf(
        "com.openai.chatgpt",                     // ChatGPT
        "com.microsoft.copilot",                  // Microsoft Copilot
        "ai.perplexity.app.android",              // Perplexity AI
        "com.anthropic.claude",                   // Claude (if available)
    )

    /**
     * All AI apps that process data in the cloud.
     * Use this list with [AddPackagesToPreventStartBlocklistUseCase] to comply
     * with STIG V-268932 (CAT I).
     */
    val AI_CLOUD_PROCESSING_APPS: List<String> = GOOGLE_AI_APPS + SAMSUNG_AI_APPS + THIRD_PARTY_AI_APPS

    /**
     * Voice assistant apps (subset of AI apps focused on voice processing).
     * STIG V-268931 mentions restricting voice assistants.
     */
    val VOICE_ASSISTANT_APPS = listOf(
        "com.google.android.apps.googleassistant",
        "com.samsung.android.bixby.agent",
        "com.amazon.dee.app",                     // Amazon Alexa
    )

    /**
     * Payment apps that should be restricted per STIG V-268931.
     */
    val PAYMENT_APPS = listOf(
        "com.google.android.apps.walletnfcrel",   // Google Wallet/Pay
        "com.samsung.android.spay",               // Samsung Pay
        "com.samsung.android.samsungpay.gear",    // Samsung Pay for Gear
    )
}
