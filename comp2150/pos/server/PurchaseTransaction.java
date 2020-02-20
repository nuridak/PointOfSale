/**
 * PurchaseTransaction
 *
 * @author Nurida Karimbaeva, 7851221
 *
 * REMARKS: This class extends Transaction class, and creates an instances of the Purchase Transaction
 *
 */
package comp2150.pos.server;
import java.util.LinkedList;

public class PurchaseTransaction extends Transaction{
    private boolean isClosed;
    private boolean isCanceled;
    private LinkedList<ItemTransaction> itemsList;
    private String idOfTransaction;
    public static int idNumber=0;

    //Constructor for the Transaction
    //
    // PARAMETERS:
    //    type       A string that describes the type of the transaction
    //    time       A long that determines the time of the creaed transaction
    //
    public PurchaseTransaction(A5POSServer.TransactionType type, long time, int client){
        super(type, time, client);
        idOfTransaction="P"+idNumber;
        itemsList=new LinkedList<ItemTransaction>();
        isClosed=false;
        isCanceled=false;
        idNumber++;
    }

    //
    //Closes the transaction
    //
    public void closeTransaction() {
        isClosed=true;
    }
    //
    //Cancel Transaction
    //
    public void cancelTransaction(){
        isCanceled=true;
    }
    //
    // Sets the quantity of each item to zero
    //
    public void setItemListToZero(){
        for (int i = 0; i < itemsList.size(); i++) {
            itemsList.get(i).setQuantitytoZero();
        }
    }
    //
    // Adds a new item to the Transactions ItemList
    //
    // PARAMETERS:
    //    name       a string that holds the code(name) of the item to be added
    //    number     an integer that holds the number of items
    //    cost       an integer that holds the price of the item
    //
    public void addItem(String name, int number, int cost){
        if(itemsList.size()==0){
            itemsList.add(new ItemTransaction(name, number, cost));
        }
        else {
            int index=-1;
            //there is already similar item in the list
            for (int i = 0; i < itemsList.size(); i++) {
                if (itemsList.get(i).getItemName().equals(name)) {
                    index=i;
                }
            }
            if(index!=(-1)){
                itemsList.get(index).changeQuantity(number);
                if(itemsList.get(index).getQuantity()==0){
                    itemsList.remove(index);
                }
            }
            else {
                itemsList.add(new ItemTransaction(name, number, cost));
            }
        }

    }
    //
    //Returns the number of different items
    //
    public  int getItemCount(){
        int count=0;
        count=itemsList.size();
        return count;
    }
    //
    // Returns the quantity of the specific item
    //
    // PARAMETERS:
    //    item       A string that holds the code(name) of the item
    //
    // RETURNS:
    //    an integer that holds the number of the requested items
    //
    public int getQuantityOfItem(String item) {
        int quantityOfItem=0;
        for(int i=0; i<itemsList.size();i++){
            if(itemsList.get(i).getItemName().equals(item)){
                quantityOfItem=itemsList.get(i).getQuantity();
            }
        }
        return quantityOfItem;
    }
    //
    //Returns the total amount of items
    //
    public int getTotalQuantityOfItems(){
        int totalNumber=0;
        for(int i=0; i<itemsList.size(); i++){
            totalNumber=totalNumber+itemsList.get(i).getQuantity();
        }
        return totalNumber;
    }
    //
    //Returns the total cost of all of the items
    //
    public int getTotalCost() {
        int totalCost=0;
        for(int i=0; i<itemsList.size(); i++){
            for(int j=0; j<itemsList.get(i).getQuantity(); j++) {
                totalCost = totalCost + itemsList.get(i).getCost();
            }
        }
        return totalCost;
    }
    //
    //Returns the isClosed value
    //
    public boolean getIsClosed() {
        return isClosed;
    }
    //
    //Returns the isCanceled value
    //
    public boolean getIsCanceled() {
        return isCanceled;
    }
    //
    //Returns the id of the transaction
    //
    public String getIdOfTransaction(){
        return idOfTransaction;
    }
    //
    // Describes the transaction
    //
    // RETURNS:
    //    returns a string that describes the transaction
    //
    public String toString(){
        String result=null;
        result=idOfTransaction+" "+super.toString();
        if(itemsList.size()==0){
            result=result+"\n      There is no items in the transaction";
        }else {
            for (int i = 0; i < itemsList.size(); i++) {
                result = result + "\n      "+itemsList.get(i).getItemName();
                result = result + "      Quantity:"+itemsList.get(i).getQuantity();
            }
        }
        result=result+"\n      IsClosed: "+isClosed+"\n";
        return result;
    }

    //For outside access and Iteration

    // Returns the name of the ith item
    //
    // PARAMETERS:
    //    index       index of the requested item's name
    //
    public String getNameOfItem(int index){
        String name=null;
        name=itemsList.get(index).getItemName();
        return name;
    }
    // Returns the number of the ith items
    //
    // PARAMETERS:
    //    index       index of the requested items' quantity
    //
    public int getAmountOfItem(int index){
        int amount=0;
        amount=itemsList.get(index).getQuantity();
        return amount;
    }


}