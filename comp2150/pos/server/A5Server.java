/**
 * A5Server.java
 *
 * @author       Nurida Karimbaeva, 7851221
 *
 * REMARKS: The Singleton class that helps to create the one same server for every client.
 */
package comp2150.pos.server;

public class A5Server {
    private static A5POSServer server;

    //Constructor for the A5Server
    //
    // PARAMETERS:
    //    name       String variable that holds the name of the .txt file, which stores the information about servers inventory
    //
    private A5Server(String name){
        server=new TheNameOfMyPOSServer(name);
    }

    // Initializes the server if it is null, otherwise returns already existed server
    //
    // PARAMETERS:
    //    name       String variable that holds the name of the .txt file, which stores the information about servers inventory
    //
    // RETURNS:
    //     A4POSServer
    public static A5POSServer getServer(String name){
        if (server==null){
            new A5Server(name);
        }
        return server;
    }
}
