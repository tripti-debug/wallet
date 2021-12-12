package edu.njit.wallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.njit.wallet.model.SendTransaction;
import edu.njit.wallet.model.TransactionStatus;
import edu.njit.wallet.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricsService {
    private SendTransactionService sendTransactionService;
    private TransactionService transactionService;
    private ObjectMapper objectMapper;
    private ElecAddressService elecAddressService;
    private UserAccountService userAccountService;

    @Autowired
    public MetricsService(SendTransactionService sendTransactionService, TransactionService transactionService,
                          ObjectMapper objectMapper, ElecAddressService elecAddressService, UserAccountService userAccountService) {
        this.sendTransactionService = sendTransactionService;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
        this.elecAddressService = elecAddressService;
        this.userAccountService = userAccountService;
    }

    public Map<String, Object> getTotalSpendingByRange(String ssn, Date from, Date to) {
        List<SendTransaction> sendTransactionList = sendTransactionService.getAllSendTransaction(ssn);
        Double sendAmount = sendTransactionList.stream()
                .filter(sendTransaction -> sendTransaction.getStatus().equals(TransactionStatus.SUCCESS) &&
                        sendTransaction.getDateortime().before(to) && sendTransaction.getDateortime().after(from))
                .map(sendTransaction -> sendTransaction.getAmount())
                .reduce(0D, Double::sum);
        List<Map<String, Object>> receiveTransactionList = transactionService.getReceivedTransactions(ssn);
        Double receiveAmount= receiveTransactionList.stream()
                .filter(receiveTransaction -> {
                    Date transactionDate = new Date((Long) receiveTransaction.get("dateortime"));
                    return transactionDate.before(to) && transactionDate.after(from) && receiveTransaction.get("status").equals(TransactionStatus.SUCCESS.name());
                })
                .map(transaction -> (Double)transaction.get("amount"))
                .reduce(0D, Double::sum);
        return Map.of("send-amount", sendAmount, "receive-amount", receiveAmount);
    }

    public Map<String, Object> getTotalSpendingByMonth(String ssn, int month, int year) {
        List<SendTransaction> sendTransactionList = sendTransactionService.getAllSendTransaction(ssn);
        Double sendAmount = sendTransactionList.stream()
                .filter(sendTransaction -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sendTransaction.getDateortime());
                    int transactionMonth =  calendar.get(Calendar.MONTH) + 1;
                    int transactionYear = calendar.get(Calendar.YEAR);
                    return sendTransaction.getStatus().equals(TransactionStatus.SUCCESS) && transactionMonth ==  month && transactionYear == year;
                })
                .map(sendTransaction -> sendTransaction.getAmount())
                .reduce(0D, Double::sum);
        Double avgSendAmount = sendAmount / sendTransactionList.size();
        List<Map<String, Object>> receiveTransactionList = transactionService.getReceivedTransactions(ssn);
        Double receiveAmount= receiveTransactionList.stream()
                .filter(receiveTransaction -> {
                    Date transactionDate = new Date((Long) receiveTransaction.get("dateortime"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transactionDate);
                    int transactionMonth =  calendar.get(Calendar.MONTH) + 1;
                    int transactionYear = calendar.get(Calendar.YEAR);
                    return transactionMonth == month && transactionYear == year && receiveTransaction.get("status").equals(TransactionStatus.SUCCESS.name());
                })
                .map(transaction -> (Double)transaction.get("amount"))
                .reduce(0D, Double::sum);
        Double avgReceiveAmount = receiveAmount / receiveTransactionList.size();
        return Map.of("send-amount", Math.round(sendAmount * 100.0) / 100.0, "receive-amount", Math.round(receiveAmount * 100.0) / 100.0, "avg-send-amount", Math.round(avgSendAmount * 100.0) / 100.0, "avg-receive-amount", Math.round(avgReceiveAmount * 100.0) / 100.0);
    }

    public Map<String, Object> getMaxSpendingByMonth(String ssn, int month, int year) {
        List<SendTransaction> sendTransactionList = sendTransactionService.getAllSendTransaction(ssn);
        List<SendTransaction> sendTransactionItem = sendTransactionList.stream()
                .filter(sendTransaction -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sendTransaction.getDateortime());
                    int transactionMonth =  calendar.get(Calendar.MONTH) + 1;
                    int transactionYear = calendar.get(Calendar.YEAR);
                    return sendTransaction.getStatus().equals(TransactionStatus.SUCCESS) && transactionMonth ==  month && transactionYear == year;
                })
                .max((a, b) -> b.getAmount().compareTo(a.getAmount()))
                .stream()
                .filter(item -> item != null)
                .collect(Collectors.toList());

        List<Map<String, Object>> receiveTransactionList = transactionService.getReceivedTransactions(ssn);
        List<Map<String, Object>> resultReceiveTransaction= receiveTransactionList.stream()
                .filter(receiveTransaction -> {
                    Date transactionDate = new Date((Long) receiveTransaction.get("dateortime"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transactionDate);
                    int transactionMonth =  calendar.get(Calendar.MONTH) + 1;
                    int transactionYear = calendar.get(Calendar.YEAR);
                    return transactionMonth == month && transactionYear == year && receiveTransaction.get("status").equals(TransactionStatus.SUCCESS.name());
                })
                .max((a, b) -> ((Double) b.get("amount")).compareTo(((Double) a.get("amount"))))
                .stream()
                .filter(item -> item != null)
                .collect(Collectors.toList());

        return Map.of("received_transactions", resultReceiveTransaction, "send_transactions", sendTransactionItem);
    }

    public List<Map<String, String>> getBestUsers(String ssn) {
        List<SendTransaction> sendTransactions = sendTransactionService.getAllSendTransaction(ssn);
        List<String> identifier = sendTransactions.stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.SUCCESS))
                .max((a,b) -> b.getAmount().compareTo(a.getAmount()))
                .stream()
                .filter(item -> item != null)
                .map(item -> item.getIdentifier())
                .collect(Collectors.toList());
        List<Map<String, Object>> receiveTransactionList = transactionService.getReceivedTransactions(ssn);
        identifier.addAll(receiveTransactionList.stream()
                .filter(transaction -> transaction.get("status").toString().equals(TransactionStatus.SUCCESS.name()))
                .max((a, b) -> ((Double) b.get("amount")).compareTo(((Double) a.get("amount"))))
                .stream()
                .filter(item -> item != null)
                .map(item -> {
                    if (item.get("ssn").toString().equals(ssn)) {
                        return item.get("identifier").toString();
                    }
                    return null;
                })
                .filter(item -> item != null)
                .collect(Collectors.toList()));
        List<String> ssnList = receiveTransactionList.stream()
                .filter(transaction -> transaction.get("status").toString().equals(TransactionStatus.SUCCESS.name()))
                .max((a, b) -> ((Double) b.get("amount")).compareTo(((Double) a.get("amount"))))
                .stream()
                .filter(item -> item != null)
                .map(item -> {
                    if (!item.get("ssn").toString().equals(ssn)) {
                        return item.get("ssn").toString();
                    }
                    return null;
                })
                .filter(item -> item != null)
                .collect(Collectors.toList());
        ssnList.addAll(elecAddressService.getSsnByIdentifiers(identifier));
        List<UserAccount> accounts = userAccountService.getUserBySSN(ssnList);
        return accounts.stream()
                .map(item -> Map.of("firstname", item.getFirstname(),
                        "lastname", item.getLastname(),
                        "emailid", item.getEmail(),
                        "phoneno", item.getPhoneno()))
                .collect(Collectors.toList());
    }
}
