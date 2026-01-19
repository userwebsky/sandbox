package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LegacyTreeSetSerializer extends LegacyCollectionSerializer {

  @Override
  public void write(Kryo kryo, Output output, Collection collection) {
    TreeSet treeSet = (TreeSet) collection;
    kryo.writeClassAndObject(output, treeSet.comparator());
    super.write(kryo, output, collection);
  }

  @Override
  public Collection read(Kryo kryo, Input input, Class<? extends Collection> type) {
    // Kryo 4 TreeSetSerializer najpierw zapisywał komparator!
    Comparator comparator = (Comparator) kryo.readClassAndObject(input);

    // Teraz czytamy resztę (długość + elementy) używając logiki z LegacyCollectionSerializer
    // Ale musimy stworzyć TreeSet z tym komparatorem

    int length = LegacyAdapters.readVarIntLegacy(input, true);

    TreeSet collection = new TreeSet(comparator);
    kryo.reference(collection);

    for (int i = 0; i < length; i++) {
      Object element = kryo.readClassAndObject(input);
      collection.add(element);
    }
    return collection;
  }
}
