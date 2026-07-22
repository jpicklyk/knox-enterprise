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
        // A genuinely absent method is reported as Success(false) via the normal path below
        // (it simply won't appear in the enumerated method list). Any unexpected failure
        // (e.g. Knox unavailable) is left to propagate to the base class's mapError so it
        // isn't silently masked as "method absent".
        val restrictionPolicy = EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy
        val methods = restrictionPolicy.javaClass.methods
        val exists = methods.any { it.name == params }
        return ApiResult.Success(exists)
    }
}
