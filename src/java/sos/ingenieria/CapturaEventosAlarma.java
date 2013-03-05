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
public class CapturaEventosAlarma extends Thread implements ManagerEventListener {

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

    public CapturaEventosAlarma(String origen, String destino, String idclave, String idsos, int numeroreintentos) throws IOException {

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
//            handleDialEvent((DialEvent)event);
            Date inicioConexion = event.getDateReceived();
//            System.out.println("La conexion se inicia en timpo: " + inicioConexion);
            System.out.println("se hace ConnectEvent");
        } else if (event instanceof NewStateEvent) {
//            handleNewStateEvent((NewStateEvent)event);
            System.out.println("se hace NewStateEvent");
            Date tiempoInicioEvento = event.getDateReceived();
            NewStateEvent e = (NewStateEvent) event;
            String callerIdNum = e.getCallerIdNum();
//            String callerIdName = e.getCallerIdName();
//            int channelState = e.getChannelState();
//            String uniqueId = e.getUniqueId();
            String todoevent = e.toString();
//            String state = e.getState();
            String state = e.getChannelStateDesc();
//            String channel = e.getChannel();



            System.out.println("Tiempo inicio evento NewStateEvent: " + tiempoInicioEvento + "\n"
//                    + "Estado del canal: " + channelState + "\n"
                    + "Estado del canal: " + state + "\n"
//                    + "Estado del canal: " + statedesc + "\n"
                    + "Numero de quien llama: " + callerIdNum + "\n"
//                    + "Nombre de quien llama: " + callerIdName + "\n"
//                    + //                                "Id de la acción: "+actionId+"\n"+
//                    "Id unico canal source: " + uniqueId + "\n"
//                    + "channel: " + channel + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");


            if (state.equals("Ringing") && callerIdNum.equals(this.destino)) {
                this.reintentocontestado++;
                this.inicioring = tiempoInicioEvento;
//                System.out.println("inicio ring "+this.inicioring);

            }

            if (state.equals("Ringing") && callerIdNum.equals(this.origen)) {

                this.finring = tiempoInicioEvento;
            }



        } else if (event instanceof BridgeEvent) {
//            handleDialEvent((DialEvent)event);
            System.out.println("se hace BridgeEvent");

            Date tiempoInicioEvento = event.getDateReceived();
            BridgeEvent e = (BridgeEvent) event;

            String bridgeState = e.getBridgeState();//.getCidCallingPresTxt();

            String callerId1 = e.getCallerId1();//.getCallerIdNum();
            String callerId2 = e.getCallerId2();//.getCallerIdName();

//            String channel1 = e.getChannel1();//.getUniqueId();
//            String channel2 = e.getChannel2();
//            String uniqueId1 = e.getUniqueId1();//.getChannel();
//            String uniqueId2 = e.getUniqueId2();
            boolean islink = e.isLink();
            boolean unlink = e.isUnlink();
            String todoevent = e.toString();
//            

// 
            System.out.println("Tiempo inicio evento BridgeEvent: " + tiempoInicioEvento + "\n"
                    + "bridgeState: " + bridgeState + "\n"
                    + "callerId1: " + callerId1 + "\n"
                    + "callerId2: " + callerId2 + "\n"
//                    + "channel1: " + channel1 + "\n"
//                    + "channel2: " + channel2 + "\n"
//                    + //                                "Id de la acción: "+actionId+"\n"+
//                    "uniqueId1: " + uniqueId1 + "\n"
//                    + "uniqueId2: " + uniqueId2 + "\n"
                    + "islink: " + islink + "\n"
                    + "unlink: " + unlink + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");


            if (unlink == true) {
                this.finllamada = tiempoInicioEvento;
            } else {
                this.iniciollamada = tiempoInicioEvento;
                this.estadollamada = "1";
                this.contestado = true;
//                this.finring=tiempoInicioEvento;
//                System.out.println("fin ring "+this.finring);
            }


            if ((unlink == true) && ((this.destino.equals(callerId1)
                    && this.origen.equals(callerId2)) || (this.destino.equals(callerId2) && this.origen.equals(callerId1)))) {
                try {
                    //1-IDCLAVE2000, 2-ESTADOLLAMADA, 3-TIEMPOLLAMADASEG, 4-IDSOSINGENIERIA, 5-NUMREINTENTOCONTESTO, //6-NUMTIEMPOTIMBRADO


                    //                    String sign = "1|2|3|4|5|6";

                    long segundosiniciollamada = iniciollamada.getTime();
//                            .getSeconds();
//                    int minutosinicio = iniciollamada.getMinutes();
//                    int horasinicio = iniciollamada.getHours();

//                    int segundosiniciollamada = segundosinicio + (minutosinicio * 60) + horasinicio * 60 * 60;

                    long segundosfinllamada = finllamada.getTime();
//                            getSeconds();
//                    int minutosfin = finllamada.getMinutes();
//                    int horasfin = finllamada.getHours();

//                    int segundosfinllamada = segundosfin + (minutosfin * 60) + (horasfin * 60 * 60);


                    long segundosinicioring = inicioring.getTime();
//                            getSeconds();
//                    int minutosinicioring = inicioring.getMinutes();
//                    int horasinicioring = inicioring.getHours();

//                    int totsegundosinicioring = segundosinicioring + (minutosinicioring * 60) + horasinicioring * 60 * 60;

                    long segundosfinring = finring.getTime();
//                            .getSeconds();
//                    int minutosfinring = finring.getMinutes();
//                    int horasfinring = finring.getHours();

//                    int totsegundosfinring = segundosfinring + (minutosfinring * 60) + (horasfinring * 60 * 60);

                    String sign = this.idclave + "|" + this.estadollamada + "|" + (segundosfinllamada - segundosiniciollamada)
                            + "|" + this.idsos + "|" + this.reintentocontestado + "|" + (segundosfinring - segundosinicioring)/*this.tiempotimbrado*/;


                    String encodeString = Base64.encodeToString(sign.getBytes(), true);
                    System.out.println("encodeToString " + encodeString);

                    String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//                    String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

                    PeticionHttp post = new PeticionHttp(httpcadena);

                    String respuesta = post.getRespueta();
                    System.out.println(respuesta);



                    this.noEvento = false;
                } catch (IOException ex) {
                    Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else if (event instanceof HangupEvent) {
//            handleHangupEvent((HangupEvent)event);
            System.out.println("se hace hangup");
            Date tiempoInicioEvento = event.getDateReceived();
            HangupEvent e = (HangupEvent) event;
            String callerIdNum = e.getCallerIdNum();
//            String callerIdName = e.getCallerIdName();
            String causaHangupTxt = e.getCauseTxt();
            int causaHangup = e.getCause();
//            String uniqueId = e.getUniqueId();
            String todoevent = e.toString();
//            String chanel = e.getChannel();

            System.out.println("Tiempo inicio evento HangupEvent: " + tiempoInicioEvento + "\n"
                    + "Numero de quien llama: " + callerIdNum + "\n"
//                    + "Nombre de quien llama: " + callerIdName + "\n"
                    + "causa de hangup txt: " + causaHangupTxt + "\n"
                    + "causa de hangup id: " + causaHangup + "\n"
//                    + "channel: " + chanel + "\n"
                    //                                "Id de la acción: "+actionId+"\n"+
//                    + "Id unico source: " + uniqueId + "\n"
                    + "Todos los datos de la llmada: " + todoevent + "\n" + "\n");

            String sign = "";

            if (!contestado && causaHangup == 0 && !this.hangup19) {
                sign = this.idclave + "|" + 3 + "|" + 0 + "|" + this.idsos + "|" + 0 + "|" + 0/*this.tiempotimbrado*/;
                managerConnection.logoff();
                try {

                    String encodeString = Base64.encodeToString(sign.getBytes(), true);
                    System.out.println("encodeToString " + encodeString);

                    String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//                    String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

                    PeticionHttp post = new PeticionHttp(httpcadena);

                    String respuesta = post.getRespueta();
                    System.out.println(respuesta);


                    this.noEvento = false;
                } catch (IOException ex) {
                    Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (!contestado && (this.numeroreintentos == this.reintentocontestado)) {//causaHangup==19 User alerting, no answer

                if (causaHangup == 19) {
                    this.hangup19 = true;
                    sign = this.idclave + "|" + this.estadollamada + "|" + 0 + "|" + this.idsos + "|" + 0 + "|" + 0/*this.tiempotimbrado*/;
                    managerConnection.logoff();
                }

                try {

                    String encodeString = Base64.encodeToString(sign.getBytes(), true);
                    System.out.println("encodeToString " + encodeString);

                    String httpcadena = " http://localhost:8080/GetSomeRest/service/update/" + encodeString;

//                    String httpcadena = "http://192.168.2.78/sifiv/crm/respuesta_llamada.php?cadena=+" + encodeString;

                    PeticionHttp post = new PeticionHttp(httpcadena);

                    String respuesta = post.getRespueta();
                    System.out.println(respuesta);



                    this.noEvento = false;
                } catch (IOException ex) {
                    Logger.getLogger(CapturaEventosDB.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }


    }

    public static void main(String[] args) throws Exception {
        CapturaEventosAlarma capturaEventosAlarma = new CapturaEventosAlarma("101", "104", "idclave", "idsos", 1);
        capturaEventosAlarma.start();

    }
}
