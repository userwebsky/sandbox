package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import static com.esotericsoftware.kryo.Kryo.NULL;

public class LegacyStringArraySerializer extends Serializer<String[]> {
  {
    setAcceptsNull(true);
  }

  @Override
  public void write(Kryo kryo, Output output, String[] object) {
    if (object == null) {
      new LegacyIntSerializer().writeVarInt(NULL, output);
      return;
    }
    new LegacyIntSerializer().writeVarInt(object.length + 1, output);

    LegacyStringSerializer stringSerializer = new LegacyStringSerializer();
    for (String s : object) {
      // Używamy LegacyStringSerializer do zapisu (z SOH 0x01)
      stringSerializer.write(kryo, output, s);
    }
  }

  @Override
  public String[] read(Kryo kryo, Input input, Class<? extends String[]> type) {
    int length = new LegacyIntSerializer().readVarInt(input);
    if (length == NULL) return null;

    length--;
    String[] array = new String[length];

    // Musimy użyć LegacyStringSerializer, aby "zjadł" bajt 0x01
    LegacyStringSerializer stringSerializer = new LegacyStringSerializer();

    for (int i = 0; i < length; i++) {
      // Czytamy bezpośrednio jako String (bez readClassAndObject, bo String[] jest typowany)
      array[i] = stringSerializer.read(kryo, input, String.class);
    }
    return array;
  }
}
