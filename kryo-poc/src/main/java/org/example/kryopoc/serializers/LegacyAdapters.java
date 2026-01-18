package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LegacyAdapters {

  // --- HELPERY: VarInt / VarLong (Logic from Kryo 4) ---

  public static int writeVarIntLegacy(Output output, int value, boolean optimizePositive) throws KryoException {
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

  public static int readVarIntLegacy(Input input, boolean optimizePositive) throws KryoException {
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

  // --- HELPERY: Fixed Big Endian (dla Float/Double/Short/Char) ---

  public static void writeIntBigEndian(Output output, int value) {
    output.writeByte(value >>> 24);
    output.writeByte(value >>> 16);
    output.writeByte(value >>> 8);
    output.writeByte(value);
  }

  public static int readIntBigEndian(Input input) {
    return ((input.readByte() & 0xFF) << 24) |
      ((input.readByte() & 0xFF) << 16) |
      ((input.readByte() & 0xFF) << 8) |
      (input.readByte() & 0xFF);
  }

  public static void writeLongBigEndian(Output output, long value) {
    output.writeByte((int) (value >>> 56));
    output.writeByte((int) (value >>> 48));
    output.writeByte((int) (value >>> 40));
    output.writeByte((int) (value >>> 32));
    output.writeByte((int) (value >>> 24));
    output.writeByte((int) (value >>> 16));
    output.writeByte((int) (value >>> 8));
    output.writeByte((int) value);
  }

  public static long readLongBigEndian(Input input) {
    return ((long) (input.readByte() & 0xFF) << 56) |
      ((long) (input.readByte() & 0xFF) << 48) |
      ((long) (input.readByte() & 0xFF) << 40) |
      ((long) (input.readByte() & 0xFF) << 32) |
      ((long) (input.readByte() & 0xFF) << 24) |
      ((long) (input.readByte() & 0xFF) << 16) |
      ((long) (input.readByte() & 0xFF) << 8) |
      ((long) (input.readByte() & 0xFF));
  }

  // --- SERIALIZERY WRAPPERÓW (Wykorzystujące powyższe helpery) ---

  public static class LegacyDoubleSerializer extends Serializer<Double> {
    { setImmutable(true); }
    @Override
    public void write(Kryo kryo, Output output, Double object) {
      writeLongBigEndian(output, Double.doubleToLongBits(object));
    }
    @Override
    public Double read(Kryo kryo, Input input, Class<? extends Double> type) {
      return Double.longBitsToDouble(readLongBigEndian(input));
    }
  }

  public static class LegacyFloatSerializer extends Serializer<Float> {
    { setImmutable(true); }
    @Override
    public void write(Kryo kryo, Output output, Float object) {
      writeIntBigEndian(output, Float.floatToIntBits(object));
    }
    @Override
    public Float read(Kryo kryo, Input input, Class<? extends Float> type) {
      return Float.intBitsToFloat(readIntBigEndian(input));
    }
  }

  public static class LegacyShortSerializer extends Serializer<Short> {
    { setImmutable(true); }
    @Override
    public void write(Kryo kryo, Output output, Short object) {
      output.writeByte(object >>> 8);
      output.writeByte(object.intValue());
    }
    @Override
    public Short read(Kryo kryo, Input input, Class<? extends Short> type) {
      return (short) (((input.readByte() & 0xFF) << 8) | (input.readByte() & 0xFF));
    }
  }

  public static class LegacyCharSerializer extends Serializer<Character> {
    { setImmutable(true); }
    @Override
    public void write(Kryo kryo, Output output, Character object) {
      output.writeByte(object >>> 8);
      output.writeByte(object.charValue());
    }
    @Override
    public Character read(Kryo kryo, Input input, Class<? extends Character> type) {
      return (char) (((input.readByte() & 0xFF) << 8) | (input.readByte() & 0xFF));
    }
  }

  public static class LegacyBooleanSerializer extends Serializer<Boolean> {
    { setImmutable(true); }
    @Override
    public void write(Kryo kryo, Output output, Boolean object) {
      output.writeBoolean(object);
    }
    @Override
    public Boolean read(Kryo kryo, Input input, Class<? extends Boolean> type) {
      return input.readBoolean();
    }
  }
}
