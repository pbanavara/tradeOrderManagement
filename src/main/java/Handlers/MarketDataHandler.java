package Handlers;

import com.aerospike.client.*;
import data.ContractPriceSummary;
import data.ContractSummary;
import data.Request;

import java.util.List;
import java.util.Map;

/**
 * The top most abstraction for handling contract buy/sell orders
 */
public class MarketDataHandler {
    private AerospikeClient client;
    private Map<Long, ContractSummary> contractSummary; //Will be mapped to Aerospike set contractSummary
    private Map<Long, ContractPriceSummary> contractPriceSummary; //Will be mapped to Aerospike set contractPriceSummary
    private final String NAMESPACE = "nse";
    public MarketDataHandler() {
        client = new AerospikeClient(null, "0.0.0.0", 3000);
    }
    /**
     * Handles trade requests in a synchronized manner across multiple threads.
     * Does the following in sequence
     * Update the contractSummary datastructure
     * Update the contractPriceSummary datastructure
     * Sends the response code for request completion, error code otherwise.
     * @param request
     */
    public Integer sendRequest(Request request) {
        ContractSummary cSummary = null;
        ContractPriceSummary cPriceSummary = null;
        try {
            if (appendContractPriceSummary(cPriceSummary) != -1) {
                updateContractSummary(cSummary);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private int appendContractPriceSummary(ContractPriceSummary cPriceSummary) {
        String SET = "contractPriceSummary";
        Key key = new Key(NAMESPACE, SET, cPriceSummary.toString());
        Bin timeStamp = new Bin("timestamp", 0);
        Bin price = new Bin("price", 100);
        try {
            client.put(null, key, timeStamp, price);
        } catch (AerospikeException ae) {
            ae.printStackTrace();
            return -1;
        }
        return 0;
    }
    private void updateContractSummary(ContractSummary cSummary) {

    }

    private void beginTransaction() {

    }
    private void endTransaction() {

    }
}