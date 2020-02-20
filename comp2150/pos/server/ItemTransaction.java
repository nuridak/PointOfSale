/**
 * ItemTransaction
 *
 * @author Nurida Karimbaeva, 7851221
 *
 * REMARKS: This class was created to store information about items that are added to the transaction
 *
 */
package comp2150.pos.server;

public class ItemTransaction{
    private String itemName;
    private int cost;
    private int quantity;

    //Constructor for the item
    //
    // PARAMETERS:
    //    name       String that holds the name(code) of the item
    //    number     an integer that holds the quantity of the items
    //    c          an integer that holds the price of the item
    //
    public ItemTransaction(String name, int number, int c){
        itemName=name;
        quantity=number;
        cost=c;
    }
    // Changes the number of items
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void changeQuantity(int number) {
        if(number<0){
            number=Math.abs(number);
            quantity=quantity-number;
            if (quantity < 0) {
                quantity = 0;
            }
        }
        else {
            quantity = quantity + number;
        }
    }

    //
    //Sets the quantity of the items to 0
    //
    public void setQuantitytoZero(){
        this.quantity=0;
    }
    //
    //Returns the Price of the item
    //
    public  int getCost(){
        return cost;
    }
    //
    //Returns the name(code) of the item
    //
    public String getItemName() {
        return itemName;
    }
    //
    //Returns the quantity of the items
    //
    public int getQuantity() {
        return quantity;
    }
}
