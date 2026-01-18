package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LegacyStringArraySerializer extends Serializer<String[]> {

  @Override
  public void write(Kryo kryo, Output output, String[] object) {
    LegacyAdapters.writeVarIntLegacy(output, object.length, true);
    for (String s : object) {
      kryo.writeClassAndObject(output, s); // Tu Kryo 4 mogło używać writeString lub writeClassAndObject zależnie od konfigu.
      // Domyślnie elementsCanBeNull=true -> writeClassAndObject.
      // Sprawdźmy testy: jeśli failuje, zmienimy na writeString.
    }
  }

  @Override
  public String[] read(Kryo kryo, Input input, Class<? extends String[]> type) {
    int length = LegacyAdapters.readVarIntLegacy(input, true);
    String[] array = new String[length];
    // kryo.reference(array); // Tablice rzadziej bywają cykliczne, ale można dodać
    for (int i = 0; i < length; i++) {
      array[i] = (String) kryo.readClassAndObject(input);
    }
    return array;
  }
}
