package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowGoogleCrashReportUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsGoogleCrashReportAllowedUseCase

@PolicyDefinition(
    title = "Block Google Crash Reports",
    description = "When enabled, prevents sending crash reports to Google from the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowGoogleCrashReportPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsGoogleCrashReportAllowedUseCase()
    private val setUseCase = AllowGoogleCrashReportUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
