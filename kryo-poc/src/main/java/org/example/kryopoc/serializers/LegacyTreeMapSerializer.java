package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LegacyTreeMapSerializer extends LegacyMapSerializer {

  @Override
  public void write(Kryo kryo, Output output, Map map) {
    TreeMap treeMap = (TreeMap) map;
    kryo.writeClassAndObject(output, treeMap.comparator());
    super.write(kryo, output, map);
  }

  @Override
  public Map read(Kryo kryo, Input input, Class<? extends Map> type) {
    // Kryo 4 TreeMapSerializer najpierw zapisywał komparator
    Comparator comparator = (Comparator) kryo.readClassAndObject(input);

    int length = LegacyAdapters.readVarIntLegacy(input, true);

    TreeMap map = new TreeMap(comparator);
    kryo.reference(map);

    for (int i = 0; i < length; i++) {
      Object key = kryo.readClassAndObject(input);
      Object value = kryo.readClassAndObject(input);
      map.put(key, value);
    }
    return map;
  }
}
