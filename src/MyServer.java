//Il programa ha il compito di definire un client ed un server che possono comunicare tra loro
//voglio che per ongi cliente che arriva il server risponde offrendo un servizio in base a quello che dice il client
//Suppongo che il Server converte in maiuscolo le frasi che comunica il client, la comunicazione si ferma quando il client invia "QUIT"


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class MyServer {
    ServerSocket socket;
    Socket client_socket; //Questo è il socket con cui vado a gestire i clienti
    private int port; //Metto la variabile porta come privata, in modo che quando uso il costruttore assegno il valore a tale variabile come argomento

    public static void main(String args[]){
    /*Metto la condizione con un if per cui nella riga di comando inserisco la porta del server*/
        if(args.length!=1){
            System.out.println("Per creare il Server usare la struttura: java MyServer <PORTA>");
            return;
        }
        /*Eseguendolo dal prompt dei comandi, io devo avere già l'oggetto creato nel main
        * in modo che quando avvio da prompt creo l'oggetto, io però ho dichiarato il costruttore con la porta come argomento, quindi
        * quindi metto nell'argomento il valore che ottengo dalla conversione della stringa che contiene args[0],
        * che è appunto il valore che inserisco da promt*/
        MyServer server = new MyServer(Integer.parseInt(args[0]));
        server.start(); //Avvio il metodo start e quindi le funzionalità del server quando avvio il programma da prompt
    }

    /*Nel costruttore metto come argomento la porta inserita dall'utente, faccio questo perchè
    il server ha senso se parte solo se ha una porta, quindi agisco sul costruttore, questo significa che
    posso costruire un MyServer solo se passo una porta al costruttore*/
    public MyServer(int port){
        System.out.println("Server inizializzato nella porta: "+port);
        this.port = port;
    }

    public void start(){
        /*Tale metodo ha il compito di creare il server socket dopo che inserisco la porta del server
        che voglio creare; per creare il server socket, devo avviare tale metodo, si noti come la variabile port è come
        se fosse già assegnata, perchè lo start() viene eseguito solo dopo che è stato creato l'oggetto, quindi
        quando si esegue il costruttore che come detto ha bisogno della porta che viene sfruttata dal metodo sstart() stesso.*/
        try {
            System.out.println("Avvio server all porta: "+port);
            socket = new ServerSocket(port);
            System.out.println("Server avviato correttamente alla porta: "+port);
            while(true) {
                /*Uso il while perchè per ogni client, dopo averlo servito, il socket server si chiude ed avvia la conversazione
                * con il client successivo, dunque nel while devo inserire cosa fa il server, cioè leggere i messaggi dal client
                * e convertirli in maiuscolo*/
                client_socket = socket.accept(); //Quando il server socket si sblocca con il client_socket gestisco la comunicazione con il client che ha sbloccato l'accept()
                System.out.println("Connessione accettata da: " + client_socket.getRemoteSocketAddress()); //Mi da l'indirizzo del client che ha sbloccato l'access
                /*Ora inserisco il comportamento del server, deve leggere i messaggi del client e inviarglieli in maiuscolo*/

                Scanner client_scanner = new Scanner(client_socket.getInputStream()); /*Sto dicendo allo scanner di leggere le cose che invia il client, quindi legge
                le cose nel socket creato dopo che si sblocca l'accept, tramite il quale il client spedisce le cose al server*/

                PrintWriter pw = new PrintWriter(client_socket.getOutputStream()); //Significa che scrivo sull'OutputStream fornito da getOutputStream fornito dal client_socket

                /*Dentro il while inserisco un altro while, in cui metto la condizione che mi finisce la comunicazione, il comportamento del server
                * quindi lettura del messaggio inviato dal client, ed invio del messaggio al client*/
                boolean go = true; //Questa è la condizione che fa ripetere il while fino a quando il client non invia QUIT
                while(go){
                    String messaggio_da_client = client_scanner.nextLine(); //Così leggo il messaggio inviato dal client con lo scanner e lo metto nella variabile messaggio_da_client
                    System.out.println("Messaggio rivecuto dal server: "+messaggio_da_client);

                    String messaggio_maiuscolo = messaggio_da_client.toUpperCase(); //Con toUpperCase() converto il messaggio ricevuto dal server in maiuscolo e lo salvo nella variabile messaggio_maiuscolo
                    System.out.println("Il Server sta inviando "+messaggio_maiuscolo+" a "+client_socket.getRemoteSocketAddress());
                    pw.println(messaggio_maiuscolo); //Cosi mando tramite il client_socket il messaggio convertito al client
                    pw.flush();

                    /*Devo mettere la condizione per cui se il client manda "QUIT" la comunicazione si interrompe,
                    * cioè esce da questo ciclo while, per farlo creo un if che fa si che quando messaggio_da_client è uguale a "QUIT"
                    * la variabile 'go' diventa false e quindi si esce dal ciclo, ed allora visto che finisce la conversazione con il client
                    * si chiuderà anche il client_socket che rimane aperto durante tutta la conversazione*/
                    if (messaggio_da_client.equals("QUIT")){
                        System.out.println("Chiusura connessione del Server con "+client_socket.getRemoteSocketAddress());
                        client_socket.close();
                        go = false;
                    }
                    /*Quando esco da questo while interno inizierà un nuovo ciclo del while esterno,
                    * quindi il Server è in ascolto per il prossimo client, infatti si rimetterà l'accept() e si aspetta
                    * il prossimo client con un altro dialogo*/
                }
            }

        } catch (IOException e) {
            System.out.println("Errore durante avvio server alla porta: "+port);
            e.printStackTrace();
        }
    }
}
