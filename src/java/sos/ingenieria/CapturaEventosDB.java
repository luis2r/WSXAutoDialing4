/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewStateEvent;

/**
 *
 * @author luis
 */
public class CapturaEventosDB extends Thread implements ManagerEventListener {

    private ManagerConnection managerConnection;
    private boolean noEvento = true;
//
    String origen;
    String destino;
    Date iniciollamada;
    Date finllamada;
    Date finring;
    Date inicioring;
    String idclave;
    String estadollamada = "0";
    String idsos = "0";
    String tiempotimbrado = "0";
    boolean hangup19 = false;
    int reintentocontestado = 0;
    boolean contestado = false;
    int numeroreintentos;
    int numerohangup = 0;
    int tamanoCadenaCanal=0 ;
    String canal="";

    public CapturaEventosDB(String origen, String destino, String idclave, String idsos, int numeroreintentos, String canal) throws IOException {
        this.canal=canal;
        this.tamanoCadenaCanal=this.canal.length();
        this.origen = origen;
        this.destino = destino;
        this.idclave = idclave;
        this.idsos = idsos;
        this.estadollamada = "2"; //////provisionel
        this.numeroreintentos = numeroreintentos;
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
                "localhost", "manager", "4u70d14l3rp455w0rd");

        this.managerConnection = factory.createManagerConnection();
    }

    public void run() {
        try {
            // register for events
            managerConnection.addEventListener(this);
            // connect to Asterisk and log in
            managerConnection.login();
            // request channel state
            managerConnection.sendAction(new StatusAction());

            try {
                while (noEvento) {

                    System.out.println(" got message: ");    ////borrar esta impresion

                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
            }

            Thread.sleep(2000);
            // wait 2 seconds for events to come in
            logoff();

            Thread.sleep(2000);

        } catch (InterruptedException e) {
        } catch (IllegalStateException ex) {
            Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthenticationFailedException ex) {
            Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void logoff() {
        managerConnection.logoff();
    }

    public void onManagerEvent(ManagerEvent event) {

        if (event instanceof ConnectEvent) {
            Date inicioConexion = event.getDateReceived();
            System.out.println("se hace ConnectEvent");
        } else if (event instanceof NewStateEvent) {
            System.out.println("se hace NewStateEvent");
            Date tiempoInicioEvento = event.getDateReceived();
            NewStateEvent e = (NewStateEvent) event;
            String callerIdNum = e.getCallerIdNum();
            String todoevent = e.toString();
            String state = e.getChannelStateDesc();
            System.out.println("Tiempo inicio evento NewStateEvent: " + tiempoInicioEvento + "\n"
                    + "Estado del canal: " + state + "\n"
                    + "Numero de quien llama: " + callerIdNum + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");

//            if (state.equals("Ringing")  && callerIdNum.equals(this.origen)) {
//
//                this.finring = tiempoInicioEvento;   ////no se usa este porque da un error de ejecucion se lo usa en bridgeevent
//            }
            if (state.equals("Ringing") && callerIdNum.equals(this.destino)) {
                this.reintentocontestado++;
                this.inicioring = tiempoInicioEvento;

            }


        } else if (event instanceof BridgeEvent) {
            System.out.println("se hace BridgeEvent");
            Date tiempoInicioEvento = event.getDateReceived();
            BridgeEvent e = (BridgeEvent) event;
            String bridgeState = e.getBridgeState();//.getCidCallingPresTxt();
            String callerId1 = e.getCallerId1();//.getCallerIdNum();
            String callerId2 = e.getCallerId2();//.getCallerIdName();
            boolean islink = e.isLink();
            boolean unlink = e.isUnlink();
            String todoevent = e.toString();
//            

// 
            System.out.println("Tiempo inicio evento BridgeEvent: " + tiempoInicioEvento + "\n"
                    + "bridgeState: " + bridgeState + "\n"
                    + "callerId1: " + callerId1 + "\n"
                    + "callerId2: " + callerId2 + "\n"
                    + "islink: " + islink + "\n"
                    + "unlink: " + unlink + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");


            if (unlink == true) {
                this.finllamada = tiempoInicioEvento;
            } else {
                this.iniciollamada = tiempoInicioEvento;
                this.estadollamada = "1";
                this.contestado = true;
                this.finring = tiempoInicioEvento;
            }


            if ((unlink == true) && ((this.destino.equals(callerId1)
                    && this.origen.equals(callerId2)) || (this.destino.equals(callerId2) && this.origen.equals(callerId1)))) {
                try {
                    //1-IDCLAVE2000, 2-ESTADOLLAMADA, 3-TIEMPOLLAMADASEG, 4-IDSOSINGENIERIA, 5-NUMREINTENTOCONTESTO, //6-NUMTIEMPOTIMBRADO
                    //                    String sign = "1|2|3|4|5|6";
                    long segundosiniciollamada = iniciollamada.getTime();
                    long segundosfinllamada = finllamada.getTime();
                    long segundosinicioring = inicioring.getTime();
                    long segundosfinring = finring.getTime();
                    String sign = this.idclave + "|" + this.estadollamada + "|" + (segundosfinllamada - segundosiniciollamada) / 1000
                            + "|" + this.idsos + "|" + this.reintentocontestado + "|" + (segundosfinring - segundosinicioring) / 1000/*this.tiempotimbrado*/;

                    String encodeString = Base64.encodeToString(sign.getBytes(), true);
                    System.out.println("encodeToString " + encodeString);

//                    String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

                    String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

                    PeticionHttp post = new PeticionHttp(httpcadena);

                    String respuesta = post.getRespueta();
                    System.out.println("La respuestaa la cadena: " + httpcadena + " es:" + respuesta);

                    this.noEvento = false;
                } catch (IOException ex) {
                    Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
                    this.noEvento = false;
                }

            }

        } else if (event instanceof HangupEvent) {
//            handleHangupEvent((HangupEvent)event);
            System.out.println("se hace hangup");
            Date tiempoInicioEvento = event.getDateReceived();
            HangupEvent e = (HangupEvent) event;
            String callerIdNum = e.getCallerIdNum();
            String causaHangupTxt = e.getCauseTxt();
            int causaHangup = e.getCause();
            String todoevent = e.toString();
            this.numerohangup++;


            System.out.println("Tiempo inicio evento HangupEvent: " + tiempoInicioEvento + "\n"
                    + "Numero de quien llama: " + callerIdNum + "\n"
                    + "causa de hangup txt: " + causaHangupTxt + "\n"
                    + "causa de hangup id: " + causaHangup + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");

            String sign = "";

            if (!contestado && (this.numeroreintentos == this.reintentocontestado) && (causaHangup == 19)) {//causaHangup==19 User alerting, no answer


                this.hangup19 = true;
                this.estadollamada = "2";


//                else{
//                    this.estadollamada = "3";
//                    this.noEvento = false;
//                }
                sign = this.idclave + "|" + this.estadollamada + "|" + 0 + "|" + this.idsos + "|" + 0 + "|" + 0/*this.tiempotimbrado*/;
//                managerConnection.logoff();
                this.noEvento = false;
                try {

                    String encodeString = Base64.encodeToString(sign.getBytes(), true);
                    System.out.println("encodeToString " + encodeString);

//                    String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

                    String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

                    PeticionHttp post = new PeticionHttp(httpcadena);

                    String respuesta = post.getRespueta();
                    System.out.println(respuesta);


                } catch (IOException ex) {
                    Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
                    this.noEvento = false;
                }

            }

        }


    }

    public static void main(String[] args) throws Exception {
        CapturaEventosDB capturaEventosDB = new CapturaEventosDB("101", "104", "idclave", "idsos", 1,"");
        capturaEventosDB.start();

    }
}
