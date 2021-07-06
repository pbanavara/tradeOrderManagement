package Handlers;

import data.Request;

/**
 * The high level order data handler per member-user-contract
 * So the 'key' here is a composite of member, user, contract (JPMorgan-Pradeep-AAPL)
 */
public class OrderDataHandler {

    /**
     * For every contract that is part of the composite key, this method updates the overall orders for individual
     * contracts, for the specific broker, for that user. Performs the following actions
     * Something like a daemon thread that keeps tab of contract fulfillment and updates order book
     * This needs access to the request data structure or object
     *
     * @param compositeKey
     */
    public void appendOrderBook(String compositeKey, Request request) {
         while (true) {
             // extract contractId and other contract parameters
             // check on the return code of
             MarketDataHandler mHandler = new MarketDataHandler("0.0.0.0");
             if (mHandler.sendRequest(request) == 1) {
                 // update order book table with the requisite information.
             }
         }
    }

    public void appendActivityBook(String compositeKey) {
        // Similar to orderDataHandler
    }
}

