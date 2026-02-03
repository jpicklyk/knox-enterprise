package net.sfelabs.knox_enterprise.domain.use_cases.security

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to enable or disable external storage (SD card) encryption.
 *
 * STIG V-268935 requires enabling encryption for data at rest on removable storage media.
 *
 * When enabled, data written to external storage will be encrypted.
 */
class SetExternalStorageEncryptionUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<Boolean, Unit>() {

    private val deviceSecurityPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).deviceSecurityPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        deviceSecurityPolicy.setExternalStorageEncryption(params)
        return ApiResult.Success(Unit)
    }
}
