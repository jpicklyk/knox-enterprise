package net.sfelabs.knox_enterprise.domain.use_cases.password

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class GetMaxCharacterSequenceLengthUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Int>() {
    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(data = passwordPolicy.maximumCharacterSequenceLength)
    }
}
