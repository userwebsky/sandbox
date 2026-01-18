package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.KryoException;

import static com.esotericsoftware.kryo.Kryo.NULL;

public class LegacyIntArraySerializer extends Serializer<int[]> {
  {
    setAcceptsNull(true);
  }

  @Override
  public void write(Kryo kryo, Output output, int[] object) {
    if (object == null) {
      writeVarInt(output, NULL, true);
      return;
    }
    writeVarInt(output, object.length + 1, true);
    for (int i = 0; i < object.length; i++) {
      writeVarInt(output, object[i], false); // false = ZigZag (Kryo 4 default for array elements)
    }
  }

  @Override
  public int[] read(Kryo kryo, Input input, Class<? extends int[]> type) {
    int length = readVarInt(input, true);
    if (length == NULL) return null;

    length--;
    int[] array = new int[length];
    for (int i = 0; i < length; i++) {
      array[i] = readVarInt(input, false); // false = ZigZag
    }
    return array;
  }

  // --- Helpery VarInt z poprawną obsługą pozycji Input/Output ---

  private int readVarInt(Input input, boolean optimizePositive) throws KryoException {
    // Używamy input.readByte(), który dba o pozycję!
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
