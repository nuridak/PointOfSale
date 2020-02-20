/**
 * TheNameOfMyPOSServer
 *
 * COMP 2150 SECTION A01
 * INSTRUCTOR    John Braico
 * ASSIGNMENT    Assignment #5, question #1
 * @author       Nurida Karimbaeva, 7851221
 * @version      01.08.2019
 *
 * REMARKS: The implementation of the interface, A5POSServer.
 */
package comp2150.pos.server;
import java.io.*;
import java.util.*;

public class TheNameOfMyPOSServer implements A5POSServer{
    private LinkedList<Item> inventoryList;
    private LinkedList<Transaction> transactionList;
    private LinkedList<LinkedList<Item>> searchLists;
    private int searchesNumber=0;
    private LinkedList<ListIterator<Item>> iterators;
    private LinkedList<String> iteratorsIDs;
    private int iteratorIDdigit=0;
    private LinkedList<Item> tempItems;
    private LinkedList<Boolean> calledNext;
    private LinkedList<Boolean> hasNext;

    // Constructor
    //
    // PARAMETERS:
    //    filename  String variable that holds the name of the .txt file, which stores the information about servers inventory
    //
    public TheNameOfMyPOSServer(String filename) {
        //create a linked list of the items available for sale
        inventoryList =new LinkedList<Item>();

        String name=new File(filename).getAbsolutePath();
        Scanner inventory;
        //create inventory list
        try {
            inventory = new Scanner(new File(name));
            while(inventory.hasNextLine()) {
                String line=inventory.nextLine();
                String[] info=line.split(",");
                if(inventoryList.size()==0) {
                    inventoryList.add(new Item(info[0], info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3])));
                }
                else if(inventoryList.size()==1){
                    if(inventoryList.get(0).getCode().compareTo(info[0])<0){
                        inventoryList.addLast( new Item(info[0], info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3])));
                    }
                    else{
                        inventoryList.addFirst(new Item(info[0], info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3])));
                    }
                }
                else{
                    int j=0;
                    while(j< inventoryList.size() && inventoryList.get(j).getCode().compareTo(info[0])<0){
                        j++;
                    }
                    if(j== inventoryList.size()){
                        inventoryList.addLast( new Item(info[0], info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3])));
                    }
                    else {
                        inventoryList.add(j, new Item(info[0], info[1], Integer.parseInt(info[2]), Integer.parseInt(info[3])));
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("FileNotFoundException");
        }
        //create a list of transactions
        transactionList =new LinkedList<Transaction>();
        searchLists = new LinkedList<LinkedList<Item>>();
        iterators = new LinkedList<ListIterator<Item>>();
        iteratorsIDs=new LinkedList<String>();
        tempItems=new LinkedList<Item>();
        calledNext=new LinkedList<Boolean>();
        hasNext=new LinkedList<Boolean>();
    }

    // Creates a new Transaction instance
    //
    // PARAMETERS:
    //    type       A string that describes the type of the transaction
    //    time       A long that determines the time of the created transaction
    //    client     An integer that holds the client's number
    //
    // RETURNS:
    //    a String that holds the created transaction's id
    public String createTransaction(TransactionType type, long time, int client) {
        String transactionID=null;
        if (type == TransactionType.PURCHASE) {
            transactionList.add(new PurchaseTransaction(type, time, client));
        } else if (type == TransactionType.RETURN) {
            transactionList.add(new ReturnTransaction(type, time, client));
        } else if (type == TransactionType.BACKORDER) {
            transactionList.add(new BackorderTransaction(type, time, client));
        } else if (type == TransactionType.RESTOCK) {
            transactionList.add(new RestockTransaction(type, time, client));
        }
        transactionID = transactionList.getLast().getIdOfTransaction();
        return transactionID;
    }

    //  Adds a new item to a transaction, or changes its quantity. If the item code is already found in
    //    the transaction, adds the given quantity to the item's transaction quantity. The item's
    //    transaction quantity may be reduced (with a negative quantity parameter) but it cannot be
    //    reduced below zero. If the quantity reaches zero exactly, the item is removed from the transaction.
    //
    // PARAMETERS:
    //    id                id of the transaction
    //    item              items code, which should be added or changed in quantity if already exists
    //    quantity          the number of the items that should be added
    //
    // RETURNS:
    //    an error message, or null on success
    public String addItemToTransaction(String id, String item, int quantity) {
        int cost=0;
        if(item!=null){
            for (int j = 0; j < inventoryList.size(); j++) {
                if (inventoryList.get(j).getCode().equals(item)) {
                    cost = inventoryList.get(j).getPriceInCents();
                    for (int i = 0; i< transactionList.size(); i++){
                        if(transactionList.get(i).getIdOfTransaction().equals(id)){
                            if(!(transactionList.get(i).getIsClosed())) {
                                if(!(transactionList.get(i).getIsCanceled())) {
                                    transactionList.get(i).addItem(item, quantity, cost);
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        return "Error has occurred";
    }

    // Completes a transaction by changing the boolean variable, isClosed, to true
    //
    // PARAMETERS:
    //    id            id of the transaction that should be closed
    //
    // RETURNS:
    //    an error message, or null on success
    public String completeTransaction(String id) {
        String name;
        int amount;
        for (int index = 0; index< transactionList.size(); index++){
            if(transactionList.get(index).getIdOfTransaction().equals(id)){
                if(!(transactionList.get(index).getIsCanceled())) {
                    if(!(transactionList.get(index).getIsClosed())) {
                        if(transactionList.get(index).getType()== TransactionType.RETURN){
                            transactionList.get(index).closeTransaction();
                        }
                        else if(transactionList.get(index).getType()== TransactionType.PURCHASE){
                            boolean enough=false;
                            int equal=0;
                            //check if there is enough items in the stock
                            for(int q=0; q<transactionList.get(index).getItemCount(); q++){ //loop through the item list of the transaction
                                for(int j=0; j<inventoryList.size(); j++){                  //loop through the inventory list
                                    name=transactionList.get(index).getNameOfItem(q);
                                    if (name.equals(inventoryList.get(j).getCode())){
                                        amount=transactionList.get(index).getAmountOfItem(q);
                                        if(amount<=inventoryList.get(j).getQuantity()) {
                                            equal++;
                                        }
                                    }
                                }
                                if(equal==transactionList.get(index).getItemCount()){
                                    enough=true;
                                }
                            }
                            if(enough) {
                                for(int q=0; q<transactionList.get(index).getItemCount(); q++) {
                                    for (int j = 0; j < inventoryList.size(); j++) {
                                        name = transactionList.get(index).getNameOfItem(q);
                                        if (name.equals(inventoryList.get(j).getCode())) {
                                            amount = transactionList.get(index).getAmountOfItem(q);
                                            inventoryList.get(j).changeQuantity(amount);
                                        }
                                    }
                                }
                            }
                            else {
                                transactionList.get(index).setItemListToZero();
                            }
                            transactionList.get(index).closeTransaction();
                        }
                        else if(transactionList.get(index).getType()== TransactionType.BACKORDER){
                            for(int i=0; i<inventoryList.size(); i++) {
                                for (int j = 0; j < transactionList.get(index).getItemCount(); j++){
                                    name = transactionList.get(index).getNameOfItem(j);
                                    if((inventoryList.get(i)).getCode().equals(name)) {
                                        amount=transactionList.get(index).getAmountOfItem(j);
                                        inventoryList.get(i).backorderChange(amount);
                                    }
                                }
                            }
                            transactionList.get(index).closeTransaction();
                        }
                        else if(transactionList.get(index).getType()== TransactionType.RESTOCK){
                            for(int i=0; i<inventoryList.size(); i++) {
                                for (int j = 0; j < transactionList.get(index).getItemCount(); j++){
                                    name = transactionList.get(index).getNameOfItem(j);
                                    if((inventoryList.get(i)).getCode().equals(name)) {
                                        amount=transactionList.get(index).getAmountOfItem(j);
                                        inventoryList.get(i).restockChange(amount);
                                    }
                                }
                            }
                            transactionList.get(index).closeTransaction();
                        }
                        return null;
                    }
                }
            }
        }
        return "Error has occurred";
    }

    // Query some feature of a transaction.
    //
    // PARAMETERS:
    //    id            id of the transaction
    //    query         determines what kind of the information about transaction should be returned
    //
    // RETURNS:
    //    a String variable that is determined by the requested value; or null on error
    public String queryTransaction(String id, TransactionQuery query) {
        String result=null;
        for (int i = 0; i< transactionList.size(); i++){
            //find the transaction with by the id number
            if(transactionList.get(i).getIdOfTransaction().equals(id)){ //if found
                if (query==TransactionQuery.TYPE){
                    result= transactionList.get(i).getType().toString();
                }
                else if (query==TransactionQuery.ITEM_COUNT){
                    result=Integer.toString(transactionList.get(i).getItemCount());
                }
                else if (query==TransactionQuery.TOTAL_QUANTITY){
                    result=Integer.toString(transactionList.get(i).getTotalQuantityOfItems());
                }
                else if (query==TransactionQuery.TOTAL_COST){
                    result=Integer.toString(transactionList.get(i).getTotalCost());
                }
                else if (query==TransactionQuery.IS_COMPLETE){
                    result=Boolean.toString(transactionList.get(i).getIsClosed());
                }
            }
        }
        return result;
    }

    // Shows the details of the transaction
    //
    // PARAMETERS:
    //    id  id number of the transaction, which's details should be showed
    //
    // RETURNS:
    //    a String that represents details (ID, type, time, all the items, and whether or not it is complete)
    //    about the requested transaction, or null if something went wrong
    public String toString(String id) {
        String result=null;
        for(int i = 0; i< transactionList.size(); i++){
            if(transactionList.get(i).getIdOfTransaction().equals(id)){
                if(!(transactionList.get(i).getIsCanceled())) {
                    result = transactionList.get(i).toString();
                }
                else{
                    result="Transaction: "+transactionList.get(i).getIdOfTransaction()+" was canceled";
                }
            }
        }
        return result;
    }

    // Sends the requested information about the Server's status
    //
    // PARAMETERS:
    //    query  a ServerQuery type parameter that specifies which type of information should be returned
    //
    // RETURNS:
    //    a String, which is the requested information or null, if the requested information cannot be accessed
    public String queryServer(ServerQuery query) {
        String result=null;
        int resultNumber=-1;
        if(query==ServerQuery.INVENTORY_COUNT){
            resultNumber= inventoryList.size();
        }
        else if(query==ServerQuery.TRANSACTION_COMPLETED_COUNT){
            resultNumber=0;
            for (int i = 0; i< transactionList.size(); i++){
                if(transactionList.get(i).getIsClosed()){
                    if(!(transactionList.get(i).getIsCanceled())) {
                        resultNumber++;
                    }
                }
            }
        }
        else if(query==ServerQuery.TRANSACTION_IN_PROGRESS_COUNT){
            resultNumber=0;
            for (int i = 0; i< transactionList.size(); i++){
                if(!(transactionList.get(i).getIsClosed())){
                    if(!(transactionList.get(i).getIsCanceled())) {
                        resultNumber++;
                    }
                }
            }
        }
        if(resultNumber>-1){
            result=Integer.toString(resultNumber);
        }
        return result;
    }

    // Describes the inventory of alphabetically sorted items for sale.
    //
    // RETURNS:
    //    returns a string that describes the inventory on the server, that are ordered by item code, one item per line
    public String toString(){
        String result="";
        for (int i = 0; i< inventoryList.size(); i++){
            result=result+ inventoryList.get(i).getCode()+"\n";
        }
        return result;
    }

    // Searches for the items that has the requested pattern in the inventory
    //
    // PARAMETERS:
    //    pattern  a String to search for
    //    order    the order in which the search should be sorted
    //
    // RETURNS:
    //    a String that holds th ID of the iterator
    public String search(String pattern, ItemField order) {
        String iteratorID=null;
        LinkedList<Item> search=new LinkedList<Item>();

        if(pattern!=null) {
            for (int i = 0; i < inventoryList.size(); i++) {
                if ((inventoryList.get(i).getCode()).contains(pattern) || (inventoryList.get(i).getDescription()).contains(pattern)) {
                    search.add(inventoryList.get(i));
                }
            }

            if (order.equals(ItemField.COST)) {
                Collections.sort(search, new SortByCostSearch());
            }
            else if (order.equals(ItemField.DESCRIPTION)) {
                Collections.sort(search, new SortByDescriptionSearch());
            } else if (order.equals(ItemField.QUANTITY)) {
                Collections.sort(search, new SortByQuantitySearch());
            } else if (order.equals(ItemField.BACKORDER_QUANTITY)) {
                Collections.sort(search, new SortByBackorderQuantitySearch());
            }
            searchLists.add(search);
            iteratorID = "ItrtrID" + iteratorIDdigit;
            iteratorIDdigit++;
            iteratorsIDs.add(iteratorID);
            ListIterator<Item> itr = searchLists.get(searchesNumber).listIterator();
            iterators.add(itr);
            searchesNumber++;
            tempItems.add(null);
            calledNext.add(false);
            hasNext.add(false);
        }
        return iteratorID;
    }

    // Gets the next match for the search by the iterator ID, until it returns false;
    // if there is next item, returns true.
    //
    // PARAMETERS:
    //    iID  the iterator ID
    //
    // RETURNS:
    //    boolean variable, true if the next item exists, false otherwise
    public boolean next(String iID) {
        boolean hasNextItem=false;
        int index=-1;
        for(int i=0;i<iteratorsIDs.size();i++){
            if(iteratorsIDs.get(i).equals(iID)){
                if(iterators.get(i).hasNext()){
                    tempItems.set(i, iterators.get(i).next());
                    hasNextItem=true;
                    index=i;
                }
                if(index>-1) {
                    calledNext.set(index, true);
                }
                hasNext.set(i, hasNextItem);
            }
        }
        return hasNextItem;
    }

    // Queries some feature of the currently-iterated item
    //
    // PARAMETERS:
    //    iID  the iterator ID
    //
    // RETURNS:
    //    a String, which is the requested information or null, if the requested information cannot be accessed
    public String queryMatch(String iID, ItemField query) {
        String result=null;
        if(iID!=null) {
            for (int i = 0; i < iteratorsIDs.size(); i++) {
                //find the search's with by the id number
                if (iteratorsIDs.get(i).equals(iID)) { //if found
                    if(calledNext.get(i) && hasNext.get(i)){
                        if (query.equals(ItemField.CODE)) {
                            result = tempItems.get(i).getCode();
                        }
                        else if (query.equals(ItemField.COST)) {
                            result = Integer.toString(tempItems.get(i).getPriceInCents());
                        }
                        else if (query.equals(ItemField.DESCRIPTION)) {
                            result = tempItems.get(i).getDescription();
                        }
                        else if (query.equals(ItemField.QUANTITY)) {
                            result = Integer.toString(tempItems.get(i).getQuantity());
                        }
                        else if (query.equals(ItemField.BACKORDER_QUANTITY)) {
                            result = Integer.toString(tempItems.get(i).getBackorderQuantity());
                        }
                    }
                }
            }
        }
        return result;
    }

    // Cancels an existing transaction.
    //
    // PARAMETERS:
    //    id        String variable that holds
    //    client    integer, id of the client that wants to cancel the transaction
    //    time      long variable, says when the cancellation was called
    //
    // RETURN:
    //      String that contains error message or null if no error occurred
    //
    public String cancelTransaction(String id, long time, int client) {
        String result="Couldn't cancel transaction";
        for(int i=0; i<transactionList.size();i++){
            if(transactionList.get(i).getClient()==client){
                if(transactionList.get(i).getIdOfTransaction().equals(id)) {
                    if (!(transactionList.get(i).getIsCanceled())) {
                        if (!(transactionList.get(i).getIsClosed())) {
                            transactionList.get(i).cancelTransaction();
                            return null;
                        }
                        else{   //if transaction is already closed we need to move items back
                            if(transactionList.get(i).getType()== TransactionType.RETURN){
                                transactionList.get(i).cancelTransaction();
                            }
                            else if(transactionList.get(i).getType()== TransactionType.PURCHASE){
                                for(int q=0; q<transactionList.get(i).getItemCount(); q++) {
                                    for (int j = 0; j < inventoryList.size(); j++) {
                                        String name = transactionList.get(i).getNameOfItem(q);
                                        if (name.equals(inventoryList.get(j).getCode())) {
                                            int amount = transactionList.get(i).getAmountOfItem(q);
                                            inventoryList.get(j).returnQuantity(amount);
                                        }
                                    }
                                }
                                transactionList.get(i).cancelTransaction();
                            }
                            else if(transactionList.get(i).getType()== TransactionType.BACKORDER){
                                for(int q=0; q<inventoryList.size(); q++) {
                                    for (int j = 0; j < transactionList.get(i).getItemCount(); j++){
                                        String name = transactionList.get(i).getNameOfItem(j);
                                        if((inventoryList.get(q)).getCode().equals(name)) {
                                            int amount=transactionList.get(i).getAmountOfItem(j);
                                            inventoryList.get(q).backorderReturnChange(amount);
                                        }
                                    }
                                }
                                transactionList.get(i).cancelTransaction();
                            }
                            else if(transactionList.get(i).getType()== TransactionType.RESTOCK){
                                for(int q=0; q<inventoryList.size(); q++) {
                                    for (int j = 0; j < transactionList.get(i).getItemCount(); j++){
                                        String name = transactionList.get(i).getNameOfItem(j);
                                        if((inventoryList.get(q)).getCode().equals(name)) {
                                            int amount=transactionList.get(i).getAmountOfItem(j);
                                            inventoryList.get(q).restockReturnChange(amount);
                                        }
                                    }
                                }
                                transactionList.get(i).cancelTransaction();
                            }
                            return null;
                        }
                    }
                }
            }
        }
        return result;
    }
}
