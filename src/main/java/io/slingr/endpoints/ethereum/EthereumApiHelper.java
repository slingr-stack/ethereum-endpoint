package io.slingr.endpoints.ethereum;

import io.slingr.endpoints.services.HttpService;
import io.slingr.endpoints.utils.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EthereumApiHelper {
    private HttpService httpService;

    public EthereumApiHelper(HttpService httpService) {
        this.httpService = httpService;
    }

    public Json getBlockByHash(String blockHash, boolean fullTransactions) {
        Json body = this.getBody("eth_getBlockByHash", Json.list().push(blockHash).push(fullTransactions));
        Json response = this.httpService.post(body);
        return response != null ? response.json("result") : null;
    }

    public Json getBlockByNumber(String number, boolean fullTransactions) {
        Json body = this.getBody("eth_getBlockByNumber", Json.list().push(number).push(fullTransactions));
        Json response = this.httpService.post(body);
        return response != null ? response.json("result") : null;
    }

    public Json getTransactionReceipt(String txHash) {
        Json body = this.getBody("eth_getTransactionReceipt", Json.list().push(txHash));
        Json response = this.httpService.post(body);
        return response != null ? response.json("result") : null;
    }

    public long getBlockNumber() {
        Json response = this.httpService.post(this.getBody("eth_blockNumber", Json.list()));
        if (response != null) {
            String blockNumber = response.string("result");
            return EthereumHelper.convertedHexToNumber(blockNumber);
        }
        return -1;
    }

    public List<Json> getLogsByBlock(String hash) {
        Json body = this.getBody("eth_getLogs", Json.list().push(Json.map().set("blockHash", hash)));
        Json response = this.httpService.post(body);
        return response != null ? response.jsons("result") : new ArrayList<>();
    }

    private Json getBody(String method, Json params) {
        return Json.map()
                .set("id", new Date().getTime())
                .set("jsonrpc", "2.0")
                .set("method", method)
                .set("params", params);
    }
}
