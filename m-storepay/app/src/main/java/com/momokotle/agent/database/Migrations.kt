package com.momokotle.agent.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Creates frequent_ussd_numbers (later dropped in 16→17). Kept for upgrade path. */
val MIGRATION_15_16 = object : Migration(15, 16) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS frequent_ussd_numbers (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                ussdCode TEXT NOT NULL,
                label TEXT NOT NULL,
                usageCount INTEGER NOT NULL,
                lastUsedAt INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                isPinned INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE UNIQUE INDEX IF NOT EXISTS index_frequent_ussd_numbers_ussdCode " +
                "ON frequent_ussd_numbers(ussdCode)"
        )
    }
}

/** Removes frequent USSD stats table — feature cancelled; contacts remain. */
val MIGRATION_16_17 = object : Migration(16, 17) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS frequent_ussd_numbers")
    }
}

/** Built-in USSD flows cached in Room for offline / no-per-transaction API. */
val MIGRATION_17_18 = object : Migration(17, 18) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS system_ussd_flows (
                flowKey TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                ussdCode TEXT NOT NULL,
                flowJson TEXT NOT NULL,
                updatedAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}
