package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
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
      writeVarInt(output, NULL, true);
      return;
    }
    writeVarInt(output, object.length + 1, true);

    LegacyStringSerializer stringSerializer = new LegacyStringSerializer();
    for (String s : object) {
      stringSerializer.write(kryo, output, s);
    }
  }

  @Override
  public String[] read(Kryo kryo, Input input, Class<? extends String[]> type) {
    int length = readVarInt(input, true);
    if (length == NULL) return null;

    length--;
    String[] array = new String[length];

    LegacyStringSerializer stringSerializer = new LegacyStringSerializer();
    for (int i = 0; i < length; i++) {
      array[i] = stringSerializer.read(kryo, input, String.class);
    }
    return array;
  }

  // --- Helpery VarInt (skopiowane z LegacyIntArraySerializer, bo nie chcę zależności od zewnętrznych klas w tym przykładzie) ---
  // W produkcji warto wydzielić do LegacyAdapters.

  private int readVarInt(Input input, boolean optimizePositive) throws KryoException {
    int b = input.readByte();
    int result = b & 0x7F;
    if ((b & 0x80) != 0) {
      b = input.readByte();
      result |= (b & 0x7F) << 7;
      if ((b & 0x80) != 0) {
        b = input.readByte();
        result |= (b & 0x7F) << 14;
        if ((b & 0x80) != 0) {
          b = input.readByte();
          result |= (b & 0x7F) << 21;
          if ((b & 0x80) != 0) {
            b = input.readByte();
            result |= (b & 0x7F) << 28;
          }
        }
      }
    }
    return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
  }

  private int writeVarInt(Output output, int value, boolean optimizePositive) throws KryoException {
    if (!optimizePositive) value = (value << 1) ^ (value >> 31);
    if (value >>> 7 == 0) {
      output.writeByte((byte)value);
      return 1;
    }
    if (value >>> 14 == 0) {
      output.writeByte((byte)((value & 0x7F) | 0x80));
      output.writeByte((byte)(value >>> 7));
      return 2;
    }
    if (value >>> 21 == 0) {
      output.writeByte((byte)((value & 0x7F) | 0x80));
      output.writeByte((byte)(value >>> 7 | 0x80));
      output.writeByte((byte)(value >>> 14));
      return 3;
    }
    if (value >>> 28 == 0) {
      output.writeByte((byte)((value & 0x7F) | 0x80));
      output.writeByte((byte)(value >>> 7 | 0x80));
      output.writeByte((byte)(value >>> 14 | 0x80));
      output.writeByte((byte)(value >>> 21));
      return 4;
    }
    output.writeByte((byte)((value & 0x7F) | 0x80));
    output.writeByte((byte)(value >>> 7 | 0x80));
    output.writeByte((byte)(value >>> 14 | 0x80));
    output.writeByte((byte)(value >>> 21 | 0x80));
    output.writeByte((byte)(value >>> 28));
    return 5;
  }
}
