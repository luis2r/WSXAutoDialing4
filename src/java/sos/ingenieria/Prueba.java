/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import sun.misc.BASE64Encoder;
//import sun.misc.BASE64Encoder;
/**
 *
 * @author luis
 */

public class Prueba {

public static void main(String[] args) throws IOException {
//1-IDCLAVE2000, 2-ESTADOLLAMADA, 3-TIEMPOLLAMADASEG, 4-IDSOSINGENIERIA, 5-NUMREINTENTOCONTESTO, //6-NUMTIEMPOTIMBRADO
String sign = "1|2|3|4|5|6";
//            BASE64Encoder encoder = new BASE64Encoder();
//            String encodeString = encoder.encodeBuffer(sign.getBytes()); 
    
    
                          

                    String encodeString = Base64.encodeToString(sign.getBytes(), true);
                    System.out.println("encodeToString " + encodeString);

                    String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//                    String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

                    PeticionHttp post = new PeticionHttp(httpcadena);

                    String respuesta = post.getRespueta();
                    System.out.println(respuesta);


          
            
            
//    BASE64Encoder encoder = new BASE64Encoder();
//    String encodeString= encoder.encodeBuffer(sign.getBytes());
  

//String httpcadena="http://localhost:8080/WSXAutoDialing2/webresources/sosingenieria.llamada/1?";

//String httpcadena="http://localhost:8080/WSXAutoDialing2/webresources/sosingenieria.llamada/countp ";
//String httpcadena="http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+"+encodeString;

//PeticionHttp post = new PeticionHttp (httpcadena);

//String respuesta = post.getRespueta();
//System.out.println(respuesta);
}

}







//            if (callerIdNum.equals(this.destino)/*||callerIdNum.equals(this.destino)*/) {
////                System.out.println("http://localhost:8080/GetSomeRest/service"+callerIdNum);
////                String httpcadena = "http://localhost:8080/GetSomeRest/service/update/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//        String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;
//                PeticionHttp post;
//                try {
//                    post = new PeticionHttp(httpcadena);
//                    try {
//                        String respuesta = post.getRespueta();
//                    } catch (IOException ex) {
//                        Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
