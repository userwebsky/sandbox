package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDate;

public class LegacyLocalDateSerializer extends Serializer<LocalDate> {

  // FieldSerializer: day(short), month(short), year(int -> VarInt)

  @Override
  public void write(Kryo kryo, Output output, LocalDate object) {
    output.writeShort(object.getDayOfMonth());
    output.writeShort(object.getMonthValue());
    new LegacyIntSerializer().writeVarInt(object.getYear(), true, output);
  }

  @Override
  public LocalDate read(Kryo kryo, Input input, Class<? extends LocalDate> type) {
    short day = input.readShort();
    short month = input.readShort();
    // Year jako VarInt (optimizePositive=true)
    int year = new LegacyIntSerializer().readVarInt(true, input);
    return LocalDate.of(year, month, day);
  }
}
