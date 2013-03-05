/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos.ingenieria;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

/**
 *
 * @author luis
 */
public class EnvioOneAlarmaReintentos implements Runnable {

    String resp[] = null;
    String destinostring;
    String mensajestring;
    int numeroReintentosstring;
    int tiempoReintentosstring;
AsteriskCallEventsStateProdAl call;
    public EnvioOneAlarmaReintentos(String destinostring, String mensajestring, int numeroReintentosstring, int tiempoReintentosstring) throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
        this.destinostring = destinostring;
        this.mensajestring = mensajestring;
        this.numeroReintentosstring = numeroReintentosstring;
        this.tiempoReintentosstring = tiempoReintentosstring;
//        iniciarAlarma();
    }

    public void iniciarAlarma() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
        int j = 0;
        for (j = 1; j <= this.numeroReintentosstring; j++) {
             this.call= new AsteriskCallEventsStateProdAl();
            call.setNumber(this.destinostring); //destino
            call.setMessage(this.mensajestring);//origen
            this.resp = call.originate();
            if (this.resp[0].equals("1")) {
                break;
            }
            System.out.println(this.resp);

            System.out.println("SIGUIENTE REINTENTO");
            Thread.sleep(this.tiempoReintentosstring * 3600);

        }
//        return this.resp;

    }
    
        @Override
    public void run() {

        try {

//            String[] iniciarAlarma = 
            iniciarAlarma();


        } catch (IOException ex) {
            Logger.getLogger(EnvioOneAlarmaReintentos.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (AuthenticationFailedException ex) {
            Logger.getLogger(EnvioOneAlarmaReintentos.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(EnvioOneAlarmaReintentos.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(EnvioOneAlarmaReintentos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
//        throw new UnsupportedOperationException("Not supported yet.");

    }

    public static void main(String[] args) throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {

        EnvioOneAlarmaReintentos call = new EnvioOneAlarmaReintentos("104", "hola asterisk", 2, 1);
        EnvioOneAlarmaReintentos call2 = new EnvioOneAlarmaReintentos("101", "hola asterisk esto es un nuevoa mensje", 2, 1);
//        call.run();
//        call2.run();
//        String[] resp = call.iniciarAlarma();
//        String[] resp2 = call2.iniciarAlarma();
//        System.out.println(resp);
//        System.out.println(resp2);
        // create and name each runnable
//        PrintTask task1 = new PrintTask("task1");
//        PrintTask task2 = new PrintTask("task2");
//        PrintTask task3 = new PrintTask("task3");
        System.out.println("Starting Executor");
// create ExecutorService to manage threads
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
// start threads and place in
        threadExecutor.execute(call); // start task1

        threadExecutor.execute(call2); // start task2

//        threadExecutor.execute(task3    

//        runnable state); // start task3





// shut down worker threads when their tasks complete
        threadExecutor.shutdown();

        System.out.println(
                "Tasks started, main ends.\n");

    }


}
