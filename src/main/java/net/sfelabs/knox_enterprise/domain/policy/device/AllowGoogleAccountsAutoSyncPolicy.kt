package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowGoogleAccountsAutoSyncUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsGoogleAccountsAutoSyncAllowedUseCase

@PolicyDefinition(
    title = "Allow Google Accounts Auto Sync",
    description = "Allow or disallow automatic syncing of Google accounts on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.AFFECTS_BATTERY
    ]
)
class AllowGoogleAccountsAutoSyncPolicy : BooleanStatePolicy() {
    private val getUseCase = IsGoogleAccountsAutoSyncAllowedUseCase()
    private val setUseCase = AllowGoogleAccountsAutoSyncUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
