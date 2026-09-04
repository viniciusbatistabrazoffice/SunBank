package com.backend.service;

import com.backend.config.BlockchainConfig;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BlockchainService {

    private static final int DECIMALS = 18;

    private final BlockchainConfig config;
    private final BlockchainAccountService accountService;

    private Web3j web3j;
    private Credentials treasuryCredentials;

    public BlockchainService(BlockchainConfig config, BlockchainAccountService accountService) {
        this.config = config;
        this.accountService = accountService;
    }

    @PostConstruct
    public void init() {
        if (!config.isEnabled()) {
            return;
        }
        this.web3j = Web3j.build(new HttpService(config.getRpcUrl()));
        if (config.getPrivateKey() != null && !config.getPrivateKey().isBlank()) {
            this.treasuryCredentials = Credentials.create(config.getPrivateKey());
        }
    }

    public boolean isEnabled() {
        return config.isEnabled() && web3j != null;
    }

    public BigDecimal getBalance(String address) throws Exception {
        requireEnabled();
        BigInteger balance = callBalanceOf(address);
        return fromSmallestUnit(balance);
    }

    public String transferFromTreasury(String toAddress, BigDecimal amount) throws Exception {
        requireEnabled();
        if (treasuryCredentials == null) {
            throw new IllegalStateException("Treasury private key not configured");
        }
        return sendTransfer(treasuryCredentials, toAddress, amount);
    }

    public String transferFromClient(String encryptedPrivateKey, String toAddress, BigDecimal amount) throws Exception {
        requireEnabled();
        Credentials from = accountService.loadCredentials(encryptedPrivateKey);
        return sendTransfer(from, toAddress, amount);
    }

    private void requireEnabled() {
        if (!isEnabled()) {
            throw new IllegalStateException("Blockchain integration is disabled");
        }
    }

    private BigInteger callBalanceOf(String address) throws Exception {
        Function function = new Function(
                "balanceOf",
                Collections.singletonList(new Address(address)),
                Collections.singletonList(new TypeReference<Uint256>() {})
        );
        String encoded = FunctionEncoder.encode(function);
        Transaction tx = Transaction.createEthCallTransaction(address, config.getContractAddress(), encoded);
        EthCall response = web3j.ethCall(tx, DefaultBlockParameterName.LATEST).send();
        if (response.hasError()) {
            throw new RuntimeException("Balance call failed: " + response.getError().getMessage());
        }
        List<Type> outputs = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return ((Uint256) outputs.get(0)).getValue();
    }

    private String sendTransfer(Credentials from, String toAddress, BigDecimal amount) throws Exception {
        BigInteger value = toSmallestUnit(amount);
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), new Uint256(value)),
                Collections.emptyList()
        );
        String encoded = FunctionEncoder.encode(function);

        EthGetTransactionCount txCount = web3j.ethGetTransactionCount(
                from.getAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = txCount.getTransactionCount();

        org.web3j.protocol.core.methods.response.EthGasPrice gasPriceResponse = web3j.ethGasPrice().send();
        BigInteger gasPrice = gasPriceResponse.getGasPrice();

        long chainId = web3j.ethChainId().send().getChainId().longValue();

        RawTransaction raw = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                config.getGasLimit(),
                config.getContractAddress(),
                BigInteger.ZERO,
                encoded
        );

        byte[] signed = TransactionEncoder.signMessage(raw, chainId, from);
        String signedTx = Numeric.toHexString(signed);
        EthSendTransaction send = web3j.ethSendRawTransaction(signedTx).send();
        if (send.hasError()) {
            throw new RuntimeException("Transfer failed: " + send.getError().getMessage());
        }
        return send.getTransactionHash();
    }

    private static BigInteger toSmallestUnit(BigDecimal amount) {
        BigDecimal factor = BigDecimal.TEN.pow(DECIMALS);
        return amount.multiply(factor).toBigInteger();
    }

    private static BigDecimal fromSmallestUnit(BigInteger amount) {
        BigDecimal factor = BigDecimal.TEN.pow(DECIMALS);
        return new BigDecimal(amount).divide(factor, DECIMALS, RoundingMode.HALF_UP);
    }
}
