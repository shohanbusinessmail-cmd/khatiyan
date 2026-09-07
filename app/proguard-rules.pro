# Keep Room entities & DAOs
-keep class androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Gson Models for Backup/Restore
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.shohan.khatiyan.data.model.** { *; }

# Room entities are serialised by Gson inside the JSON backup payload, so their
# field names must survive R8 or restore silently produces empty ledgers.
-keep class com.shohan.khatiyan.data.local.entities.** { *; }

# Keep Compose
-keepclassmembers class * extends androidx.compose.ui.Modifier
