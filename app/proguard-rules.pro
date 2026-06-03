# ========================
# AzHua ProGuard Rules
# ========================

# Hilt
-keepattributes *Annotation*
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
-keepclassmembers class * {
    @dagger.assisted.Assisted <fields>;
    @dagger.assisted.AssistedFactory <methods>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static ** INSTANCE;
}
-keep class com.azhua.core.database.entity.** { *; }
-keep class com.azhua.core.database.dao.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Coil
-keep class coil3.** { *; }

# Media3/ExoPlayer
-keep class androidx.media3.** { *; }

# Extension API
-keep class com.azhua.extension.api.** { *; }
-keep class com.azhua.core.model.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
