package org.example.kryopoc.services;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.stereotype.Service;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class KryoService {

  /**
   * Konfiguracja Kryo 4.x.
   * Dodano StdInstantiatorStrategy, aby obsługiwać klasy bez konstruktora bezargumentowego
   * (takie jak UUID czy Arrays.asList), co jest standardem w większości wdrożeń.
   */
  private Kryo createKryoInstance() {
    Kryo kryo = new Kryo();
    // Fallback: jeśli nie ma konstruktora bezargumentowego, użyj Objenesis (StdInstantiatorStrategy)
    kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
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
