package net.sfelabs.knox_enterprise.domain.use_cases.password

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.model.BiometricAuthType

class IsBiometricAuthenticationEnabledUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<BiometricAuthType, Boolean>() {

    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: BiometricAuthType): ApiResult<Boolean> {
        return ApiResult.Success(data = passwordPolicy.isBiometricAuthenticationEnabled(params.value))
    }
}
