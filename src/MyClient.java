import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    Socket socket;
    private String indirizzo;
    private int porta;

    public static void main(String args[]){
        MyClient client = new MyClient(args[0], Integer.parseInt(args[1]));
        client.start();
    }

    /*Siccome quando creo il client devo comunicare l'indirizzo del server a cui collegarsi,
     * allora come variabili private ho l'IP e la porta che definiscono l'indirizzo del server a cui il client si deve collegare,
     * questi valori li inserisco da prompt e sono rispettivamente contenuti in args[0](c'è l'IP) e args[1](c'è la porta).
     * Quando creo il client, questo deve avere l'indirizzo che voglio io, dunque come argomento del costruttore metto proprio l'indirizzo del server
     * che vado ad inserire manualmente, infatti nel main vedo che quando creol'oggetto MyClient metto come argomenti args[0] e args[1],
     * i cui valori saranno passati alle variabili private 'indirizzo' e 'porta', questo avviene tramite i comandi inseriti nel costruttore*/
    public MyClient(String indirizzo, int porta){
        this.indirizzo = indirizzo;
        this.porta = porta;
    }

    public void start(){
        /*Tale metodo definisce il comportamento del client, la connessione con il server e cosa dice a lui,
        * come il messaggio che si vuoleconvertire */
        System.out.println("Il Client si sta connettendo a "+indirizzo+":"+porta);

        /*Ora vado a creare la connessione con il server, dunque creo il socket del client, indicando come argomento
        * l'indirizzo del server a cui di deve connettere il client*/
        try {
            socket = new Socket(indirizzo, porta); //I termini 'indirizzo' e 'porta' hanno sicuramente  un valore, grazie al costruttore definito precedentemente
            System.out.println("Connessione avviata del Client con "+indirizzo+":"+porta);

            /*Ora che abbiamo creato il socket con cui il client comunica al server, come detto il client invia al server le stringhe
            * che gli dice l'utente, per poi ricevere dal server la conversione in maiuscolo del messaggio e mostrarla, allora deduco che sia
            * un ciclo while in cui il client chiede le stringhe da inviare al server.
            * Dal punto di vista delgli Stream, abbiamo bisogno dello Stream che invia il messaggio comunicato dall'untente al server, dunque un OutputStream*/
            PrintWriter pw = new PrintWriter(socket.getOutputStream());//Così invio il messaggio che dice l'untente al clint verso il server, sfruttando il socket del client stesso

            Scanner scanner = new Scanner(socket.getInputStream());//Così leggo le cose che il client riceve dal server

            Scanner user_scanner = new Scanner(System.in); //Così il client legge quello che l'utente scrive con la tastiera, per mandarlo poi al server

            /*Ora devodefinire il ciclo while in cui si trova tutto il comportamento del client, compresa anche l'interruzione
            * della comunicazione quando si scrive "QUIT", avrò quindi tutti i comportamenti del client compreso
            * il chiedere e scannerizzare la stringa da convertire all'utente, la scennerizzo per poi inviarla al server
            * tramite il PrintWriter, e con il primo scanner creato precedentemente leggo quello che il client riceve dal server */

            boolean go = true;
            while(go){
                System.out.println("Cosa vuoi maiuscolizzare:");
                String messaggio_da_utente = user_scanner.nextLine();//Il client legge la stringa da inviare al server e la salva su una variabile messaggio_da_utente
                pw.println(messaggio_da_utente);//Il client manda con PrintWriter il messaggio al server
                pw.flush();
                System.out.println("E' stato mandato il seguente messaggio: "+messaggio_da_utente);

                /*Dopo che ho mandato il messaggio il client riceve il messaggio convertito e lo deve leggere, per cui utilizzo il primo scanner creato precedentemente*/
                String messaggio_convertito_da_server = scanner.nextLine();
                System.out.println("Il messaggio ricevuto è: "+messaggio_convertito_da_server);

                /*Quando voglio finire la comunicazione devo scrivere "QUIT" che non viene spedito al server
                * quindi posso inserire la condizione per cui quando il client riceve "QUIT" dall'utente  allora la variabile 'go' diventa false
                * e si esce dal ciclo while ed il client sarà alla ricerca di un altro server a cui collegarsi*/
                if (messaggio_da_utente.equals("QUIT")){
                    System.out.println("Interruzione connessione con: "+socket.getRemoteSocketAddress());
                    go = false;

                }


            }


        } catch (IOException e) {
            System.out.println("Errore connessione Client");
            e.printStackTrace();
        }

    }
}
