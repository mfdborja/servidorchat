package servidorchatv2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Borjaro2000
 */
public class Main {

    public static void main(String[] args) {
        //Variables
        int puertoEschucha = 65000; //Puerto en el que se establecerá el socket de escucha.
        ServerSocket conectorEscucha = null; //Sockect donde escucharemos las nuevas conexiones.
        List<Socket> listaClientes=new ArrayList<Socket>(); //Lista de los clientes conectados.

        //Iniciar la ventana de información
        VentanaAdministracion ventana=new VentanaAdministracion();
        ventana.setVisible(true);

        //NombrePrograma [puerto]
        if (args.length > 0) {
            try {
                puertoEschucha = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                ventana.incluirError("Formato de puerto no válido.");
            }
        }

        //Iniciar la lectura y envío de mensajes
        //Objeto que escucha a los clientes y les envía la conversación
        Mensajeria lectenv=Mensajeria.iniciar(listaClientes, ventana);
        lectenv.start(); //Iniciamos en un nuevo hilo
        
        //Arrancar el socket de escucha
        //Los clientes llamarán a aquí
        try {
            conectorEscucha = new ServerSocket(puertoEschucha);
        } catch (IOException ex) {
            ventana.incluirError("Imposible realizar la escucha: " + ex.getMessage());
        }

        //Si ha salido bien la conexión
        if (conectorEscucha != null) {
            //Escucha continua de usuario
            ventana.incluirError("Servidor iniciado en el puerto " + puertoEschucha + ".");
            ventana.incluirError("Esperando clientes.");

            //Conexión que nos sirve para manejar temporalmente las nuevas conexiones
            //Que añadiremos a la lista de clientes
            Socket temp;

            //Bucle infinito que hará que podamos escuchar por siempre a clientes nuevos.
            while (true) {
                try {
                    //Acepta las nuevas conexiones
                    temp = conectorEscucha.accept(); //Si no hay cliente nuevo se espera
                    boolean existe=false;
                    for(int i=0;i<listaClientes.size() && !existe;i++){
                        existe=listaClientes.get(i).equals(temp);
                    }
                    if(existe){ //Miramos que no esté ya en la lista y si está lo quitamos
                        listaClientes.remove(temp);
                    }

                    //Añade el cliente a la lista
                    listaClientes.add(temp);
                    ventana.incluirError("Nuevo cliente añadido: "+temp.getInetAddress().getHostName());
                    ventana.incluirError("Clientes en la lista: "+listaClientes.size());
                } catch (IOException ex) {
                    ventana.incluirError("No se puede conectar con un cliente: "+ex.getMessage()+".");
                }
            }
        }
    }
}
