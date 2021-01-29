package tuwien.models;

public class BlockChainConfig {

    private String blockChainAddressWS;
    private String blockChainAddressHttp;
    private String contractAddress;
    private String privateAccountKey;

    public String getBlockChainAddressWS() {
        return blockChainAddressWS;
    }

    public void setBlockChainAddressWS(String blockChainAddressWS) {
        this.blockChainAddressWS = blockChainAddressWS;
    }

    public String getBlockChainAddressHttp() {
        return blockChainAddressHttp;
    }

    public void setBlockChainAddressHttp(String blockChainAddressHttp) {
        this.blockChainAddressHttp = blockChainAddressHttp;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getPrivateAccountKey() {
        return privateAccountKey;
    }

    public void setPrivateAccountKey(String privateAccountKey) {
        this.privateAccountKey = privateAccountKey;
    }
}
