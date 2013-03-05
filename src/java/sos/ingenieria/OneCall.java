/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
/**
 *
 * @author luis
 */

 
public class OneCall extends Thread{
 
    private ManagerConnection managerConnection;
 
    public OneCall() throws IOException {
        
        ManagerConnectionFactory factory = new ManagerConnectionFactory("localhost", "manager", "4u70d14l3rp455w0rd");
 
        this.managerConnection = factory.createManagerConnection();
    }
    
    public OneCall(String host, String cuenta, String password) throws IOException {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(host, cuenta, password);
 
        this.managerConnection = factory.createManagerConnection();
    }
 
    public String call(final String user, final String phoneNumber) throws IOException, AuthenticationFailedException {
        OriginateAction originateAction;
        ManagerResponse originateResponse = null;
 
        originateAction = new OriginateAction();
        originateAction.setChannel("Local/" + user + "@autodialer");
        originateAction.setContext("autodialer");
        originateAction.setExten(phoneNumber);
        originateAction.setPriority(new Integer(1));
        originateAction.setTimeout(new Long(40000));
        originateAction.setVariable("customernum", phoneNumber);
//        originateAction.setCallingPres(MIN_PRIORITY);
        
        
        String respuesta ="Conexión establecida con el cliente";
        
        try {
            // connect to Asterisk and log in
            managerConnection.login();
        } catch (IllegalStateException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una exception en oneCall: IllegalStateException managerConnection");
        } catch (TimeoutException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una exception en oneCall: TimeoutException managerConnection");
            return "No es posible loguearse en el servidor Asterisk";
        }
        try {
            // send the originate action and wait for a maximum of 30 seconds for Asterisk to send a reply
            originateResponse = managerConnection.sendAction(originateAction,500000);
        } catch (TimeoutException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una exception en oneCall: TimeoutException originateResponse");
            return respuesta= "Llamada no contestado";
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una exception en oneCall: IllegalArgumentException originateResponse");
        } catch (IllegalStateException ex) {
            Logger.getLogger(OneCall.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una exception en oneCall: IllegalStateException originateResponse");
        }
 
        // and finally log off and disconnect
        if(originateResponse.getMessage().equals("Originate failed")){
            respuesta = "No se pudo establecer conexión con el cliente";
        }
        managerConnection.logoff();
        return respuesta;
    }
 
    public static void main(String[] args) throws Exception {
        
//        CapturaEventosDB capturaEventosDB = new CapturaEventosDB("101","104");
//        capturaEventosDB.start();
//        
//        CapturaEventosDB capturaEventosDB2 = new CapturaEventosDB("102","104");
//        capturaEventosDB2.start();
        OneCall oneCall = new OneCall();
        String res=oneCall.call("101", "104");
        System.out.println(res);
    }
}