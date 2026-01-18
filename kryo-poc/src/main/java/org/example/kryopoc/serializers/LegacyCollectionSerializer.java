package org.example.kryopoc.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

@SuppressWarnings("rawtypes")
public class LegacyCollectionSerializer extends Serializer<Collection> {

  // W Kryo 4 CollectionSerializer używał domyślnie elementsCanBeNull = true

  @Override
  public void write(Kryo kryo, Output output, Collection object) {
    LegacyAdapters.writeVarIntLegacy(output, object.size(), true);
    for (Object element : object) {
      kryo.writeClassAndObject(output, element);
    }
  }

  @Override
  public Collection read(Kryo kryo, Input input, Class<? extends Collection> type) {
    // Kryo 4 zapisywało rozmiar jako VarInt(true)
    int length = LegacyAdapters.readVarIntLegacy(input, true);

    Collection collection = createCollection(type);
    kryo.reference(collection); // Ważne dla grafów cyklicznych wewnątrz kolekcji!

    for (int i = 0; i < length; i++) {
      Object element = kryo.readClassAndObject(input);
      collection.add(element);
    }
    return collection;
  }

  private Collection createCollection(Class<? extends Collection> type) {
    if (type == ArrayList.class) return new ArrayList();
    if (type == HashSet.class) return new HashSet();
    if (type == TreeSet.class) return new TreeSet();
    if (type == LinkedList.class) return new LinkedList();
    // Fallback
    try {
      return type.newInstance();
    } catch (Exception e) {
      return new ArrayList();
    }
  }
}
