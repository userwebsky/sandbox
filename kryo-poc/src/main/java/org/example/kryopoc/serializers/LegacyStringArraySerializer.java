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
      LegacyAdapters.writeVarIntLegacy(output, NULL, true);
      return;
    }
    LegacyAdapters.writeVarIntLegacy(output, object.length + 1, true);

    LegacyStringSerializer stringSerializer = new LegacyStringSerializer();
    for (String s : object) {
      // W V4 String[] zapisuje: [RefID] [SOH] [Len] [Chars] (dla każdego elementu)
      // Używamy writeObjectOrNull, żeby zapisać RefID
      kryo.writeObjectOrNull(output, s, stringSerializer);
    }
  }

  @Override
  public String[] read(Kryo kryo, Input input, Class<? extends String[]> type) {
    int length = LegacyAdapters.readVarIntLegacy(input, true);
    if (length == NULL) return null;

    length--;
    String[] array = new String[length];

    LegacyStringSerializer stringSerializer = new LegacyStringSerializer();
    for (int i = 0; i < length; i++) {
      // W V4 elementy tablicy obiektów (String[]) mają RefID.
      // readObjectOrNull obsłuży RefID i dopiero potem zawoła stringSerializer.read
      array[i] = kryo.readObjectOrNull(input, String.class, stringSerializer);
    }
    return array;
  }
}
