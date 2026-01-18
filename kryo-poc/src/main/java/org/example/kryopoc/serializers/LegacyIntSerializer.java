package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LegacyIntSerializer extends Serializer<Integer> {
  {
    setImmutable(true);
  }

  @Override
  public void write(Kryo kryo, Output output, Integer object) {
    // Wrapper Integer w Kryo 4 używał domyślnie VarInt (ZigZag = false)!
    // W przeciwieństwie do pól w FieldSerializer (gdzie było true).
    LegacyAdapters.writeVarIntLegacy(output, object, false);
  }

  @Override
  public Integer read(Kryo kryo, Input input, Class<? extends Integer> aClass) {
    return LegacyAdapters.readVarIntLegacy(input, false);
  }

  // Metody publiczne dla innych serializerów (zachowują elastyczność)
  public void writeVarInt(int value, boolean optimizePositive, Output output) {
    LegacyAdapters.writeVarIntLegacy(output, value, optimizePositive);
  }

  public int readVarInt(boolean optimizePositive, Input input) {
    return LegacyAdapters.readVarIntLegacy(input, optimizePositive);
  }
}
