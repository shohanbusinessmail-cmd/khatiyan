# Keep Room entities & DAOs
-keep class androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Gson Models for Backup/Restore
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.shohan.khatiyan.data.model.** { *; }

# Keep Compose
-keepclassmembers class * extends androidx.compose.ui.Modifier
