# knox-enterprise

Android library module for Samsung Knox Enterprise SDK integration. This module provides use cases for enterprise device management features available on Samsung devices with Knox support.

## Overview

knox-enterprise wraps the Samsung Knox Enterprise SDK, providing a clean use case-based API for standard enterprise device management features. The module is DI-agnostic and can be used with any dependency injection framework.

## Features

### Device Security

- **ADB Control** - Enable/disable Android Debug Bridge
- **CC Mode** - Common Criteria mode configuration
- **Firmware Recovery** - Allow/block firmware recovery mode
- **OTA Updates** - Control over-the-air update policy

### Display & Settings

- **Brightness Control** - Get/set screen brightness value and mode
- **Screen Settings** - Various display configuration options

### Connectivity & Data

- **Mobile Data** - Enable/disable mobile data
- **Data Roaming** - Control mobile data roaming
- **USB Host Storage** - Allow/block USB storage access
- **USB Exception List** - Configure USB device whitelist

### Certificates & Security

- **CA Certificate Installation** - Install trusted CA certificates
- **Certificate Management** - Manage device certificates by type and keystore

### Attestation

- **Device Attestation** - Generate attestation blobs
- **Key Generation** - Generate attestation keys
- **Attestation Support Check** - Verify device attestation capabilities

### Audit & Logging

- **Audit Log** - Enable/disable and query audit logging

## Architecture

knox-enterprise follows a DI-agnostic architecture:

```
┌─────────────────────────────────────────────────────────┐
│  Your App                                               │
├─────────────────────────────────────────────────────────┤
│  knox-hilt (optional - for Hilt users)                  │
├─────────────────────────────────────────────────────────┤
│  knox-enterprise (this module)                          │
│  ├── domain/use_cases/  - Business logic                │
│  └── domain/model/      - Domain models                 │
├─────────────────────────────────────────────────────────┤
│  knox-core              - Shared infrastructure         │
└─────────────────────────────────────────────────────────┘
```

## Usage

### Direct Use Case Instantiation

Use cases can be instantiated directly without dependency injection:

```kotlin
// Control ADB state
val result = SetAdbStateUseCase().invoke(enabled = true)
when (result) {
    is ApiResult.Success -> println("ADB state changed")
    is ApiResult.Error -> println("Error: ${result.error.message}")
}

// Get screen brightness
val brightness = GetBrightnessValueUseCase().invoke(Unit)

// Set screen brightness
val result = SetBrightnessUseCase().invoke(brightness = 128)

// Check CC Mode status
val ccMode = GetCCModeUseCase().invoke(Unit)

// Control mobile data
val result = SetMobileDataStateUseCase().invoke(enabled = true)

// Install CA certificate
val result = InstallCaCertificateUseCase().invoke(
    certificateData = certBytes,
    alias = "my-ca-cert"
)
```

### Device Attestation

```kotlin
// Check if attestation is supported
val isSupported = IsAttestationSupportedUseCase().invoke(Unit)

// Generate attestation key
val keyResult = KeyGeneratorUseCase().invoke(keyAlias = "attestation-key")

// Get attestation blob
val blobResult = GetAttestationBlobUseCase().invoke(
    nonce = nonceBytes,
    keyAlias = "attestation-key"
)
```

### With Hilt (via knox-hilt)

If using Dagger Hilt, add the knox-hilt module for automatic DI integration:

```kotlin
// In your app's build.gradle.kts
dependencies {
    implementation(project(":knox-enterprise"))
    implementation(project(":knox-hilt"))  // For Hilt integration
}
```

## Domain Models

knox-enterprise exposes domain models to avoid SDK dependencies in consuming code:

```kotlin
// CC Mode states
enum class CCModeState {
    DISABLED, ENABLED, ENFORCED
}

// Certificate types
enum class CertificateType {
    CA, USER, VPN, WIFI
}

// Target keystore
enum class TargetKeystore {
    SYSTEM, USER, VPN, WIFI
}

// USB interface types
enum class UsbInterface {
    MTP, PTP, RNDIS, MIDI, MASS_STORAGE
}
```

## Use Case Categories

### ADB (`domain/use_cases/adb/`)
- `SetAdbStateUseCase` - Enable/disable ADB

### Attestation (`domain/use_cases/attestation/`)
- `GetAttestationBlobUseCase` - Generate attestation blob
- `IsAttestationSupportedUseCase` - Check attestation support
- `KeyGeneratorUseCase` - Generate attestation keys

### Audit (`domain/use_cases/audit/`)
- `EnableAuditLogUseCase` - Enable audit logging
- `DisableAuditLogUseCase` - Disable audit logging
- `IsAuditLogEnabledUseCase` - Check audit log status

### Settings (`domain/use_cases/settings/`)
- `GetBrightnessValueUseCase` - Get current brightness
- `GetBrightnessModeUseCase` - Get brightness mode (auto/manual)
- `SetBrightnessUseCase` - Set screen brightness

### Other Use Cases
- `AllowFirmwareRecoveryUseCase` / `IsFirmwareRecoveryAllowedUseCase`
- `AllowOtaUpgradeUseCase` / `IsOtaUpgradeAllowedUseCase`
- `AllowUsbHostStorageUseCase` / `IsUsbHostStorageAllowedUseCase`
- `GetCCModeUseCase` / `SetCCModeUseCase`
- `InstallCaCertificateUseCase`
- `SetMobileDataStateUseCase`
- `SetMobileDataRoamingStateUseCase`
- `SetUsbExceptionListUseCase`

## Requirements

- Samsung device with Knox Enterprise SDK support
- Knox Enterprise license activated
- Device Owner or appropriate Knox permissions

## SDK Isolation

The Knox Enterprise SDK JAR is included in the `libs/` directory:
- SDK classes are available at compile time
- All SDK access is encapsulated within use cases
- Consuming apps interact only with domain models and use cases

## Error Handling

All use cases return `ApiResult` which is a sealed class:

```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
}
```

Handle results appropriately:

```kotlin
when (val result = someUseCase.invoke(params)) {
    is ApiResult.Success -> {
        // Handle success with result.data
    }
    is ApiResult.Error -> {
        // Handle error with result.error.message
    }
}
```

## Testing

Use cases are designed for easy testing:

```kotlin
class MyViewModelTest {
    @Test
    fun `test brightness change`() = runTest {
        // Use cases can be mocked or stubbed
        val mockUseCase = mockk<SetBrightnessUseCase>()
        coEvery { mockUseCase.invoke(any()) } returns ApiResult.Success(Unit)

        // Test your ViewModel or other code
    }
}
```
