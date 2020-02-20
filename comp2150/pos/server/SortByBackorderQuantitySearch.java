/**
 * SortByBackorderQuantitySearch
 *
 * @author Nurida Karimbaeva, 7851221
 *
 * REMARKS: This class implements Comparator, to sort search list in the BackorderQuantity order
 *
 */
package comp2150.pos.server;
import java.util.Comparator;


public class SortByBackorderQuantitySearch implements Comparator<Item> {

    // Compares two Items by BackorderQuantity
    //
    // PARAMETERS:
    //    o1       Item object to be compared
    //    o2       Item object to be compared
    //
    // RETURNS:
    //    difference between two
    //
    public int compare(Item o1, Item o2) {
        return o1.getBackorderQuantity() - o2.getBackorderQuantity();
    }
}
