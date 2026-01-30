package net.sfelabs.knox_enterprise.domain.use_cases.attestation

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.os.Build
import android.security.AttestedKeyPair
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError
import java.math.BigInteger
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec
import java.util.Date
import javax.security.auth.x500.X500Principal

class KeyGeneratorUseCase: WithAndroidApplicationContext, SuspendingUseCase<Unit, Unit>() {
    private val devicePolicyManager = this@KeyGeneratorUseCase.applicationContext.getSystemService(DevicePolicyManager::class.java)

    @RequiresApi(Build.VERSION_CODES.S)
    private fun generateKey(): AttestedKeyPair? {
        val builder: KeyGenParameterSpec.Builder =
            KeyGenParameterSpec.Builder("alias", KeyProperties.PURPOSE_SIGN)
        val challenge = ByteArray(16)
        SecureRandom().nextBytes(challenge)
        builder.setAttestationChallenge(challenge)
        builder.setCertificateSerialNumber(BigInteger.valueOf(42))
        builder.setCertificateSubject(X500Principal("C=US, ST=Texas, O=ABC, OU=XYZ, CN=ABCDEFGH12345678"))


        val startTime: Long = System.currentTimeMillis()
        builder.setCertificateNotBefore(Date(startTime))
        builder.setCertificateNotAfter(Date(startTime + 365L * 24L * 60L * 60L * 1000L - 1L))
        builder.setUserAuthenticationRequired(false)
        builder.setIsStrongBoxBacked(true)
        builder.setUnlockedDeviceRequired(false)
        builder.setDevicePropertiesAttestationIncluded(true)
        builder.setDigests(KeyProperties.DIGEST_SHA256)
        //builder.setSignaturePaddings(arrayOf<String>(0))
        //builder . setEncryptionPaddings arrayOfNulls<String>(0)
        builder.setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
        val name = ComponentName(
            "net.sfelabs.knoxmoduleshowcase",
            "net.sfelabs.knoxmoduleshowcase.app.receivers.AdminReceiver"
        )
        return devicePolicyManager.generateKeyPair(
            name,
            "EC",
            builder.build(),
            DevicePolicyManager.ID_TYPE_BASE_INFO or DevicePolicyManager.ID_TYPE_SERIAL
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override suspend fun execute(params: Unit): ApiResult<Unit> {
        val result = generateKey()
        return if (result == null)
            ApiResult.Error(DefaultApiError.UnexpectedError("Error, generate keypair returned null"))
        else {
            println("Key generated: ${result.keyPair}")
            ApiResult.Success(Unit)
        }
    }

}