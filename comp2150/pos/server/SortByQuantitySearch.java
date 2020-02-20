/**
 * SortByQuantitySearch
 *
 * @author Nurida Karimbaeva, 7851221
 *
 * REMARKS: This class implements Comparator, to sort search list in the Quantity order
 *
 */
package comp2150.pos.server;
import java.util.Comparator;

public class SortByQuantitySearch implements Comparator<Item> {

    // Compares two Items by quantity
    //
    // PARAMETERS:
    //    o1       Item object to be compared
    //    o2       Item object to be compared
    //
    // RETURNS:
    //    difference between two
    //
    public int compare(Item o1, Item o2) {
        return o1.getQuantity() - o2.getQuantity();
    }
}
