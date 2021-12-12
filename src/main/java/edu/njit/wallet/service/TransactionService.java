package edu.njit.wallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.njit.wallet.Repository.ReceiveRepo;
import edu.njit.wallet.model.Transaction;
import edu.njit.wallet.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private ReceiveRepo transactionRepo;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SendTransactionService sendTransactionService;

    @Autowired
    private RequestTransactionService requestTransactionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Transaction> viewTransactionByDate(String ssn, String Date){
       return transactionRepo.viewByDate(ssn,Date);

    }

    public Map<String, String> viewTransactionByMonth(String ssn, String Month){
        return transactionRepo.viewByMonth(ssn,Month);

    }

    public List<Map<String, Object>> getReceivedTransactions(String ssn) {
        List<String> identifiers = new ArrayList<>(Arrays.asList(userAccountService.getUserBySSN(ssn).getPhoneno()));
        List<String> emails = emailService.getAllEmailsBySSN(ssn).stream().map(item -> item.getEmailAdd()).collect(Collectors.toList());
        identifiers.addAll(emails);
        List<Map<String, Object>> receivedFromSendTransaction = objectMapper
                .convertValue(sendTransactionService.getAllSendTransaction(identifiers)
                        .stream()
                                .filter(sendTransaction -> sendTransaction.getStatus().equals(TransactionStatus.SUCCESS))
                                .collect(Collectors.toList()), List.class);
        List<Map<String, Object>> receivedFromRequestTransaction = objectMapper
                .convertValue(requestTransactionService.getRequestTransactionBySSN(ssn)
                        .stream()
                        .filter(requestTransaction -> requestTransaction.getStatus().equals(TransactionStatus.SUCCESS))
                        .collect(Collectors.toList()),List.class);
        receivedFromSendTransaction.addAll(receivedFromRequestTransaction);
        Collections.sort(receivedFromSendTransaction, (t1, t2) -> ((Long) t2.get("dateortime")).compareTo(((Long)t1.get("dateortime"))));
        return receivedFromSendTransaction;
    }
}
