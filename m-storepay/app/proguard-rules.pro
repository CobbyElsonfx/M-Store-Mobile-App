# Paystack
-keep class com.paystack.** { *; }
-dontwarn com.paystack.**

# Retrofit / OkHttp / Gson
-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# API models must keep Gson field names
-keep class com.momokotle.agent.data.remote.** { *; }

# Room-cached USSD flows are Gson-serialized; R8 must not rename fields or
# List element types (otherwise release builds crash on every USSD button).
-keep class com.momokotle.agent.ussd.UssdFlow { *; }
-keep class com.momokotle.agent.ussd.UssdStep { *; }
-keepclassmembers enum com.momokotle.agent.models.UssdActionType { *; }
-keepclassmembers enum com.momokotle.agent.models.TransactionType { *; }
-keep class * extends com.google.gson.reflect.TypeToken { *; }

# Hilt / Dagger
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**
