package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LegacyStringSerializer extends Serializer<String> {
  { setImmutable(true); }

  @Override
  public void write(Kryo kryo, Output output, String object) {
    // W trybie zapisu (gdybyśmy go używali) używamy standardowego formatu Kryo 5
    output.writeString(object);
  }

  @Override
  public String read(Kryo kryo, Input input, Class<? extends String> type) {
    // Kryo 4 zapisywało 0x01 przed stringiem ASCII (jeśli referencje były włączone)
    // Sprawdzamy pierwszy bajt
    if (input.canReadInt()) {
      int b = input.readByte() & 0xFF;

      if (b == 0x00) return null; // Null string

      if (b == 0x01) {
        // To jest ten "SOH" (Start of Heading) z Kryo 4. Ignorujemy go.
        // Następnie czytamy string standardową metodą, która oczekuje długości.
        // W Kryo 4 po 0x01 następowała długość (VarInt), co pasuje do readString() w Kryo 5.
        return input.readString();
      }

      // Jeśli to nie 0x01 ani 0x00, cofamy kursor i próbujemy czytać normalnie
      // (może to Unicode flag 0x80 lub po prostu długość)
      input.setPosition(input.position() - 1);
    }
    return input.readString();
  }
}
