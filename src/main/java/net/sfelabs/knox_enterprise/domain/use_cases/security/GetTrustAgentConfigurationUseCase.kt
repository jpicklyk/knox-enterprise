package net.sfelabs.knox_enterprise.domain.use_cases.security

import android.content.ComponentName
import android.os.PersistableBundle
import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to get the configuration for a specific trust agent.
 *
 * Returns the list of PersistableBundle configurations for the given trust agent,
 * aggregating all calls made across device admins.
 *
 * Note: This API is available in Knox SDK API level 27+ (Knox 3.2.1) and
 * deprecated in API level 35. Uses reflection for SDK compatibility.
 */
class GetTrustAgentConfigurationUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<GetTrustAgentConfigurationUseCase.Params, List<PersistableBundle>>() {

    data class Params(
        val adminComponent: ComponentName,
        val trustAgentComponent: ComponentName
    )

    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Params): ApiResult<List<PersistableBundle>> {
        return try {
            val method = passwordPolicy.javaClass.getMethod(
                "getTrustAgentConfiguration",
                ComponentName::class.java,
                ComponentName::class.java
            )
            val configs = method.invoke(
                passwordPolicy,
                params.adminComponent,
                params.trustAgentComponent
            ) as? List<PersistableBundle>
            ApiResult.Success(data = configs ?: emptyList())
        } catch (e: NoSuchMethodException) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("getTrustAgentConfiguration not available (requires Knox SDK API 27+)")
            )
        } catch (e: Exception) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to get trust agent configuration: ${e.message}")
            )
        }
    }
}
