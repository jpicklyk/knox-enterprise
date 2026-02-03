package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError
import net.sfelabs.knox_enterprise.domain.model.BluetoothProfile

class SetBluetoothProfileStateUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<SetBluetoothProfileStateUseCase.Params, Boolean>() {

    data class Params(val profile: BluetoothProfile, val enabled: Boolean)

    private val bluetoothPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).bluetoothPolicy
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return when (bluetoothPolicy.setProfileState(params.enabled, params.profile.value)) {
            true -> ApiResult.Success(data = params.enabled)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError(
                    "Failed to set Bluetooth profile ${params.profile.name} to ${if (params.enabled) "enabled" else "disabled"}"
                )
            )
        }
    }
}
