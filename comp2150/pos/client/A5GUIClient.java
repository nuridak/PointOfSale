/**
 * COMP 2150 Summer 2019: Assignment 5 Question 1 "Point of Sale"
 */

package comp2150.pos.client;

public interface A5GUIClient {
   void updateStatistics();
   void updateInventory();
   void updateTransaction(String transactionID);
   
   String getTimeStamp();
}
