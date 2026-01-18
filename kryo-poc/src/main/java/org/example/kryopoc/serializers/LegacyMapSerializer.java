package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class LegacyMapSerializer extends Serializer<Map> {

  @Override
  public void write(Kryo kryo, Output output, Map object) {
    LegacyAdapters.writeVarIntLegacy(output, object.size(), true);
    for (Object key : object.keySet()) {
      kryo.writeClassAndObject(output, key);
      kryo.writeClassAndObject(output, object.get(key));
    }
  }

  @Override
  public Map read(Kryo kryo, Input input, Class<? extends Map> type) {
    int length = LegacyAdapters.readVarIntLegacy(input, true);

    Map map = createMap(type);
    kryo.reference(map);

    for (int i = 0; i < length; i++) {
      Object key = kryo.readClassAndObject(input);
      Object value = kryo.readClassAndObject(input);
      map.put(key, value);
    }
    return map;
  }

  private Map createMap(Class<? extends Map> type) {
    if (type == HashMap.class) return new HashMap();
    if (type == TreeMap.class) return new TreeMap();
    if (type == ConcurrentHashMap.class) return new ConcurrentHashMap();
    try {
      return type.newInstance();
    } catch (Exception e) {
      return new HashMap();
    }
  }
}
