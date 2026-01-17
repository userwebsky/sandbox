package org.example.kryopoc.services;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class KryoService {

  /**
   * Konfiguracja Kryo 5.x.
   * UWAGA: W Kryo 5 domyślne ustawienia są inne niż w Kryo 4!
   * - registrationRequired jest domyślnie TRUE (w Kryo 4 było false).
   * - Inne mechanizmy obsługi referencji i stringów.
   * * Na potrzeby Etapu 2 celowo zostawiamy "naiwną" konfigurację new Kryo(),
   * aby zademonstrować błędy migracji.
   */
  private Kryo createKryoInstance() {
    Kryo kryo = new Kryo();

    // Utrzymujemy strategię instancjonowania (to się nie zmieniło drastycznie w API),
    // aby w ogóle móc próbować tworzyć obiekty bez konstruktorów.
    kryo.setInstantiatorStrategy(new org.objenesis.strategy.StdInstantiatorStrategy());

    return kryo;
  }

  public String serialize(Object object) {
    if (object == null) {
      return null;
    }

    Kryo kryo = createKryoInstance();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Output output = new Output(outputStream);

    try {
      kryo.writeClassAndObject(output, object);
      output.flush();
      byte[] bytes = outputStream.toByteArray();
      return Base64.getEncoder().encodeToString(bytes);
    } finally {
      output.close();
    }
  }

  public Object deserialize(String base64Data) {
    if (base64Data == null) {
      return null;
    }

    Kryo kryo = createKryoInstance();
    byte[] bytes = Base64.getDecoder().decode(base64Data);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    Input input = new Input(inputStream);

    try {
      return kryo.readClassAndObject(input);
    } finally {
      input.close();
    }
  }
}
