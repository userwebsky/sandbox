package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import static com.esotericsoftware.kryo.Kryo.NULL;

public class LegacyIntArraySerializer extends Serializer<int[]> {
  {
    setAcceptsNull(true);
  }

  @Override
  public void write(Kryo kryo, Output output, int[] object) {
    if (object == null) {
      LegacyAdapters.writeVarIntLegacy(output, NULL, true);
      return;
    }
    // Długość: optimizePositive=true
    LegacyAdapters.writeVarIntLegacy(output, object.length + 1, true);

    for (int i : object) {
      // Elementy: optimizePositive=false (ZigZag)
      LegacyAdapters.writeVarIntLegacy(output, i, false);
    }
  }

  @Override
  public int[] read(Kryo kryo, Input input, Class<? extends int[]> type) {
    // Długość: optimizePositive=true
    int length = LegacyAdapters.readVarIntLegacy(input, true);
    if (length == NULL) return null;

    length--;
    int[] array = new int[length];
    for (int i = 0; i < length; i++) {
      // Elementy: optimizePositive=false (ZigZag)
      array[i] = LegacyAdapters.readVarIntLegacy(input, false);
    }
    return array;
  }
}
