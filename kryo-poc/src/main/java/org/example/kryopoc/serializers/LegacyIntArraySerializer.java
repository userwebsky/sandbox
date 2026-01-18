package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import static com.esotericsoftware.kryo.Kryo.NULL;

public class LegacyIntArraySerializer extends Serializer<int[]> {

  @Override
  public void write(Kryo kryo, Output output, int[] object) {
    if (object == null) {
      output.writeVarInt(NULL, true);
      return;
    }
    writeVarInt(object.length + 1, true, output);
    writeInts(object, false, output);
  }

  public void writeInts (int[] object, boolean optimizePositive, Output output) throws KryoException {
    for (int i = 0, n = object.length; i < n; i++)
      writeInt(object[i], optimizePositive, output);
  }

  public int writeInt (int value, boolean optimizePositive, Output output) throws KryoException {
    return writeVarInt(value, optimizePositive, output);
  }

  public int writeVarInt (int value, boolean optimizePositive, Output output) throws KryoException {
    if (!optimizePositive) value = (value << 1) ^ (value >> 31);
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

  @Override
  public int[] read(Kryo kryo, Input input, Class<? extends int[]> aClass) {
    int length = input.readVarInt(true);
    if (length == NULL) return null;
    return readInts(length - 1, false, input);
  }

  public int[] readInts (int length, boolean optimizePositive, Input input) throws KryoException {
    int[] array = new int[length];
    for (int i = 0; i < length; i++)
      array[i] = readInt(optimizePositive, input);
    return array;
  }

  public int readInt (boolean optimizePositive, Input input) throws KryoException {
    return readVarInt(optimizePositive, input);
  }

  public int readVarInt (boolean optimizePositive, Input input) throws KryoException {
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
    return optimizePositive ? result : ((result >>> 1) ^ -(result & 1));
  }
}
