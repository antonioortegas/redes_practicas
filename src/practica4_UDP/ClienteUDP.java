package practica4_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        DatagramSocket s = new DatagramSocket();
        System.out.println("Puerto asignado: " + s.getLocalPort());
        boolean continueLoop = true;
        Scanner sc = new Scanner(System.in);

        while(continueLoop){
            // Ask the user for the message to send
            System.out.println("Escribe el mensaje a enviar: ");
            String texto = sc.nextLine();

            // if message does not start with a digit, end the loop
            if(Character.isDigit(texto.charAt(0))){
                DatagramPacket dp = new DatagramPacket(
                        texto.getBytes(StandardCharsets.UTF_8), // Datos
                        texto.getBytes(StandardCharsets.UTF_8).length, // Longitud de los datos
                        InetAddress.getByName("127.0.0.1"), // IP del servidor
                        2000 // puerto del servidor
                );

                s.send(dp);
                System.out.println("Texto enviado con éxito");
                DatagramPacket reply = new DatagramPacket(new byte[512], 512);
                s.receive(reply);
                System.out.println("Respuesta del servidor: " + new String(reply.getData(), reply.getOffset(), reply.getLength(), StandardCharsets.UTF_8));
            } else {
                continueLoop = false;
                System.out.println("El mensaje no empieza con un número, terminando el programa");
            }
        }

        s.close();
        sc.close();
        
    }
}