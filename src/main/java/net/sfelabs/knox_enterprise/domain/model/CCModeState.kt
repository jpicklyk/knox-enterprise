package net.sfelabs.knox_enterprise.domain.model

/**
 * CC Mode (Common Criteria) state values.
 * These mirror the Knox SDK AdvancedRestrictionPolicy constants but are exposed
 * through the domain layer so tests don't need direct SDK access.
 */
object CCModeState {
    /** CC Mode is ready but not enabled */
    const val READY = 0

    /** CC Mode is enabled */
    const val ENABLED = 1

    /** CC Mode state is unknown or error */
    const val UNKNOWN = -1
}
