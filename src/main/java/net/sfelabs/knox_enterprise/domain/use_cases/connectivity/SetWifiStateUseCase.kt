package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Turns Wi-Fi on or off, optionally configuring a connection to an access point.
 *
 * If AP credentials are not required, set [Params.ssid], [Params.username],
 * and [Params.password] to `null`.
 *
 * Uses [com.samsung.android.knox.custom.SettingsManager.setWifiState].
 */
class SetWifiStateUseCase : SuspendingUseCase<SetWifiStateUseCase.Params, Unit>() {
    data class Params(
        val state: Boolean,
        val ssid: String? = null,
        val username: String? = null,
        val password: String? = null,
    )

    private val settingsManager by lazy {
        CustomDeviceManager.getInstance().settingsManager
    }

    suspend operator fun invoke(
        state: Boolean,
        ssid: String? = null,
        username: String? = null,
        password: String? = null,
    ): ApiResult<Unit> = invoke(Params(state, ssid, username, password))

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return when (settingsManager.setWifiState(params.state, params.ssid, params.username, params.password)) {
            CustomDeviceManager.SUCCESS -> ApiResult.Success(Unit)
            else -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set WiFi state to ${params.state}")
            )
        }
    }
}
