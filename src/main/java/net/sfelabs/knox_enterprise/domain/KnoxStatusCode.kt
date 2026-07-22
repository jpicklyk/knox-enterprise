package net.sfelabs.knox_enterprise.domain

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Maps a Knox [CustomDeviceManager] integer status code to a typed [ApiResult].
 *
 * Knox `settingsManager` / `customDeviceManager` APIs report success and failure through
 * `int` return codes rather than exceptions. Converting those raw codes into the shared
 * [ApiResult] hierarchy in one place — instead of hand-rolling a `when` in every use case —
 * means callers receive **typed** errors ([DefaultApiError.PolicyRestricted],
 * [DefaultApiError.InvalidInput], ...) they can branch on, rather than an opaque
 * [DefaultApiError.UnexpectedError] carrying a stringly-typed message.
 *
 * Only status codes verified to exist in `knoxsdk_ver38.jar` are referenced:
 * [CustomDeviceManager.SUCCESS], [CustomDeviceManager.ERROR_NOT_SUPPORTED],
 * [CustomDeviceManager.ERROR_POLICY_RESTRICTED] and [CustomDeviceManager.ERROR_INVALID_VALUE].
 * Any other (or unrecognized) code falls through to [DefaultApiError.UnexpectedError] with the
 * raw code preserved for diagnostics.
 *
 * @param operation short human-readable name of the API being invoked, used to build error messages.
 * @param onSuccess produces the success payload; only invoked when the code is [CustomDeviceManager.SUCCESS].
 */
internal inline fun <T : Any> Int.toKnoxApiResult(
    operation: String,
    onSuccess: () -> T
): ApiResult<T> = when (this) {
    CustomDeviceManager.SUCCESS ->
        ApiResult.Success(onSuccess())

    CustomDeviceManager.ERROR_NOT_SUPPORTED ->
        ApiResult.NotSupported

    CustomDeviceManager.ERROR_POLICY_RESTRICTED ->
        ApiResult.Error(DefaultApiError.PolicyRestricted("$operation rejected by device policy"))

    CustomDeviceManager.ERROR_INVALID_VALUE ->
        ApiResult.Error(DefaultApiError.InvalidInput("$operation: invalid value"))

    else ->
        ApiResult.Error(DefaultApiError.UnexpectedError("$operation failed with Knox status code $this"))
}

/**
 * [Unit]-returning convenience overload for Knox APIs whose success carries no payload.
 */
internal fun Int.toKnoxApiResult(operation: String): ApiResult<Unit> =
    toKnoxApiResult(operation) { }
