/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.mysql;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koushik
 */
public class Iniciar {

    public static String BOOTSTRAP_CLASS_NAME;

    /**
     * The IP Address with port of the parent Server
     */
    public static String PARENT_SERVER_IP;
    public static String[] SAME_FEATHERS_IP;
    public static int NUMBER_OF_SAME_FEATHERS;
    public static String DB_HOST;
    public static String DB_USER;
    public static String DB_PASS;
    public static String DB_NAME;
    private boolean build_status;

    public Iniciar(String BOOTSTRAP_CLASS_NAME) {
        // Nothing to do here

        Iniciar.BOOTSTRAP_CLASS_NAME = BOOTSTRAP_CLASS_NAME;
    }

    public Iniciar parent(String PARENT_SERVER_IP) {
        Iniciar.PARENT_SERVER_IP = PARENT_SERVER_IP;
        return this;
    }// End of function parent

    public Iniciar mates(String SAME_FEATHER[]) {

        Iniciar.NUMBER_OF_SAME_FEATHERS = SAME_FEATHER.length;

        Iniciar.SAME_FEATHERS_IP = new String[NUMBER_OF_SAME_FEATHERS];

        System.arraycopy(SAME_FEATHER, 0, Iniciar.SAME_FEATHERS_IP, 0, Iniciar.NUMBER_OF_SAME_FEATHERS);

        return this;
    }// End of function parent

    public Iniciar database(String db_host, String db_user, String db_pass, String db_name) {
        Iniciar.DB_HOST = db_host;
        Iniciar.DB_USER = db_user;
        Iniciar.DB_PASS = db_pass;
        Iniciar.DB_NAME = db_name;
        return this;
    }

    public boolean buildServer() {

        System.out.println("Your server Configs are");
        try {
            Class.forName(BOOTSTRAP_CLASS_NAME);
            InetAddress address = InetAddress.getByName(PARENT_SERVER_IP);
            if (address.isReachable(2000)) {
                System.out.println("Parent Server is working Successfully");
            } else {
                throw new Exception("Request timed out!");
            }
            mysql my = new mysql(DB_HOST, DB_USER, DB_PASS, DB_NAME);
        } catch (ClassNotFoundException ex) {
            System.out.println("\u001B[31m" + "Your boot strap class is not Found! Make sure you entered class with package Name" + "\u001B[0m");
            return build_status;
        } catch (UnknownHostException ex) {
            System.out.println("Unknow Host for parent Address");
            return build_status;
        } catch (IOException ex) {
            System.out.println("Checkout your port for this server . Cannot ping to that server");
            return build_status;
        } catch (Exception ex) {
            System.out.println(ex);
            return build_status;
        }
        build_status = true;
        return build_status;
    }

    public static void initiate() {
        System.out.println("\u001B[33m" + "Server is now starting up ." + "\u001B[0m");
        Thread thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(1140);

                    while (true) {
                        Socket s = ss.accept();
                        Threads t = new Threads(s.getRemoteSocketAddress().toString(), s);
                        t.start();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Iniciar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
        System.out.println("Successfully Started Server. \"stop\" to Stop the server");
        
        Scanner lab = new Scanner(System.in);
        while(lab.next().equals("stop")){
            System.out.println("Good Bye");
            System.exit(0);
        }

    }// End of class initiate

    /**
     *
     * @return The string representation of the Server variables
     */
    @Override
    public String toString() {
        String var;

        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                var = f.toGenericString();
                var = var.substring(var.lastIndexOf(".") + 1);
                if (f.getType().isArray()) {
                    System.out.println(var + "-> " + Arrays.toString((Object[]) f.get(f)));
                } else {
                    System.out.println(var + "-> " + f.get(f));
                }

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Iniciar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}// End of class Iniciar
