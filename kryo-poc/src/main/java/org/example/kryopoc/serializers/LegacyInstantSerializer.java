package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Instant;

public class LegacyInstantSerializer extends Serializer<Instant> {

  // FieldSerializer: nanos(int), seconds(long).

  @Override
  public void write(Kryo kryo, Output output, Instant object) {
    new LegacyIntSerializer().writeVarInt(object.getNano(), true, output);
    // EpochSecond jako ZigZag (skoro primitive_long był ZigZag)
    LegacyLongSerializer.writeVarLong(output, object.getEpochSecond(), false);
  }

  @Override
  public Instant read(Kryo kryo, Input input, Class<? extends Instant> type) {
    int nanos = new LegacyIntSerializer().readVarInt(true, input);
    long seconds = LegacyLongSerializer.readVarLong(input, false);
    return Instant.ofEpochSecond(seconds, nanos);
  }
}
