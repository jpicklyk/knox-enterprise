package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsExternalStorageEncryptedUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetExternalStorageEncryptionUseCase

/**
 * Policy to control external storage (SD card) encryption.
 *
 * STIG V-268935 (CAT III) requires enabling encryption for data at rest
 * on removable storage media.
 *
 * When enabled, data written to external storage will be encrypted,
 * protecting sensitive data if the storage media is removed from the device.
 */
@PolicyDefinition(
    title = "External Storage Encryption",
    description = "Enable or disable encryption for external storage (SD card). When enabled, data on removable storage is encrypted.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class ExternalStorageEncryptionPolicy : BooleanStatePolicy() {
    private val getUseCase = IsExternalStorageEncryptedUseCase()
    private val setUseCase = SetExternalStorageEncryptionUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
