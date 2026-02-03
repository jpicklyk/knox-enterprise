package net.sfelabs.knox_enterprise.domain.use_cases.application

import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to get all blocked package names as a flat set.
 *
 * This is a convenience wrapper around [GetBlockedPackagesUseCase] that flattens
 * the [AppControlInfo] structure into a simple set of package names.
 *
 * Useful when you need to check if specific packages are blocked without
 * caring which admin blocked them.
 *
 * @see GetBlockedPackagesUseCase for the underlying implementation
 */
class GetAllBlockedPackageNamesUseCase : SuspendingUseCase<Unit, Set<String>>() {

    private val getBlockedPackagesUseCase = GetBlockedPackagesUseCase()

    override suspend fun execute(params: Unit): ApiResult<Set<String>> {
        return when (val result = getBlockedPackagesUseCase()) {
            is ApiResult.Success -> {
                val allNames = mutableSetOf<String>()
                for (info in result.data) {
                    allNames.addAll(info.entries)
                }
                ApiResult.Success(allNames)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
