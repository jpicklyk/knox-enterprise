package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to check if a method exists on the Knox RestrictionPolicy.
 * This allows tests to verify API existence without direct SDK access.
 *
 * Example usage:
 * ```
 * val result = CheckRestrictionPolicyMethodExistsUseCase().invoke("isRandomisedMacAddressEnabled")
 * if (result is ApiResult.Success && result.data) {
 *     // Method exists
 * }
 * ```
 */
class CheckRestrictionPolicyMethodExistsUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Boolean>() {

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return try {
            val restrictionPolicy = EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy
            val methods = restrictionPolicy.javaClass.methods
            val exists = methods.any { it.name == params }
            ApiResult.Success(exists)
        } catch (e: Exception) {
            ApiResult.Success(false)
        }
    }
}
