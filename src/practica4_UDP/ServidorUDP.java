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

        s.receive(dp);

        String texto = new String(
            dp.getData(), 
            dp.getOffset(),
            dp.getLength(),
            StandardCharsets.UTF_8
        );

        System.out.println("Mensaje " + texto + " (recibido desde: " + dp.getAddress() + ":" + dp.getPort() + ")");
    }
}