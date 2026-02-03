package net.sfelabs.knox_enterprise.domain.model

/**
 * Biometric authentication types supported by Knox SDK.
 */
enum class BiometricAuthType(val value: Int) {
    /** Fingerprint authentication */
    FINGERPRINT(1),

    /** Face recognition authentication */
    FACE(2),

    /** Iris recognition authentication */
    IRIS(4),

    /** All biometric types */
    ALL(FINGERPRINT.value or FACE.value or IRIS.value);

    companion object {
        fun fromValue(value: Int): BiometricAuthType? = entries.find { it.value == value }
    }
}
