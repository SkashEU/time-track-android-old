package com.skash.timetrack.core.cache.seeder

import com.skash.timetrack.core.cache.model.RealmProjectColor
import io.realm.Realm

class ProjectColorSeeder : Realm.Transaction {

    override fun execute(realm: Realm) {
        realm.insert(
            listOf(
                RealmProjectColor(hex = "#54468d"),
                RealmProjectColor(hex = "#2d699a"),
                RealmProjectColor(hex = "#398ca8"),
                RealmProjectColor(hex = "#44b39e"),
                RealmProjectColor(hex = "#50db93"),
                RealmProjectColor(hex = "#83e377"),
                RealmProjectColor(hex = "#b9e768"),
                RealmProjectColor(hex = "#efea59"),
                RealmProjectColor(hex = "#f1c453"),
                RealmProjectColor(hex = "#f29e4c"),
                RealmProjectColor(hex = "#f3b055"),
                RealmProjectColor(hex = "#f27f51"),
                RealmProjectColor(hex = "#282828"),
                RealmProjectColor(hex = "#720226"),
                RealmProjectColor(hex = "#4f020a"),
            )
        )
    }

}