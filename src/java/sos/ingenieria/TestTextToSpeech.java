/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;



/**
 *
 * @author luis
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class TestTextToSpeech
{
    private ManagerConnection managerConnection;
    String destino, mensaje;

    public TestTextToSpeech(String destino, String mensaje) throws IOException
    {
        this.destino=destino;
        this.mensaje=mensaje;
        ManagerConnectionFactory factory = new ManagerConnectionFactory
                ("localhost", "manager", "4u70d14l3rp455w0rd");
//        ("192.168.0.17", "manager", "4u70");

        this.managerConnection = factory.createManagerConnection();
    }

    public String call() 
    {
        OriginateAction originateAction;
        ManagerResponse originateResponse = null;

        originateAction = new OriginateAction();
        originateAction.setChannel("Local/" + destino + "@autodialer");
        originateAction.setContext("autodialer");
        originateAction.setExten("11100");
        originateAction.setPriority(new Integer(1));
        originateAction.setTimeout(new Long(30000));



        originateAction.setApplication("AGI");
//originateAction.setData("googletts.agi?text=hola");
        originateAction.setData("googletts.agi,\"" + mensaje + "\",es");

        
        
        

        String respuesta ="Conexión establecida con el cliente";
        
        try {
            try {
                // connect to Asterisk and log in
                managerConnection.login();
            } catch (IOException ex) {
                Logger.getLogger(TestTextToSpeech.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AuthenticationFailedException ex) {
                Logger.getLogger(TestTextToSpeech.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IllegalStateException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
//            return "No es posible loguearse en el servidor Asterisk";
        }
        try {
            try {
                // send the originate action and wait for a maximum of 30 seconds for Asterisk to send a reply
                originateResponse = managerConnection.sendAction(originateAction,500000);
            } catch (IOException ex) {
                Logger.getLogger(TestTextToSpeech.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (TimeoutException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
//            return respuesta= "Llamada no contestado";
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        // and finally log off and disconnect
        managerConnection.logoff();
        if(originateResponse.getMessage().equals("Originate failed")){
            respuesta = "No se pudo establecer conexión con el cliente";
        }
        
        return respuesta;
    }

    public static void main(String[] args) throws Exception
    {
        TestTextToSpeech textToSpeech;
        textToSpeech = new TestTextToSpeech("104","bienvenido a la nueva aplicacion usando asterisk y conversion de texto a voz, muchas gracias");
        
//        
//        TestTextToSpeech capturaEventosDB;
//        capturaEventosDB = new TestTextToSpeech("101","hola mundo");
//        
//        
//        capturaEventosDB.start();
        textToSpeech.call();
    }
}