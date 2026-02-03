package net.sfelabs.knox_enterprise.domain.use_cases.security

import android.content.ComponentName
import android.os.PersistableBundle
import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to configure a specific trust agent via Knox SDK.
 *
 * This allows fine-grained control over individual trust agents,
 * enabling administrators to configure specific trust agent behavior
 * rather than disabling all trust agents globally.
 *
 * Note: This API is available in Knox SDK API level 27+ (Knox 3.2.1) and
 * deprecated in API level 35. Uses reflection for SDK compatibility.
 *
 * Note: For STIG compliance (V-268927), use [SetTrustAgentsDisabledUseCase]
 * to disable all trust agents.
 *
 * @see SetTrustAgentsDisabledUseCase for disabling all trust agents
 */
class SetTrustAgentConfigurationUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<SetTrustAgentConfigurationUseCase.Params, Unit>() {

    data class Params(
        val adminComponent: ComponentName,
        val trustAgentComponent: ComponentName,
        val configuration: PersistableBundle
    )

    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return try {
            val method = passwordPolicy.javaClass.getMethod(
                "setTrustAgentConfiguration",
                ComponentName::class.java,
                ComponentName::class.java,
                PersistableBundle::class.java
            )
            method.invoke(
                passwordPolicy,
                params.adminComponent,
                params.trustAgentComponent,
                params.configuration
            )
            ApiResult.Success(Unit)
        } catch (e: NoSuchMethodException) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("setTrustAgentConfiguration not available (requires Knox SDK API 27+)")
            )
        } catch (e: Exception) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set trust agent configuration: ${e.message}")
            )
        }
    }
}
