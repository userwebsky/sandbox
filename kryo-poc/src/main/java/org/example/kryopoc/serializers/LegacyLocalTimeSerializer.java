package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalTime;

public class LegacyLocalTimeSerializer extends Serializer<LocalTime> {

  @Override
  public void write(Kryo kryo, Output output, LocalTime object) {
    output.writeByte(object.getHour());
    output.writeByte(object.getMinute());
    new LegacyIntSerializer().writeVarInt(object.getNano(), true, output);
    output.writeByte(object.getSecond());
  }

  @Override
  public LocalTime read(Kryo kryo, Input input, Class<? extends LocalTime> type) {
    byte hour = input.readByte();
    byte minute = input.readByte();
    int nano = new LegacyIntSerializer().readVarInt(true, input);
    byte second = input.readByte();
    return LocalTime.of(hour, minute, second, nano);
  }
}
