/**
 * Adapter
 *
 * COMP 2150 SECTION A01
 * INSTRUCTOR    John Braico
 * ASSIGNMENT    Assignment #5, question #1
 * @author       Nurida Karimbaeva, 7851221
 * @version      01.08.2019
 *
 * REMARKS: The implementation of the interface, A5GUIAdapter.
 */
package comp2150.pos.client;

import comp2150.pos.server.A5POSServer;

import java.util.LinkedList;

public class MyAdapter implements A5GUIAdapter {
    private A5POSServer server;
    private LinkedList<A5GUIClient> clients;
    private LinkedList<Integer> clientIDs;
    // Constructor
    //
    // PARAMETERS:
    //    A5POSServer  a server to connect with
    //
    public MyAdapter(A5POSServer server){
        this.server=server;
        clientIDs = new LinkedList<>();
        clients = new LinkedList<>();
    }

    // Sends information about the server's state
    //
    // RETURNS:
    //    a String that holds the number of items in the inventory
    public String getInventoryCount() {
        return server.queryServer(A5POSServer.ServerQuery.INVENTORY_COUNT);
    }

    // Sends information about the server's state
    //
    // RETURNS:
    //    a String that holds the number of completed transactions
    public String getTransactionCompletedCount() {
        return server.queryServer(A5POSServer.ServerQuery.TRANSACTION_COMPLETED_COUNT);
    }

    // Sends information about the server's state
    //
    // RETURNS:
    //    a String that holds the number of transactions that are in progress
    public String getTransactionInProgressCount() {
        return server.queryServer(A5POSServer.ServerQuery.TRANSACTION_IN_PROGRESS_COUNT);
    }

    // Searches for the item in the inventory and generates a String array of the results
    //
    // PARAMETERS:
    //    pattern       a string that is a word we are looking for in the inventory
    //    order         a order in which the results should be presented
    //
    // RETURNS:
    //    An array of Strings that represents the list of search's results
    public String[] search(String pattern, String order) {
        String searchIterator=null;
        if (order.equals("COST")) {
            searchIterator=server.search(pattern, A5POSServer.ItemField.COST);
        }
        else if (order.equals("QUANTITY")) {
            searchIterator=server.search(pattern, A5POSServer.ItemField.QUANTITY);
        }
        else if (order.equals("CODE")) {
            searchIterator=server.search(pattern, A5POSServer.ItemField.CODE);
        }
        else if (order.equals("BACKORDER_QUANTITY")) {
            searchIterator=server.search(pattern, A5POSServer.ItemField.BACKORDER_QUANTITY);
        }
        else if (order.equals("DESCRIPTION")) {
            searchIterator=server.search(pattern, A5POSServer.ItemField.DESCRIPTION);
        }
        String search="";
        while(server.next(searchIterator)){
            search=search+server.queryMatch(searchIterator, A5POSServer.ItemField.CODE)+" ";
            search=search+server.queryMatch(searchIterator, A5POSServer.ItemField.DESCRIPTION)+" ";
            search=search+server.queryMatch(searchIterator, A5POSServer.ItemField.COST)+" ";
            search=search+server.queryMatch(searchIterator, A5POSServer.ItemField.QUANTITY)+" ";
            search=search+server.queryMatch(searchIterator, A5POSServer.ItemField.BACKORDER_QUANTITY)+",";
        }
        return search.split(",");
    }

    // Sends information about transaction
    //
    // PARAMETERS:
    //    transactionID       an id number of the transaction
    //
    // RETURNS:
    //    a String that holds all information about the transaction(type, item count, total quantity, total cost, whether it is complete or not)
    public String getTransactionDetails(String transactionID) {
        String result="";
        if(transactionID!=null) {
            result="Transaction ID: "+transactionID+"\n";
            result = result + "-Type: " + server.queryTransaction(transactionID, A5POSServer.TransactionQuery.TYPE) + "\n";
            result = result + "-Item Count: " + server.queryTransaction(transactionID, A5POSServer.TransactionQuery.ITEM_COUNT) + "\n";
            result = result + "-Total quantity of items: " + server.queryTransaction(transactionID, A5POSServer.TransactionQuery.TOTAL_QUANTITY) + "\n";
            result = result + "-Total cost of items: " + server.queryTransaction(transactionID, A5POSServer.TransactionQuery.TOTAL_COST) + "\n";
            result = result + "-Completion Status: " + server.queryTransaction(transactionID, A5POSServer.TransactionQuery.IS_COMPLETE) + "\n";
        }
        return result;
    }

    // Compares this clients id with the received one
    //
    // PARAMETERS:
    //    otherId       an id number of the client to compare with
    //
    // RETURNS:
    //    a boolean that is true if clients' ids are same and false if not
    public String getTransactionType(String transactionID) {
        String result="";
        if(transactionID!=null) {
            result=server.queryTransaction(transactionID, A5POSServer.TransactionQuery.TYPE);
        }
        return result;
    }

    // Sends information about transaction
    //
    // PARAMETERS:
    //    transactionID       an id number of the transaction
    //
    // RETURNS:
    //    a String that holds the number of different items in the transaction
    public String getTransactionItemCount(String transactionID) {
        String result="";
        if(transactionID!=null) {
            result=server.queryTransaction(transactionID, A5POSServer.TransactionQuery.ITEM_COUNT);
        }
        return result;    }

    // Sends information about transaction
    //
    // PARAMETERS:
    //    transactionID       an id number of the transaction
    //
    // RETURNS:
    //    a String that holds the total quantity of items in the transaction
    public String getTransactionQuantity(String transactionID) {
        String result="";
        if(transactionID!=null) {
            result=server.queryTransaction(transactionID, A5POSServer.TransactionQuery.TOTAL_QUANTITY);
        }
        return result;    }

    // Sends information about transaction
    //
    // PARAMETERS:
    //    transactionID       an id number of the transaction
    //
    // RETURNS:
    //    a String that holds the total cost of all items in the transaction
    public String getTransactionCost(String transactionID) {
        String result="";
        if(transactionID!=null) {
            result=server.queryTransaction(transactionID, A5POSServer.TransactionQuery.TOTAL_COST);
        }
        return result;    }

    // Creates a new transaction
    //
    // PARAMETERS:
    //    clientID       an id number of the client
    //    type           type of the transaction to create
    //
    // RETURNS:
    //    a String that holds the new transaction's ID on success, or null if there occurs an error
    public String createTransaction(int clientID, String type) {
        String newTransaction=null;
        int index=-1;
        for (int i=0;i<clientIDs.size();i++){
            if(clientIDs.get(i)==clientID){
                index=i;
            }
        }
        if(index!=-1) {
            long time = Long.parseLong(clients.get(index).getTimeStamp());
            if (type.equals("RETURN")) {
                newTransaction = server.createTransaction(A5POSServer.TransactionType.RETURN, time, clientID);
            }
            else if (type.equals("RESTOCK")) {
                newTransaction = server.createTransaction(A5POSServer.TransactionType.RESTOCK, time, clientID);
            }
            else if (type.equals("PURCHASE")) {
                newTransaction = server.createTransaction(A5POSServer.TransactionType.PURCHASE, time, clientID);
            }
            else if (type.equals("BACKORDER")) {
                newTransaction = server.createTransaction(A5POSServer.TransactionType.BACKORDER, time, clientID);
            }
            for (int i=0; i<clients.size();i++){
                clients.get(i).updateStatistics();
            }
        }
        else {
            newTransaction="Couldn't find client with such ID";
        }
        return newTransaction;
    }

    // Adds the certain amount of items to the transaction
    //
    // PARAMETERS:
    //    clientID       an id number of the client
    //    transactionID  an id of th transaction
    //    item           code(name) of the item to add
    //    quantity       number of items to add
    //
    // RETURNS:
    //    String that is null when the item was added successfully, or is an error message otherwise
    public String addToTransaction(int clientID, String transactionID, String item, String quantity) {
        String addItem=null;

        int index=-1;
        for (int i=0;i<clientIDs.size();i++){
            if(clientIDs.get(i)==clientID){
                index=i;
            }
        }

        if(transactionID!=null && item!=null && index!=-1){
            String[] split=item.split(" ");
            addItem=server.addItemToTransaction(transactionID,split[0],Integer.parseInt(quantity));
            clients.get(index).updateTransaction(transactionID);
        }

        if(addItem!=null){
            System.out.println(addItem);
        }
        return addItem;
    }

    // Ends Transaction the transaction, completes or cancels
    //
    // PARAMETERS:
    //    clientID       an id number of the client
    //    type           type of the transaction
    //    transactionID  id of the transaction to complete
    public void endTransaction(int clientID, String type, String transactionID) {
        //type  cancel || complete
        String endOfTransaction=null;

        int index=-1;
        for (int i=0;i<clientIDs.size();i++){
            if(clientIDs.get(i)==clientID){
                index=i;
            }
        }

        if(transactionID!=null && index!=-1){
            if (type.equals("COMPLETE")) {
                endOfTransaction=server.completeTransaction(transactionID);
            }
            else if (type.equals("CANCEL")) {
                long time = Long.parseLong(clients.get(index).getTimeStamp());
                endOfTransaction=server.cancelTransaction(transactionID, time, clientID);
            }
        }
        else {
            System.out.println("couldn't find client #"+clientID);
        }

        if(endOfTransaction!=null){
            System.out.println(endOfTransaction);
        }
        for(int i=0;i<clientIDs.size();i++) {
            clients.get(i).updateStatistics();
        }
    }

    // Adds a new client and his/her id to the lists
    //
    // PARAMETERS:
    //    clientID       an id number of the client
    //    client         instance of A5GUIClient
    public void newClient(int clientID, A5GUIClient client) {
        clientIDs.add(clientID);
        clients.add(client);
    }
}
