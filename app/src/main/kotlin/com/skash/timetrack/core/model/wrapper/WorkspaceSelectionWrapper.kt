package com.skash.timetrack.core.model.wrapper

import com.skash.timetrack.core.model.Workspace

data class WorkspaceSelectionWrapper(
    val workspace: Workspace,
    val isSelected: Boolean = false
)