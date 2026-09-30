-keep class com.jcraft.jsch.** { *; }
-keepclassmembers class com.jcraft.jsch.** { *; }
-dontwarn com.jcraft.jsch.**

-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }