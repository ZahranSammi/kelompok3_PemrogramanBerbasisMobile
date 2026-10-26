# Proguard / R8 rules for AplikasiPencatatanKeuanganPribadi
# Reference: OWASP Mobile M7 Insufficient Binary Protections

# Keep data models used across SQLite queries
-keep class com.example.aplikasipencatatankeuanganpribadi.model.** { *; }

# Keep Android SQLite and Support classes
-keepclassmembers class * extends android.database.sqlite.SQLiteOpenHelper {
    public <init>(...);
}
