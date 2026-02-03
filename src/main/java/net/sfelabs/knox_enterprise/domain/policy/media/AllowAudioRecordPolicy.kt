package net.sfelabs.knox_enterprise.domain.policy.media

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.media.AllowAudioRecordUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.media.IsAudioRecordAllowedUseCase

@PolicyDefinition(
    title = "Block Audio Recording",
    description = "When enabled, blocks audio recording on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowAudioRecordPolicy : BooleanStatePolicy() {
    private val getUseCase = IsAudioRecordAllowedUseCase()
    private val setUseCase = AllowAudioRecordUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
