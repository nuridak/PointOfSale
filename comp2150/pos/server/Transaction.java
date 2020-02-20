/**
 * Transaction
 *
 * @author Nurida Karimbaeva, 7851221
 *
 * REMARKS: This is an abstract class that is extended into 4 types of transactions
 *
 */
package comp2150.pos.server;

abstract class Transaction {
    private A5POSServer.TransactionType type;
    private long time;
    private int client;
    public Transaction(){
    }
    //Constructor for the Transaction
    //
    // PARAMETERS:
    //    type       A string that describes the type of the transaction
    //    time       A long that determines the time of the creaed transaction
    //
    public Transaction(A5POSServer.TransactionType type, long time, int client){
        this.type=type;
        this.time=time;
        this.client=client;
    }
    //
    //Returns the time
    //
    public long getTime() {
        return time;
    }
    //
    //Returns the type of the transaction
    //
    public A5POSServer.TransactionType getType() {
        return type;
    }
    //
    //Returns client's number
    //
    public int getClient(){
        return client;
    }
    //
    // Describes the transaction
    //
    // RETURNS:
    //    returns a string that describes the transaction
    public String toString(){
        String result=null;
        result=type+" "+time+" Items: ";
        return result;
    }
    //Abstract Methods that are implemented in the subclasses
    public abstract String getIdOfTransaction();
    public abstract boolean getIsClosed();
    public abstract boolean getIsCanceled();
    public abstract void closeTransaction();
    public abstract void cancelTransaction();
    public abstract void addItem(String name, int number, int cost);
    public abstract int getQuantityOfItem(String item);
    public abstract int getTotalQuantityOfItems();
    public abstract int getTotalCost();
    public abstract int getItemCount();
    public abstract String getNameOfItem(int number);
    public abstract int getAmountOfItem(int number);
    public abstract void setItemListToZero();
}
