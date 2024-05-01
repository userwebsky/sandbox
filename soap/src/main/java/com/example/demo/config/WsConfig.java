package com.example.demo.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
//@EnableWs - adnotacja ta aktywuje automatyczną konfigurację funkcji serwera SOAP.
@Configuration
public class WsConfig {

  /**
   * public ServletRegistrationBean messageDispatcherServlet(ApplicationContext
   * applicationContext) - rejestruje MessageDispatcherServlet
   * w kontekście aplikacji Spring. MessageDispatcherServlet
   * jest wymagany do obsługi żądań SOAP. Ten specyficzny
   * Servlet inicjalizuje infrastrukturę usług webowych Spring.
   * @param applicationContext
   * @return
   */
  @Bean
  public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext){
    MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
    messageDispatcherServlet.setApplicationContext(applicationContext);
    messageDispatcherServlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean(messageDispatcherServlet,"/ws/*");
  }

  /**
   * public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema studentsSchema)
   * - Wsdl11Definition to klasa pomocnicza do tworzenia plików definicji usług sieci
   * Web (WSDL 1.1) na podstawie XSD. Plik definicji usługi sieci Web to XML, który
   * zawiera informacje o usługach sieci Web, takie jak nazwa usługi, port, schemat,
   * metoda itp.
   * @param studentsSchema
   * @return
   */
  @Bean(name="students")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema studentsSchema){
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("StudentPort");
    wsdl11Definition.setLocationUri("/ws");
    wsdl11Definition.setTargetNamespace("https://robert-programista.pl/soup-example");
    wsdl11Definition.setSchema(studentsSchema);
    return wsdl11Definition;
  }

  /**
   * public XsdSchema studentsSchema() - ta metoda tworzy schemat XSD z pliku XSD dostępnego w katalogu zasobów.
   * Ten schemat jest używany do tworzenia WSDL.
   * @return
   */
  @Bean
  public XsdSchema studentsSchema() {
    return new SimpleXsdSchema(new ClassPathResource("student.xsd"));
  }
}
