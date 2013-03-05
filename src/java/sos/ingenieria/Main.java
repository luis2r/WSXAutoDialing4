/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;

/**
 *
 * @author luis
 */


import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Main {
  public void main(String arg[]) throws Exception{
    String xmlRecords = "<data>"
            + "             <employee>"
            + "                 <name>A</name>"
        + "                     <title>Manager</title>"
            + "             </employee>"
            + "         </data>"
            ;
    
    
           xmlRecords = "<alarmas>" 
                        +    "<alarma>" 
                        +           "<alarmaId>89</alarmaId>" 
                        +           "<destino>104</destino>" 
                        +           "<mensaje>éste es un mensaje generado para probar la funcionalidad de una sola llamada con conversión de texto a voz, muchas gracias, áterisk es un programa de licencia libre que proporciona funcionalidades de una central telefónica. Como cualquier pbx se puede conectar un número determinado de teléfonos para hacer llamadas entre si e incluso conectar a un prvedor vo i p tanto básicos como primarios. Asterisk incluye muchas características que anteriormente sólo estaban disponibles en costosos sistemas propietarios PBX, como buzón de voz, conferencias, etc</mensaje>" 
                        +           "<numeroReintentos>3</numeroReintentos>" 
                        +           "<tiempoReintentos>3</tiempoReintentos>" 
                        +    "</alarma>"
                        +"</alarmas>";

    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(xmlRecords));

    Document doc = db.parse(is);
    NodeList nodes = doc.getElementsByTagName("alarma");

    for (int i = 0; i < nodes.getLength(); i++) {
      Element element = (Element) nodes.item(i);

      NodeList alarmaId = element.getElementsByTagName("alarmaId");
      Element line = (Element) alarmaId.item(0);
      System.out.println("alarmaId: " + getCharacterDataFromElement(line));

      NodeList destino = element.getElementsByTagName("destino");
      line = (Element) destino.item(0);
//      String d=getCharacterDataFromElement(line);
      System.out.println("destino: " + getCharacterDataFromElement(line));
      
      NodeList mensaje = element.getElementsByTagName("mensaje");
      line = (Element) mensaje.item(0);
      System.out.println("mensaje: " + getCharacterDataFromElement(line));
      
      NodeList numeroReintentos = element.getElementsByTagName("numeroReintentos");
      line = (Element) numeroReintentos.item(0);
      System.out.println("numeroReintentos: " + getCharacterDataFromElement(line));
      
      NodeList tiempoReintentos = element.getElementsByTagName("tiempoReintentos");
      line = (Element) tiempoReintentos.item(0);
      System.out.println("tiempoReintentos: " + getCharacterDataFromElement(line));
    }

  }

  public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
      CharacterData cd = (CharacterData) child;
      return cd.getData();
    }
    return "";
  }
}