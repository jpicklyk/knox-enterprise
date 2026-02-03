package net.sfelabs.knox_enterprise.domain.use_cases.security

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to check if external storage (SD card) encryption is enabled.
 *
 * STIG V-268935 requires enabling encryption for data at rest on removable storage media.
 */
class IsExternalStorageEncryptedUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<Unit, Boolean>() {

    private val deviceSecurityPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).deviceSecurityPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = deviceSecurityPolicy.isExternalStorageEncrypted)
    }
}
