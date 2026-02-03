package net.sfelabs.knox_enterprise.domain.use_cases.device

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to check if AI/Intelligence features are allowed to process data in the cloud.
 *
 * STIG V-268932 (CAT I) requires excluding AI apps that process data in the cloud.
 *
 * Note: Requires Knox SDK API level 37+ (Knox 3.10). Uses reflection for compatibility
 * with older SDK versions.
 */
class IsIntelligenceOnlineProcessingAllowedUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val advancedRestrictionPolicy by lazy {
        EnterpriseKnoxManager.getInstance(applicationContext).advancedRestrictionPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return try {
            val method = advancedRestrictionPolicy.javaClass.getMethod(
                "isIntelligenceOnlineProcessingAllowed"
            )
            val result = method.invoke(advancedRestrictionPolicy) as Boolean
            ApiResult.Success(data = result)
        } catch (e: NoSuchMethodException) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("isIntelligenceOnlineProcessingAllowed not available (requires Knox SDK API 37+)")
            )
        } catch (e: Exception) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to check intelligence online processing: ${e.message}")
            )
        }
    }
}
