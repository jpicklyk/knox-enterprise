package net.sfelabs.knox_enterprise.domain.model

/**
 * Bluetooth profile types for profile-level control.
 *
 * STIG V-268957 specifies that only these profiles should be allowed:
 * - HSP (Headset Profile) - HEADSET
 * - HFP (Hands-Free Profile) - HEADSET
 * - SPP (Serial Port Profile) - SPP
 * - A2DP (Advanced Audio Distribution Profile) - A2DP
 * - AVRCP (Audio/Video Remote Control Profile) - AVRCP
 * - PBAP (Phone Book Access Profile) - PBAP
 */
enum class BluetoothProfile(val value: Int) {
    /** Advanced Audio Distribution Profile - Audio streaming */
    A2DP(0),

    /** Headset Profile / Hands-Free Profile - Voice calls via Bluetooth headset */
    HEADSET(1),

    /** Human Interface Device Profile - Keyboards, mice, etc. */
    HID(2),

    /** Personal Area Network Profile - Network sharing */
    PAN(3),

    /** Message Access Profile - SMS/MMS access */
    MAP(4),

    /** Phone Book Access Profile - Contact sharing */
    PBAP(5),

    /** Object Push Profile - File transfer */
    OPP(6),

    /** SIM Access Profile - Remote SIM access */
    SAP(7),

    /** Serial Port Profile - Serial communication */
    SPP(8),

    /** Audio/Video Remote Control Profile - Media controls */
    AVRCP(9);

    companion object {
        /**
         * Returns the set of Bluetooth profiles allowed per STIG V-268957.
         */
        val stigAllowedProfiles: Set<BluetoothProfile> = setOf(
            A2DP,     // Audio streaming
            HEADSET,  // HSP/HFP - Voice calls
            SPP,      // Serial Port
            AVRCP,    // Audio/Video Remote Control
            PBAP      // Phone Book Access
        )

        /**
         * Returns the set of Bluetooth profiles that should be disabled per STIG V-268957.
         */
        val stigDisallowedProfiles: Set<BluetoothProfile> = entries.toSet() - stigAllowedProfiles

        fun fromValue(value: Int): BluetoothProfile? = entries.find { it.value == value }
    }
}
