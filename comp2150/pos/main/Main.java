/**
 * Main
 *
 * COMP 2150 SECTION A01
 * INSTRUCTOR    John Braico
 * ASSIGNMENT    Assignment #5, question #1
 * @author       Nurida Karimbaeva, 7851221
 * @version      01.08.2019
 *
 * REMARKS: The main program for A5.
 */
package comp2150.pos.main;

import comp2150.pos.client.A5GUI;
import comp2150.pos.client.A5GUIClient;
import comp2150.pos.client.MyAdapter;
import comp2150.pos.server.A5Server;
import comp2150.pos.server.A5POSServer;

public class Main {
    private static A5POSServer server;

    public static void main(String[] args) {
        server = A5Server.getServer("a5inventory.txt");
        MyAdapter adapter=new MyAdapter(server);
        A5GUIClient client = A5GUI.getClient(adapter);
    }
}
