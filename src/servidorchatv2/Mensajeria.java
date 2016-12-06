package servidorchatv2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Mensajeria extends Thread {

    private static Mensajeria instancia = null; //Para el singleton (solo una instancia del objeto)
    VentanaAdministracion ventana; //Puntero a la ventana para escribir
    private List<Socket> listaClientes = new ArrayList<Socket>(); //La lista de clientes del main
    String temp = ""; //Variable temporal para el mensaje que se recibe

    //Este método sirve para que solo se pueda iniciar una vez esta clase
    public static Mensajeria iniciar(List<Socket> listaClientes, VentanaAdministracion ventana) {
        if (instancia == null) {
            instancia = new Mensajeria(listaClientes, ventana);
        }

        return instancia;
    }

    //No se puede instanciar la clase desde fuera con un new Mensajeria(), solo con el método anterior
    private Mensajeria(List<Socket> listaClientes, VentanaAdministracion ventana) {
        this.listaClientes = listaClientes;
        this.ventana = ventana;
    }

    //Método Run del hilo, nos permite realizar varias tareas en paralelo
    @Override
    public void run() {
        //Temporizador
        javax.swing.Timer reloj = new javax.swing.Timer(200, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });


        while (true) //Lee indefinidamente a todos los clientes
        {
            //Miramos cliente por cliente si ha escrito algo
            for (int i = 0; i < listaClientes.size(); i++) {
                reloj.start();
                try {
                    //Sacamos lo que ha escrito un cliente, si lo hay.
                    if (listaClientes.get(i).getInputStream().available() > 0) {
                        temp = new DataInputStream(listaClientes.get(i).getInputStream()).readUTF();
                    } else {
                        temp = "";
                    }
                } catch (IOException ex) {
                    ventana.incluirError("No se puede leer el mensaje de: " + listaClientes.get(i).getInetAddress().getHostName());
                    //Si no podemos comunicar, quitamos al cliente de la lista
                    listaClientes.remove(i);
                    ventana.incluirError("Eliminado cliente");
                }

                //Y si ha escrito algo, se lo mandamos a todos
                if (!temp.isEmpty()) {
                    ventana.incluirMensaje(temp);
                    //Si encuentra algo nuevo escrito en un sockect lo envía a todos, en este caso cadenas de texto.
                    DataOutputStream salida; //Salida del socket para cada cliente
                    for (int j = 0; j < listaClientes.size(); j++) {
                        try {
                            salida = new DataOutputStream(listaClientes.get(j).getOutputStream());
                            //Escribe en el buffer del socket del cliente
                            salida.writeUTF(temp);
                            System.out.println("A: " + listaClientes.get(j).getInetAddress().getHostAddress());
                        } catch (IOException ex) {
                            ventana.incluirError("No se puede escribir en: " + listaClientes.get(j).getInetAddress().getHostName());
                            listaClientes.remove(j);
                            ventana.incluirError("Eliminado cliente");
                        }
                    }
                    System.gc(); //Se limpia la memoria.
                }
            }
        }
    }
}
