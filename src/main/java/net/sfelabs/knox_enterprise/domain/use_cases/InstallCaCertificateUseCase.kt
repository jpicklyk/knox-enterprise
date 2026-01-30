package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError
import net.sfelabs.knox_enterprise.domain.model.CertificateType
import net.sfelabs.knox_enterprise.domain.model.TargetKeystore

class InstallCaCertificateUseCase: WithAndroidApplicationContext, SuspendingUseCase<InstallCaCertificateUseCase.Params, Unit>() {
    class Params(
        val keystore: TargetKeystore,
        val certificateType: CertificateType,
        val data: ByteArray,
        val alias: String,
        val password: String
    )

    private val enterpriseDeviceManager = EnterpriseDeviceManager.getInstance(applicationContext)

    suspend operator fun invoke(
        keystore: TargetKeystore,
        certificateType: CertificateType,
        data: ByteArray,
        alias: String,
        password: String
    ): ApiResult<Unit> {
        return invoke(Params(keystore, certificateType, data, alias, password))
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        val certificateProvisioning = enterpriseDeviceManager.certificateProvisioning
        val result = certificateProvisioning.installCertificateToKeystore(
            params.certificateType.type,
            params.data,
            params.alias,
            params.password,
            params.keystore.value
        )
        return if (result) ApiResult.Success(Unit)
        else ApiResult.Error(
            DefaultApiError.UnexpectedError(
                "Certificate failed to install to keystore"
            )
        )
    }
}
