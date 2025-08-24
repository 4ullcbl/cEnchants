# Основные опции
-dontoptimize

# Сохраняем аннотации и другую важную информацию
-keepattributes *Annotation*,Signature,Exceptions,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable

# Для Java 9+ модульной системы
-keep class module-info

# Игнорируем предупреждения о системных классах JDK
-dontwarn jdk.internal.**
-dontnote jdk.internal.**

# Игнорируем предупреждения о дубликатах
-ignorewarnings

# Сохраняем плагин Bukkit/Spigot
-keep class su.trident.cenchants.CEnchants {
    public *;
}

# Сохраняем обработчики событий
-keepclassmembers class * extends org.bukkit.event.Event {
    public *;
}

# Сохраняем команды
-keepclassmembers class * {
    @org.bukkit.command.Command *;
}