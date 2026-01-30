package net.sfelabs.knox_enterprise.domain.model

sealed class CertificateType(val type: String) {
    data object Cert : CertificateType("CERT")
    data object Pkcs12 : CertificateType("PKCS12")
}
