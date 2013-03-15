/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * REST Web Service
 *
 * @author luis
 */
@Path("service")
public class ServicesAsterisk {

//    
//IDCLAVE2000, ESTADOLLAMADA, TIEMPOLLAMADASEG, IDSOSINGENIERIA, NUMREINTENTOCONTESTO, NUMTIEMPOTIMBRADO
//
// estado de la llamada:
//
//
//0=no se ha intentado
//1= Exitosa
//2=  No se estableció porque no se contesto
//3= No se estableció porque no dio tono de timbrado, estaba cortado
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServicesAsterisk
     */
    public ServicesAsterisk() {
    }

    /**
     * Retrieves representation of an instance of
     * sos.ingenieria.ServicesAsterisk
     *
     * @return an instance of java.lang.String
     */
//    @GET
//    @Produces("text/html")
//    public String getHtml() {
//        //TODO return proper representation object
//        return "hola";
//    }
//    @GET
//    @Produces("text/plain")
//    public String getText() {
//        //TODO return proper representation object
//        ConsultaHash con4 = new ConsultaHash(45, "c9e1074f5b3f9fc8ea15d152add07294");
//        String hashorigen = con4.consultar();
//        return hashorigen;
//    }
    /**
     * PUT method for updating or creating an instance of ServicesAsterisk
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
//    @PUT
//    @Consumes("text/html")
//    public void putHtml(String content) {
//    }
//
//    @POST
//    @Path("/oneCall/{idllamada}/{extorigen}/{numteldestino}/{numreintentos}/{tiemporeintento}")
//    @Produces({"text/plain"})
//    public String oneCall(@PathParam("idllamada") String idllamada, @PathParam("extorigen") String extorigen, @PathParam("numteldestino") String numteldestino, @PathParam("numreintentos") Integer numreintentos, @PathParam("tiemporeintento") Integer tiemporeintento) throws Exception {
//
//        String respuesta = null;
//
//
//
//        ConexionDataBase con = new ConexionDataBase(idllamada, extorigen, numteldestino, numreintentos, tiemporeintento);
//        con.InsertarLlamadaDataBase();
////
//        CapturaEventosDB capturaEventosDB = new CapturaEventosDB(extorigen, numteldestino, "idclave", "idsos", 1);
//        capturaEventosDB.start();
//
//        OneCall oneCall = new OneCall();
//
//        for (int i = 0; i <= numreintentos; i++) {
//            respuesta = oneCall.call(numteldestino, extorigen);
////                        respuesta = oneCall.call("1010", "277");
//            if (respuesta.equals("Conexión establecida con el cliente")) {
//                break;
//            }
//
//            Thread.sleep(tiemporeintento * 3600);
//
//        }
//
//        return respuesta;
//    }
    @GET
    @Path("/oneCall/{urlbdstring}/{idllamada}/{extorigen}/{numteldestino}/{numreintentos}/{tiemporeintento}")
    @Produces({"text/plain"})
    public String oneCall1(@PathParam("urlbdstring") String urlbdstring, @PathParam("idllamada") String idllamada, @PathParam("extorigen") String extorigen, @PathParam("numteldestino") String numteldestino, @PathParam("numreintentos") Integer numreintentos, @PathParam("tiemporeintento") Integer tiemporeintento) throws Exception {

//        String respuesta = null;
        String respuestallam = null;

        urlbdstring=urlbdstring.replace('|', '/');
        urlbdstring=urlbdstring.replace('*', '.');
        urlbdstring=urlbdstring.replace('$', '?');
        urlbdstring=urlbdstring.replace('&', ':');
        // Base64 decode
                //        urlbdstring = new String(Base64.decode(urlbdstring));
                //        urlbdstring = new String(Base32.decode(urlbdstring));




        ConexionDataBase con = new ConexionDataBase(idllamada, extorigen, numteldestino, numreintentos, tiemporeintento);
        con.InsertarLlamadaDataBase();
//
        ConsultaIdSOS con2 = new ConsultaIdSOS(idllamada);
        String idsos = con2.consultar();

//        CapturaEventosDB capturaEventosDB = new CapturaEventosDB(extorigen, numteldestino, idllamada, idsos, numreintentos, "");
//        capturaEventosDB.start();



//        OneCall oneCall = new OneCall();

//        for (int i = 1; i <= numreintentos; i++) {
//            respuesta = oneCall.call(numteldestino, extorigen);
////                        respuesta = oneCall.call("1010", "277");
//            if (respuesta.equals("Conexión establecida con el cliente")) {
//                break;
//            }
//            System.out.println("SIGUIENTE REINTENTO");
//            Thread.sleep(tiemporeintento * 3600);
//
//        }

        String[] resp = null;

        int i = 0;
        for (i = 1; i <= numreintentos; i++) {
            AsteriskCallEventsStateProd call = new AsteriskCallEventsStateProd();
            call.setNumber(numteldestino); //destino
            call.setMessage(extorigen);//origen


//            resp = call.originate();

            long tiempoInicio = System.currentTimeMillis();
            System.out.println("El tiempo de inicio es :" + tiempoInicio + " miliseg");
            resp = call.originate();
            long tiempoFin = System.currentTimeMillis();
            System.out.println("El tiempo de fin es :" + tiempoFin + " miliseg");
            long totalTiempo = tiempoFin - tiempoInicio;


            if (resp[0].equals("1")) {
                break;
            }




            if (resp[0].equals("4") && totalTiempo > 30000L) {
                resp[0] = "2";
            }

            for (String resp1 : resp) {
                System.out.println(resp1);
            }
            System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");








            System.out.println("SIGUIENTE REINTENTO");
            Thread.sleep(tiemporeintento * 3600);

        }
        if (resp[0].equals("1") && resp[1].equals("0")) {
            resp[0] = "4";
        }

        if (!resp[0].equals("1")) {
            resp[1] = "0";
            resp[2] = "0";
            i = 0;//reintento contestado

        }
//        if (resp[0].equals("1") && Integer.parseInt(resp[1]) < 10) {
//            resp[0] = "5";
//        }

        respuestallam = idllamada + "|" + resp[0] + "|" + resp[1] + "|" + idsos + "|" + i + "|" + resp[2];


        String encodeString = Base64.encodeToString(respuestallam.getBytes(), true);
        System.out.println("encodeToString " + encodeString);
        System.out.println("La cadena base 32 es: " + urlbdstring);

//        String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//        String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

//        String httpcadena = "http://192.168.2.78/sifiv/llamadas_sistematizadas/respuesta_llamada.php?cadena=+" + encodeString;
        String httpcadena = urlbdstring + "=+" + encodeString;

        PeticionHttp post = new PeticionHttp(httpcadena);

        String respuesta = post.getRespueta();
        System.out.println("La respuesta a la cadena: " + httpcadena + " es:" + respuesta);

        respuestallam = "Proceso terminado exitosamente";


        return respuestallam;
    }

//    @POST
//    @Path("/oneAlarm")
//    @Consumes({"application/xml", "application/json"})
//    @Produces({"text/plain"})
//    public String oneAlarm(Alarma entity) throws Exception {
//        String numteldestino = entity.getDestino();
//        String idllamada = entity.getAlarmaId().toString();
//        String mensaje = entity.getMensaje();
//        int numreintentos = entity.getNumeroReintentos();
//        int tiemporeintento = entity.getTiempoReintentos();
//
//
//        String respuesta = null;
//
//
//
//        ConexionDataBase con = new ConexionDataBase(idllamada, "11100", numteldestino, numreintentos, tiemporeintento);
//        con.InsertarLlamadaDataBase();
//
//        ConsultaIdSOS con2 = new ConsultaIdSOS(idllamada);
//        String idsos = con2.consultar();
//////
////        CapturaEventosDB capturaEventosDB = new CapturaEventosDB("null", numteldestino, idllamada, idsos);
////        capturaEventosDB.start();
//
//
//
//        CapturaEventosAlarma capturaEventosDB = new CapturaEventosAlarma("null", numteldestino, idllamada, idsos, numreintentos);
//        capturaEventosDB.start();
//
//
//
//
////        textToSpeech.start();
//
//
//
//
//
////        OneCall oneCall = new OneCall();
//        TestTextToSpeech textToSpeech = new TestTextToSpeech(numteldestino, mensaje);
//
//        for (int i = 0; i <= numreintentos; i++) {
//            respuesta = textToSpeech.call();
////                        respuesta = oneCall.call("1010", "277");
//            if (respuesta.equals("Conexión establecida con el cliente")) {
//                break;
//            }
//
//            Thread.sleep(tiemporeintento * 3600);
//
//        }
//        respuesta = "Proceso terminado exitosamenteLa respuestaa la cadena es:" + respuesta;
//        return respuesta;
//
//
//
//    }
//    @POST
//    @Path("/oneAlarmXml/{mensaje}")
//    @Produces({"text/plain"})
//    public String oneAlarmXml(@PathParam("mensaje") String mensajeExterno) throws Exception {
//
//        String xmlRecords =
//         "<alarma>"
//                +"<alarma>"
//                    + "<alarmaId>89</alarmaId> "
//                    + "<destino>104</destino> "
//                    + "<mensaje>éste es un mensaje generado para probar la funcionalidad de una sola llamada con conversión de"
//                    + " texto a voz, muchas gracias, áterisk es un programa de licencia libre que proporciona funcionalidades "
//                    + "de una central telefónica. Como cualquier pbx se puede conectar un número determinado de teléfonos para "
//                    + "hacer llamadas entre si e incluso conectar a un prvedor vo i p tanto básicos como primarios. Asterisk "
//                    + "incluye muchas características que anteriormente sólo estaban disponibles en costosos sistemas "
//                    + "propietarios PBX, como buzón de voz, conferencias, etc</mensaje> "
//                    + "<numeroReintentos>3</numeroReintentos> "
//                    + "<tiempoReintentos>3</tiempoReintentos> "
//                + "</alarma>"
//        +"<alarma>";
//
//        try {
//            DocumentBuilderFactory dbf =
//                    DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            InputSource is = new InputSource();
//            is.setCharacterStream(new StringReader(xmlRecords));
//
//            Document doc = db.parse(is);
//            NodeList nodes = doc.getElementsByTagName("alarma");
//
//            // iterate the employees
//            for (int i = 0; i < nodes.getLength(); i++) {
//                Element element = (Element) nodes.item(i);
//
//                NodeList alarmaId = element.getElementsByTagName("alarmaId");
//                Element line = (Element) alarmaId.item(0);
////           System.out.println("Title: " + element.getCharacterDataFromElement(line));
//                NodeList destino = element.getElementsByTagName("destino");
//                line = (Element) destino.item(0);
//
//                NodeList mensaje = element.getElementsByTagName("mensaje");
//                line = (Element) mensaje.item(0);
//
//                NodeList numeroReintentos = element.getElementsByTagName("numeroReintentos");
//                line = (Element) numeroReintentos.item(0);
//
//
//                NodeList tiempoReintentos = element.getElementsByTagName("tiempoReintentos");
//                line = (Element) tiempoReintentos.item(0);
//
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return mensajeExterno;
//
//
//    }
//    @POST
//    @Path("/oneAlarmXml")
////    @Path("/oneAlarmXml/{id}/{mensaje}/{destino}/{2}/{1}")
//    @Consumes({"text/plain"})
//    @Produces({"text/plain"})
//    public String oneAlarmXml(/*@PathParam("id") String id,@PathParam("xmlRecords")*/String xmlRecords/*,@PathParam("destino") String destino*/) throws Exception {
//        xmlRecords = xmlRecords.substring(2);
//        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        InputSource is = new InputSource();
//        is.setCharacterStream(new StringReader(xmlRecords));
//
//        Document doc = db.parse(is);
//        NodeList nodes = doc.getElementsByTagName("alarma");
//        String respuesta = "";
//
//        String alarmaIdstring = "";
//        String destinostring = "";
//        String mensajestring = "";
//        int numeroReintentosstring = 0;
//        int tiempoReintentosstring = 0;
//        for (int i = 0; i < nodes.getLength(); i++) {
//            Element element = (Element) nodes.item(i);
//
//            NodeList alarmaId = element.getElementsByTagName("alarmaId");
//            Element line = (Element) alarmaId.item(0);
//            System.out.println("alarmaId: " + getCharacterDataFromElement(line));
//            alarmaIdstring = getCharacterDataFromElement(line);
//
//            NodeList destino = element.getElementsByTagName("destino");
//            line = (Element) destino.item(0);
////      String d=getCharacterDataFromElement(line);
//            System.out.println("destino: " + getCharacterDataFromElement(line));
//            destinostring = getCharacterDataFromElement(line);
//
//            NodeList mensaje = element.getElementsByTagName("mensaje");
//            line = (Element) mensaje.item(0);
//            System.out.println("mensaje: " + getCharacterDataFromElement(line));
//            mensajestring = getCharacterDataFromElement(line);
//
//            NodeList numeroReintentos = element.getElementsByTagName("numeroReintentos");
//            line = (Element) numeroReintentos.item(0);
//            System.out.println("numeroReintentos: " + getCharacterDataFromElement(line));
//            numeroReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));
//
//            NodeList tiempoReintentos = element.getElementsByTagName("tiempoReintentos");
//            line = (Element) tiempoReintentos.item(0);
//            System.out.println("tiempoReintentos: " + getCharacterDataFromElement(line));
//            tiempoReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));
//
////        OneCall oneCall = new OneCall();
//            TestTextToSpeech textToSpeech = new TestTextToSpeech(destinostring, mensajestring);
//
//            for (int j = 0; j <= numeroReintentosstring; j++) {
//                respuesta = textToSpeech.call();
////                        respuesta = oneCall.call("1010", "277");
//                if (respuesta.equals("Conexión establecida con el cliente")) {
//                    break;
//                }
//
//                Thread.sleep(tiempoReintentosstring * 3600);
//
//            }
//        }
//
//
//
//        respuesta = "Proceso terminado exitosamenteLa respuestaa la cadena es:" + respuesta;
//        return respuesta;
//    }
//    @POST
//    @Path("/oneAlarmXmltext")
////    @Path("/oneAlarmXml/{id}/{mensaje}/{destino}/{2}/{1}")
//    @Consumes({"text/plain"})
//    @Produces({"text/plain"})
//    public String oneAlarmXml3(/*@PathParam("id") String id,@PathParam("xmlRecords")*/String xmlRecords/*,@PathParam("destino") String destino*/) throws Exception {
//
//        xmlRecords = xmlRecords.substring(2);
////        xmlRecords = xmlRecords.substring(1);
//        return xmlRecords;
//    }
//    @POST
////    @Path("/oneAlarmXml")
//    @Path("/oneAlarmXml/{xmlRecords}")
////    @Consumes({"text/plain"})
//    @Produces({"text/plain"})
//    public String oneAlarmXml2(/*@PathParam("id") String id,*/@PathParam("xmlRecords") String xmlRecords/*,@PathParam("destino") String destino*/) throws Exception {
//        xmlRecords = xmlRecords.replace('|', '/');
//        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        InputSource is = new InputSource();
//        is.setCharacterStream(new StringReader(xmlRecords));
//
//        Document doc = db.parse(is);
//        NodeList nodes = doc.getElementsByTagName("alarma");
//        String respuesta = "";
//
//        String alarmaIdstring = "";
//        String destinostring = "";
//        String mensajestring = "";
//        int numeroReintentosstring = 0;
//        int tiempoReintentosstring = 0;
//        for (int i = 0; i < nodes.getLength(); i++) {
//            Element element = (Element) nodes.item(i);
//
//            NodeList alarmaId = element.getElementsByTagName("alarmaId");
//            Element line = (Element) alarmaId.item(0);
//            System.out.println("alarmaId: " + getCharacterDataFromElement(line));
//            alarmaIdstring = getCharacterDataFromElement(line);
//
//            NodeList destino = element.getElementsByTagName("destino");
//            line = (Element) destino.item(0);
////      String d=getCharacterDataFromElement(line);
//            System.out.println("destino: " + getCharacterDataFromElement(line));
//            destinostring = getCharacterDataFromElement(line);
//
//            NodeList mensaje = element.getElementsByTagName("mensaje");
//            line = (Element) mensaje.item(0);
//            System.out.println("mensaje: " + getCharacterDataFromElement(line));
//            mensajestring = getCharacterDataFromElement(line);
//
//            NodeList numeroReintentos = element.getElementsByTagName("numeroReintentos");
//            line = (Element) numeroReintentos.item(0);
//            System.out.println("numeroReintentos: " + getCharacterDataFromElement(line));
//            numeroReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));
//
//            NodeList tiempoReintentos = element.getElementsByTagName("tiempoReintentos");
//            line = (Element) tiempoReintentos.item(0);
//            System.out.println("tiempoReintentos: " + getCharacterDataFromElement(line));
//            tiempoReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));
//
////        OneCall oneCall = new OneCall();
//            TestTextToSpeech textToSpeech = new TestTextToSpeech(destinostring, mensajestring);
//
//            for (int j = 0; j <= numeroReintentosstring; j++) {
//                respuesta = textToSpeech.call();
////                        respuesta = oneCall.call("1010", "277");
//                if (respuesta.equals("Conexión establecida con el cliente")) {
//                    break;
//                }
//
//                Thread.sleep(tiempoReintentosstring * 3600);
//
//            }
//        }
//        respuesta = "Proceso terminado exitosamenteLa respuestaa la cadena es:" + respuesta;
//        return respuesta;
//    }
    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    @POST
    @Path("/groupCallXml")
//    @Path("/oneAlarmXml/{id}/{mensaje}/{destino}/{2}/{1}")
    @Consumes({"text/plain"})
    @Produces({"text/plain"})
    public String groupCallXml(/*@PathParam("id") String id,@PathParam("xmlRecords")*/String xmlRecords/*,@PathParam("destino") String destino*/) throws Exception {
        xmlRecords = xmlRecords.substring(2);
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("llamada");
        String respuesta = "";
        String respuestallam = "";

        String llamadaIdIdstring = "";
        String destinostring = "";
        String origenstring = "";
        String urlbdstring = "";
        int numeroReintentosstring = 0;
        int tiempoReintentosstring = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            NodeList llamadaId = element.getElementsByTagName("llamadaId");
            Element line = (Element) llamadaId.item(0);
            System.out.println("llamadaId: " + getCharacterDataFromElement(line));
            llamadaIdIdstring = getCharacterDataFromElement(line);

            NodeList urlbd = element.getElementsByTagName("urlbd");
            line = (Element) urlbd.item(0);
            System.out.println("urlbd: " + getCharacterDataFromElement(line));
            urlbdstring = getCharacterDataFromElement(line);

            NodeList destino = element.getElementsByTagName("destino");
            line = (Element) destino.item(0);
//      String d=getCharacterDataFromElement(line);
            System.out.println("destino: " + getCharacterDataFromElement(line));
            destinostring = getCharacterDataFromElement(line);

            NodeList origen = element.getElementsByTagName("origen");
            line = (Element) origen.item(0);
            System.out.println("origen: " + getCharacterDataFromElement(line));
            origenstring = getCharacterDataFromElement(line);

            NodeList numeroReintentos = element.getElementsByTagName("numeroReintentos");
            line = (Element) numeroReintentos.item(0);
            System.out.println("numeroReintentos: " + getCharacterDataFromElement(line));
            numeroReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));

            NodeList tiempoReintentos = element.getElementsByTagName("tiempoReintentos");
            line = (Element) tiempoReintentos.item(0);
            System.out.println("tiempoReintentos: " + getCharacterDataFromElement(line));
            tiempoReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));









            //        String respuesta = null;




            ConexionDataBase con = new ConexionDataBase(llamadaIdIdstring, origenstring, destinostring, numeroReintentosstring, tiempoReintentosstring);
            con.InsertarLlamadaDataBase();
//
            ConsultaIdSOS con2 = new ConsultaIdSOS(llamadaIdIdstring);
            String idsos = con2.consultar();

//        CapturaEventosDB capturaEventosDB = new CapturaEventosDB(extorigen, numteldestino, idllamada, idsos, numreintentos, "");
//        capturaEventosDB.start();



//        OneCall oneCall = new OneCall();

//        for (int i = 1; i <= numreintentos; i++) {
//            respuesta = oneCall.call(numteldestino, extorigen);
////                        respuesta = oneCall.call("1010", "277");
//            if (respuesta.equals("Conexión establecida con el cliente")) {
//                break;
//            }
//            System.out.println("SIGUIENTE REINTENTO");
//            Thread.sleep(tiemporeintento * 3600);
//
//        }

            String[] resp = null;

            int j = 0;
            for (j = 1; j <= numeroReintentosstring; j++) {

                AsteriskCallEventsStateProd call = new AsteriskCallEventsStateProd();
                call.setNumber(destinostring); //destino
                call.setMessage(origenstring);//origen

                long tiempoInicio = System.currentTimeMillis();
                System.out.println("El tiempo de inicio es :" + tiempoInicio + " miliseg");
                resp = call.originate();
                long tiempoFin = System.currentTimeMillis();
                System.out.println("El tiempo de fin es :" + tiempoFin + " miliseg");
                long totalTiempo = tiempoFin - tiempoInicio;

                if (resp[0].equals("1")) {
                    break;
                }

                if (resp[0].equals("4") && totalTiempo > 1000) {
                    resp[0] = "2";
                }

                System.out.println(resp);
                System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");
                System.out.println("SIGUIENTE REINTENTO");
                Thread.sleep(tiempoReintentosstring * 3600);

            }

            if (resp[0].equals("1") && resp[1].equals("0")) {
                resp[0] = "4";
            }

            if (!resp[0].equals("1")) {
                resp[1] = "0";
                resp[2] = "0";
                j = 0;//reintento contestado
            }

            if (resp[0].equals("1") && Integer.parseInt(resp[1]) < 10) {
                resp[0] = "5";
            }

            respuestallam = llamadaIdIdstring + "|" + resp[0] + "|" + resp[1] + "|" + idsos + "|" + j + "|" + resp[2] + "|";


            String encodeString = Base64.encodeToString(respuestallam.getBytes(), true);
            System.out.println("encodeToString " + encodeString);

//            String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//            String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

//            String httpcadena = "http://192.168.2.78/sifiv/llamadas_sistematizadas/respuesta_llamada.php?cadena=+" + encodeString;

            String httpcadena = urlbdstring + "=+" + encodeString;
            PeticionHttp post = new PeticionHttp(httpcadena);

            respuesta = post.getRespueta();
            System.out.println("La respuestaa la cadena: " + httpcadena + " es:" + respuesta);




//        return respuestallam;








        }

        respuestallam = "Proceso terminado exitosamente";

//return respuesta;
        return respuestallam;
    }

    @POST
    @Path("/oneAlarmXml1")
//    @Path("/oneAlarmXml/{id}/{mensaje}/{destino}/{2}/{1}")
    @Consumes({"text/plain"})
    @Produces({"text/plain"})
    public String oneAlarmXml1(/*@PathParam("id") String id,@PathParam("xmlRecords")*/String xmlRecords/*,@PathParam("destino") String destino*/) throws Exception {
        xmlRecords = xmlRecords.substring(2);
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("alarma");
        String respuesta = "";
        String respuestallam = "";

        String alarmaIdstring = "";
        String destinostring = "";
        String mensajestring = "";
        String urlbdstring = "";
        int numeroReintentosstring = 0;
        int tiempoReintentosstring = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            NodeList alarmaId = element.getElementsByTagName("alarmaId");
            Element line = (Element) alarmaId.item(0);
            System.out.println("alarmaId: " + getCharacterDataFromElement(line));
            alarmaIdstring = getCharacterDataFromElement(line);

            NodeList urlbd = element.getElementsByTagName("urlbd");
            line = (Element) urlbd.item(0);
            System.out.println("urlbd: " + getCharacterDataFromElement(line));
            urlbdstring = getCharacterDataFromElement(line);

            NodeList destino = element.getElementsByTagName("destino");
            line = (Element) destino.item(0);
//      String d=getCharacterDataFromElement(line);
            System.out.println("destino: " + getCharacterDataFromElement(line));
            destinostring = getCharacterDataFromElement(line);

            NodeList mensaje = element.getElementsByTagName("mensaje");
            line = (Element) mensaje.item(0);
            System.out.println("mensaje: " + getCharacterDataFromElement(line));
            mensajestring = getCharacterDataFromElement(line);

            NodeList numeroReintentos = element.getElementsByTagName("numeroReintentos");
            line = (Element) numeroReintentos.item(0);
            System.out.println("numeroReintentos: " + getCharacterDataFromElement(line));
            numeroReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));

            NodeList tiempoReintentos = element.getElementsByTagName("tiempoReintentos");
            line = (Element) tiempoReintentos.item(0);
            System.out.println("tiempoReintentos: " + getCharacterDataFromElement(line));
            tiempoReintentosstring = Integer.parseInt(getCharacterDataFromElement(line));

            ConexionDataBase con = new ConexionDataBase(alarmaIdstring, "11100", destinostring, numeroReintentosstring, tiempoReintentosstring);
            con.InsertarLlamadaDataBase();
//
            ConsultaIdSOS con2 = new ConsultaIdSOS(alarmaIdstring);
            String idsos = con2.consultar();

//        CapturaEventosDB capturaEventosDB = new CapturaEventosDB(extorigen, numteldestino, idllamada, idsos, numreintentos, "");
//        capturaEventosDB.start();



//        OneCall oneCall = new OneCall();

//        for (int i = 1; i <= numreintentos; i++) {
//            respuesta = oneCall.call(numteldestino, extorigen);
////                        respuesta = oneCall.call("1010", "277");
//            if (respuesta.equals("Conexión establecida con el cliente")) {
//                break;
//            }
//            System.out.println("SIGUIENTE REINTENTO");
//            Thread.sleep(tiemporeintento * 3600);
//
//        }

            String[] resp = null;

            int j = 0;
            for (j = 1; j <= numeroReintentosstring; j++) {
                AsteriskCallEventsStateProdAl call = new AsteriskCallEventsStateProdAl();
                call.setNumber(destinostring); //destino
                call.setMessage(mensajestring);//origen


                long tiempoInicio = System.currentTimeMillis();
                System.out.println("El tiempo de inicio es :" + tiempoInicio + " miliseg");
                resp = call.originate();
                long tiempoFin = System.currentTimeMillis();
                System.out.println("El tiempo de fin es :" + tiempoFin + " miliseg");
                long totalTiempo = tiempoFin - tiempoInicio;

//                resp = call.originate();






                if (resp[0].equals("1")) {
                    break;
                }



                if (resp[0].equals("4") && totalTiempo > 30000L) {
                    resp[0] = "2";
                }

                for (String resp1 : resp) {
                    System.out.println(resp1);
                }
                System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");




                System.out.println("SIGUIENTE REINTENTO");
                Thread.sleep(tiempoReintentosstring * 3600);

            }

            if (resp[0].equals("1") && resp[1].equals("0")) {
                resp[0] = "4";
            }

            if (!resp[0].equals("1")) {
                resp[1] = "0";
                resp[2] = "0";
                j = 0;//reintento contestado
            }

            if (resp[0].equals("1") && Integer.parseInt(resp[1]) < 10) {
                resp[0] = "5";
            }

            if (!alarmaIdstring.equals("0")) {


                respuestallam = alarmaIdstring + "|" + resp[0] + "|" + resp[1] + "|" + idsos + "|" + j + "|" + resp[2] + "|";


                String encodeString = Base64.encodeToString(respuestallam.getBytes(), true);
                System.out.println("encodeToString " + encodeString);

//            String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//            String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

//            String httpcadena = "http://192.168.2.78/sifiv/llamadas_sistematizadas/respuesta_llamada.php?cadena=+" + encodeString;

                String httpcadena = urlbdstring + "=+" + encodeString;


                PeticionHttp post = new PeticionHttp(httpcadena);

                respuesta = post.getRespueta();
                System.out.println("La respuestaa la cadena: " + httpcadena + " es:" + respuesta);
            }

        }

        respuesta = "Proceso terminado exitosamente";

        return respuesta;
    }
}
