package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LegacyIntSerializer extends Serializer<Integer> {
  {
    setImmutable(true);
  }

  public void write (Kryo kryo, Output output, Integer object) {
    writeVarInt(object, output);
  }

  @Override
  public Integer read(Kryo kryo, Input input, Class<? extends Integer> aClass) {
    return readVarInt(input);
  }

  public int writeVarInt (int value, Output output) throws KryoException {
    byte[] buffer = output.getBuffer();
    int position = output.position();
    if (value >>> 7 == 0) {
      buffer[position++] = (byte)value;
      return 1;
    }
    if (value >>> 14 == 0) {
      buffer[position++] = (byte)((value & 0x7F) | 0x80);
      buffer[position++] = (byte)(value >>> 7);
      return 2;
    }
    if (value >>> 21 == 0) {
      buffer[position++] = (byte)((value & 0x7F) | 0x80);
      buffer[position++] = (byte)(value >>> 7 | 0x80);
      buffer[position++] = (byte)(value >>> 14);
      return 3;
    }
    if (value >>> 28 == 0) {
      buffer[position++] = (byte)((value & 0x7F) | 0x80);
      buffer[position++] = (byte)(value >>> 7 | 0x80);
      buffer[position++] = (byte)(value >>> 14 | 0x80);
      buffer[position++] = (byte)(value >>> 21);
      return 4;
    }
    buffer[position++] = (byte)((value & 0x7F) | 0x80);
    buffer[position++] = (byte)(value >>> 7 | 0x80);
    buffer[position++] = (byte)(value >>> 14 | 0x80);
    buffer[position++] = (byte)(value >>> 21 | 0x80);
    buffer[position++] = (byte)(value >>> 28);
    return 5;
  }

  public int readVarInt(Input input) {
    byte[] buffer = input.getBuffer();
    int position = input.position();
    int b = buffer[position++];
    int result = b & 0x7F;
    if ((b & 0x80) != 0) {
      b = buffer[position++];
      result |= (b & 0x7F) << 7;
      if ((b & 0x80) != 0) {
        b = buffer[position++];
        result |= (b & 0x7F) << 14;
        if ((b & 0x80) != 0) {
          b = buffer[position++];
          result |= (b & 0x7F) << 21;
          if ((b & 0x80) != 0) {
            b = buffer[position++];
            result |= (b & 0x7F) << 28;
          }
        }
      }
    }
    return ((result >>> 1) ^ -(result & 1));//optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
  }
}
