package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Member
import com.skash.timetrack.core.model.Role
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class ApiTeamRepository : TeamRepository {

    override fun fetchTeamMembersForWorkspace(workspaceId: UUID): Observable<List<Member>> {
        return Observable.just(
            listOf(
                Member(workspaceId, workspaceId, "Test Name Admin", Role.ADMIN),
                Member(workspaceId, workspaceId, "Test Name Member", Role.MEMBER)
            )
        )
    }
}