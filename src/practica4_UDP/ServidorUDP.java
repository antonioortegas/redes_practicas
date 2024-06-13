package practica4_UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class ServidorUDP {
    public static void main(String[] args) throws IOException{
        int puerto_Servidor = 2000;
        DatagramSocket s = new DatagramSocket(puerto_Servidor);

        byte [] buffer = new byte[100];

        DatagramPacket dp = new DatagramPacket(
            buffer, 
            buffer.length
            );

        while(true){
            try {
                s.receive(dp);

                String texto = new String(
                        dp.getData(),
                        dp.getOffset(),
                        dp.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("Mensaje " + texto + " (recibido desde: " + dp.getAddress() + ":" + dp.getPort() + ")");

                // get the first character of the message
                String firstChar = texto.substring(0, 1);
                //delete the first character from the message
                texto = texto.substring(1);
                //split the message into words
                String[] words = texto.split(" ");
                // only return the words that are longer than "firstChar" characters
                StringBuilder result = new StringBuilder();
                for (String word : words) {
                    if (word.length() > Integer.parseInt(firstChar)) {
                        result.append(word).append(" ");
                    }
                }
                // send the response to the client
                DatagramPacket reply = new DatagramPacket(
                        result.toString().getBytes(StandardCharsets.UTF_8),
                        result.toString().getBytes(StandardCharsets.UTF_8).length,
                        dp.getAddress(),
                        dp.getPort()
                );
                System.out.println("Enviando respuesta: " + result.toString());
                s.send(reply);
            } catch (IOException e) {
                break;
            }

        }
        s.close();
    }
}