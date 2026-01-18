package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Emuluje FieldSerializer z Kryo 4 dla LocalDateTime.
 * Pola w Java 8 LocalDateTime: date (LocalDate), time (LocalTime).
 * FieldSerializer sortuje alfabetycznie: date, time.
 */
public class LegacyLocalDateTimeSerializer extends Serializer<LocalDateTime> {

  @Override
  public void write(Kryo kryo, Output output, LocalDateTime object) {
    kryo.writeObject(output, object.toLocalDate());
    kryo.writeObject(output, object.toLocalTime());
  }

  @Override
  public LocalDateTime read(Kryo kryo, Input input, Class<? extends LocalDateTime> type) {
    // Ważne: to wykorzysta zarejestrowane LegacyLocalDateSerializer i LegacyLocalTimeSerializer
    LocalDate date = kryo.readObject(input, LocalDate.class);
    LocalTime time = kryo.readObject(input, LocalTime.class);
    return LocalDateTime.of(date, time);
  }
}
