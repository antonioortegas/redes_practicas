package practica5_TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientTCP {

    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int port = 12345;
        Socket socket = new Socket(ip, port);

        // Obtener flujos
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

        Scanner scanner = new Scanner(System.in);
        // Leer de teclado
        boolean valido = true;
        while (valido) {
            System.out.print("Introduzca el mensaje: ");
            String input = "";
            input = scanner.nextLine();
            System.out.println(input);

            // mientras que lo leído sea correcto
            // enviar mensaje
            // recibir respuesta

            // si el mensaje empieza por un numero
            if(Character.isDigit(input.charAt(0))){
                // enviar mensaje
                out.println(input);
                // recibir respuesta
            } else {
                valido = false;
                // enviar mensaje de FINISH
                out.println("FINISH");
                // recibir respuesta
            }
            String respuesta = in.readLine();
            System.out.println("Respuesta: " + respuesta);
        }
        // Finalizar y liberar recursos
        in.close();
        out.close();
        scanner.close();
        socket.close();

    }
}
