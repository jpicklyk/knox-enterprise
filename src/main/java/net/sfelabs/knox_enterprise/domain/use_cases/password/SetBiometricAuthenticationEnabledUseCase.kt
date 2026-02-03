package net.sfelabs.knox_enterprise.domain.use_cases.password

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError
import net.sfelabs.knox_enterprise.domain.model.BiometricAuthType

class SetBiometricAuthenticationEnabledUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<SetBiometricAuthenticationEnabledUseCase.Params, Boolean>() {

    data class Params(
        val biometricType: BiometricAuthType = BiometricAuthType.ALL,
        val enabled: Boolean
    )

    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return when (passwordPolicy.setBiometricAuthenticationEnabled(params.biometricType.value, params.enabled)) {
            true -> ApiResult.Success(data = params.enabled)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set biometric authentication (${params.biometricType.name}) enabled to ${params.enabled}")
            )
        }
    }
}
