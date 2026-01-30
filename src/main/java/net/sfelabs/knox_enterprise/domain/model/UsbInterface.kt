package net.sfelabs.knox_enterprise.domain.model

/**
 * USB interface class constants for USB exception list configuration.
 * These mirror the Knox SDK RestrictionPolicy.USBInterface constants.
 *
 * Use these with [net.sfelabs.knox_enterprise.domain.use_cases.SetUsbExceptionListUseCase].
 *
 * Example:
 * ```
 * // Allow CDC (ethernet) and MAS (mass storage)
 * val usbClasses = UsbInterface.CDC or UsbInterface.MAS
 * SetUsbExceptionListUseCase().invoke(usbClasses)
 * ```
 */
object UsbInterface {
    /** Disable USB interface filtering (allow all) */
    const val OFF = 0

    /** Communications Device Class - includes ethernet dongles */
    const val CDC = 1

    /** Mass Storage - includes USB drives */
    const val MAS = 2

    /** Human Interface Device - includes keyboards, mice */
    const val HID = 4

    /** Audio class */
    const val AUD = 8

    /** Video class */
    const val VID = 16

    /** Printer class */
    const val PRT = 32

    /** Wireless controller */
    const val WIR = 64

    /** Miscellaneous */
    const val MIS = 128

    /** Application specific */
    const val APP = 256

    /** Vendor specific */
    const val VEN = 512
}
