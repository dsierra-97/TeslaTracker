package com.sierrapor.teslatracker.data;

import androidx.room.TypeConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @TypeConverter
    public static LocalDate fromString(String value) {
        return value == null ? null : LocalDate.parse(value, formatter);
    }

    @TypeConverter
    public static String toString(LocalDate date) {
        return date == null ? null : date.format(formatter);
    }

    // Convierte List<players> a String para almacenar en la base de datos
    @TypeConverter
    public static String fromPlayersList(List<Tesla.players> players) {
        if (players == null || players.isEmpty()) {
            return "";
        }
        return players.stream()
                .map(Enum::name)
                .collect(Collectors.joining(",")); // Separa los valores por comas
    }

    // Convierte una String de la base de datos a List<players>
    @TypeConverter
    public static List<Tesla.players> toPlayersList(String data) {
        if (data == null || data.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(data.split(","))
                .map(Tesla.players::valueOf)
                .collect(Collectors.toList());
    }
}
