package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.UUID;

public class LegacyUUIDSerializer extends Serializer<UUID> {

  @Override
  public void write(Kryo kryo, Output output, UUID object) {
    LegacyLongSerializer.writeVarLong(output, object.getLeastSignificantBits(), false);
    LegacyLongSerializer.writeVarLong(output, object.getMostSignificantBits(), false);
  }

  @Override
  public UUID read(Kryo kryo, Input input, Class<? extends UUID> aClass) {
    long leastSigBits = LegacyLongSerializer.readVarLong(input, false);
    long mostSigBits = LegacyLongSerializer.readVarLong(input, false);
    return new UUID(mostSigBits, leastSigBits);
  }
}
