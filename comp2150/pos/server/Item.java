/**
 * Item
 *
 * @author Nurida Karimbaeva, 7851221
 *
 * REMARKS: This class stores the constructors and methods for items from the inventory
 */
package comp2150.pos.server;

public class Item {
    private String code;
    private String description;
    private int priceInCents;
    private int quantity;
    private int backorderQuantity;
    //CONSTRUCTORS
    // Constructor
    //
    // PARAMETERS:
    //    c     String that holds the code of the item
    //    d     String that holds the description of the item
    //    p     an integer that holds the price of the item
    //
    public Item(String c, String d, int p, int stock){
        code=c;
        description=d;
        priceInCents=p;
        quantity=stock;
        backorderQuantity=0;
    }
    //
    //Returns the code of the item
    //
    public String getCode() {
        return code;
    }
    //
    //Returns the Description of the item
    //
    public String getDescription(){
        return description;
    }
    //
    //Returns the price of the item
    //
    public int getPriceInCents() {
        return priceInCents;
    }
    //
    //Returns the number of items in stock
    //
    public int getQuantity(){
        return quantity;
    }
    //
    //Returns the number of items in backOrder
    //
    public int getBackorderQuantity(){
        return backorderQuantity;
    }

    // Returns(increases) the number of items
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void returnQuantity(int number) {
        quantity = quantity + number;
    }

    // Returns(increases) the number of items by rules of Backorder
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void backorderReturnChange(int number) {
        if(backorderQuantity>0){
            if(number > backorderQuantity){
                quantity=number-backorderQuantity;
                backorderQuantity=0;
            }
            else{
                backorderQuantity-=number;
            }
        }
        else{
            quantity = quantity + number;
        }
    }

    // Returns(increases) the number of items by rules of Restock
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void restockReturnChange(int number) {
        if(quantity>0){
            if(number > quantity){
                backorderQuantity=number-quantity;
                quantity=0;
            }
            else{
                quantity-=number;
            }
        }
        else{
            backorderQuantity = backorderQuantity + number;
        }
    }

    // Changes the number of items
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void changeQuantity(int number) {
        quantity = quantity - number;
    }

    // Changes the number of items by rules of Backorder
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void backorderChange(int number) {
        if(backorderQuantity>0){
            backorderQuantity+=number;
        }
        else{
            quantity = quantity - number;
            if(quantity<0 ){
                backorderQuantity=backorderQuantity+(Math.abs(quantity));
                quantity=0;
            }
        }
    }

    // Changes the number of items by rules of Restock
    //
    // PARAMETERS:
    //    number       an integer that holds the amount by which the quantity of items should be changed
    //
    public void restockChange(int number) {
        if(quantity>0){
            quantity+=number;
        }
        else{
            backorderQuantity = backorderQuantity - number;
            if(backorderQuantity<0 ){
                quantity=quantity+(Math.abs(backorderQuantity));
                backorderQuantity=0;
            }
        }
    }
}
