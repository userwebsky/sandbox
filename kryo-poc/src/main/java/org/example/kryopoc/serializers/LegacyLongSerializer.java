package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LegacyLongSerializer extends Serializer<Long> {
  {
    setImmutable(true);
  }

  @Override
  public void write(Kryo kryo, Output output, Long object) {
    writeVarLong(output, object, false); // optimizePositive=false (ZigZag) - domyślne w Kryo 4
  }

  @Override
  public Long read(Kryo kryo, Input input, Class<? extends Long> type) {
    return readVarLong(input, false);
  }

  /**
   * Logika zapisu VarLong (z opcjonalnym ZigZag) skopiowana z Kryo 4.
   */
  public static int writeVarLong(Output output, long value, boolean optimizePositive) throws KryoException {
    if (!optimizePositive) value = (value << 1) ^ (value >> 63);

    // Zabezpieczenie miejsca w buforze (max 9 bajtów dla VarLong)
    //output.require(9); 'require(int)' jest protected access in 'com.esotericsoftware.kryo.io.Output'

    byte[] buffer = output.getBuffer();
    int position = output.position();

    if (value >>> 7 == 0) {
      buffer[position++] = (byte) value;
      output.setPosition(position);
      return 1;
    }
    if (value >>> 14 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7);
      output.setPosition(position);
      return 2;
    }
    if (value >>> 21 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7 | 0x80);
      buffer[position++] = (byte) (value >>> 14);
      output.setPosition(position);
      return 3;
    }
    if (value >>> 28 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7 | 0x80);
      buffer[position++] = (byte) (value >>> 14 | 0x80);
      buffer[position++] = (byte) (value >>> 21);
      output.setPosition(position);
      return 4;
    }
    if (value >>> 35 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7 | 0x80);
      buffer[position++] = (byte) (value >>> 14 | 0x80);
      buffer[position++] = (byte) (value >>> 21 | 0x80);
      buffer[position++] = (byte) (value >>> 28);
      output.setPosition(position);
      return 5;
    }
    if (value >>> 42 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7 | 0x80);
      buffer[position++] = (byte) (value >>> 14 | 0x80);
      buffer[position++] = (byte) (value >>> 21 | 0x80);
      buffer[position++] = (byte) (value >>> 28 | 0x80);
      buffer[position++] = (byte) (value >>> 35);
      output.setPosition(position);
      return 6;
    }
    if (value >>> 49 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7 | 0x80);
      buffer[position++] = (byte) (value >>> 14 | 0x80);
      buffer[position++] = (byte) (value >>> 21 | 0x80);
      buffer[position++] = (byte) (value >>> 28 | 0x80);
      buffer[position++] = (byte) (value >>> 35 | 0x80);
      buffer[position++] = (byte) (value >>> 42);
      output.setPosition(position);
      return 7;
    }
    if (value >>> 56 == 0) {
      buffer[position++] = (byte) ((value & 0x7F) | 0x80);
      buffer[position++] = (byte) (value >>> 7 | 0x80);
      buffer[position++] = (byte) (value >>> 14 | 0x80);
      buffer[position++] = (byte) (value >>> 21 | 0x80);
      buffer[position++] = (byte) (value >>> 28 | 0x80);
      buffer[position++] = (byte) (value >>> 35 | 0x80);
      buffer[position++] = (byte) (value >>> 42 | 0x80);
      buffer[position++] = (byte) (value >>> 49);
      output.setPosition(position);
      return 8;
    }
    buffer[position++] = (byte) ((value & 0x7F) | 0x80);
    buffer[position++] = (byte) (value >>> 7 | 0x80);
    buffer[position++] = (byte) (value >>> 14 | 0x80);
    buffer[position++] = (byte) (value >>> 21 | 0x80);
    buffer[position++] = (byte) (value >>> 28 | 0x80);
    buffer[position++] = (byte) (value >>> 35 | 0x80);
    buffer[position++] = (byte) (value >>> 42 | 0x80);
    buffer[position++] = (byte) (value >>> 49 | 0x80);
    buffer[position++] = (byte) (value >>> 56);
    output.setPosition(position);
    return 9;
  }

  /**
   * Logika odczytu VarLong (z opcjonalnym ZigZag) skopiowana z Kryo 4.
   */
  public static long readVarLong(Input input, boolean optimizePositive) throws KryoException {
    // Input.require(1) jest wbudowane w readByte
    long b = input.readByte();
    long result = b & 0x7F;
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
            if ((b & 0x80) != 0) {
              b = input.readByte();
              result |= (b & 0x7F) << 35;
              if ((b & 0x80) != 0) {
                b = input.readByte();
                result |= (b & 0x7F) << 42;
                if ((b & 0x80) != 0) {
                  b = input.readByte();
                  result |= (b & 0x7F) << 49;
                  if ((b & 0x80) != 0) {
                    b = input.readByte();
                    result |= b << 56;
                  }
                }
              }
            }
          }
        }
      }
    }
    if (!optimizePositive) {
      result = (result >>> 1) ^ -(result & 1);
    }
    return result;
  }
}
