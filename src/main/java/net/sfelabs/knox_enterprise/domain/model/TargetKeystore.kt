package net.sfelabs.knox_enterprise.domain.model

sealed class TargetKeystore(val value: Int) {
    data object VpnAndApps : TargetKeystore(4)
    data object Wifi : TargetKeystore(2)
    data object Default : TargetKeystore(1)
}
