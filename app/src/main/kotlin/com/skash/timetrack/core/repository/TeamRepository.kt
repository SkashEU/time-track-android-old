package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Member
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

interface TeamRepository {

    fun fetchTeamMembersForWorkspace(workspaceId: UUID): Observable<List<Member>>
}