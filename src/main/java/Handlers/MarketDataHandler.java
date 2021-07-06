package Handlers;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import data.ContractPriceSummary;
import data.ContractSummary;
import data.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The top most abstraction for handling contract buy/sell orders
 */
public class MarketDataHandler {
    private AerospikeClient client;
    private Map<Long, ContractSummary> contractSummary; //Will be mapped to Aerospike set contractSummary
    private Map<Long, ContractPriceSummary> contractPriceSummary; //Will be mapped to Aerospike set contractPriceSummary
    private final String NAMESPACE = "test";
    public MarketDataHandler(String host) {
        ClientPolicy policy = new ClientPolicy();
        policy.user = "superman";
        policy.password = "lois";
        client = new AerospikeClient(policy, host, 3000);
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
        ContractPriceSummary cPriceSummary = new ContractPriceSummary();
        String SET = "contractPriceSummary";
        long before = System.currentTimeMillis();
        int c = 0;
        for (int i = 0;i < 2500; ++i) {
            try {
                cPriceSummary.tokenNumber = request.iTokenNmbr.intValue();
                cPriceSummary.pricePoint = request.iLimitPrice1;
                Key key = new Key(NAMESPACE, SET, cPriceSummary.tokenNumber);
                appendContractPriceSummary(cPriceSummary, key);
                //c += 1;
            } catch (Exception e) {
                e.printStackTrace();
                rollbackUpdateContractSummary();
                return -1;
            }
        }
        long elapsed = System.currentTimeMillis() - before;
        System.out.println("Elapsed time :" + elapsed);
        return 0;
    }

    private void rollbackUpdateContractSummary() {

    }

    /**
     * Can technically use Java Object Mapper to directly write contractPriceSummary to AS
     * @param cPriceSummary
     * @return
     */
    private int appendContractPriceSummary(ContractPriceSummary cPriceSummary, Key key) {
        Bin timeStamp = new Bin("timestamp", System.currentTimeMillis());
        Bin price = new Bin("price", cPriceSummary.pricePoint);
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