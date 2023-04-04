package com.skash.timetrack.core.model.wrapper

import com.skash.timetrack.core.model.Organization

data class OrganizationSelectionWrapper(
    val organization: Organization,
    val isSelected: Boolean = false
)