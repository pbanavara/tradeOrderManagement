package core;

import Handlers.MarketDataHandler;
import data.Request;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        MarketDataHandler handler = new MarketDataHandler("0.0.0.0");
        Request request = new Request();
        request.iTokenNmbr = 1;
        request.iLimitPrice1 = new Random().nextInt();
        handler.sendRequest(request);
    }

}
