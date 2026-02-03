package net.sfelabs.knox_enterprise.domain.model

/**
 * CC Mode (Common Criteria) state values.
 * These mirror the Knox SDK AdvancedRestrictionPolicy constants but are exposed
 * through the domain layer so tests don't need direct SDK access.
 *
 * Values from Knox SDK AdvancedRestrictionPolicy:
 * - CC_MODE_ENABLED = 4
 * - CC_MODE_READY = 2
 */
object CCModeState {
    /** CC Mode is ready but not enabled */
    const val READY = 2

    /** CC Mode is enabled */
    const val ENABLED = 4

    /** CC Mode state is unknown or error */
    const val UNKNOWN = -1
}
