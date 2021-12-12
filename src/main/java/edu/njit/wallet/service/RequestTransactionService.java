package edu.njit.wallet.service;

import edu.njit.wallet.Repository.RequestTransactionRepo;
import edu.njit.wallet.Repository.ReceiveRepo;
import edu.njit.wallet.model.RequestTransaction;
import edu.njit.wallet.model.TransactionStatus;
import edu.njit.wallet.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestTransactionService {
    @Autowired
    private RequestTransactionRepo requestTransactionRepo;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ReceiveRepo receiveRepo;

    @Autowired
    private EmailService emailService;

    public void saveRequestTransaction(@RequestBody RequestTransaction requestTransaction) {
        try {
            requestTransactionRepo.saveRequestDetails(new Date(), requestTransaction.getMemo(),
                    requestTransaction.getIdentifier(), requestTransaction.getSsn(), requestTransaction.getAmount(),
                    0.00, TransactionStatus.PENDING.name());

        } catch (Exception e) {
            throw e;
        }
    }

    public List<RequestTransaction> getRequestTransactionBySSN(String ssn) {
        List<String> identifiers = emailService.getAllEmailsBySSN(ssn)
                .stream().map(email -> email.getEmailAdd()).collect(Collectors.toList());
        String phone = userAccountService.getUserBySSN(ssn).getPhoneno();
        identifiers.add(phone);
        return requestTransactionRepo.getRequestTransactionBySSN(ssn, identifiers);
    }

    public void approveTransaction(int id, String ssn) {
        RequestTransaction requestTransaction = requestTransactionRepo.getRequestTransactionId(id);
        UserAccount approverAccount = userAccountService.getUserBySSN(ssn);
        UserAccount requesteeAccount = userAccountService.getUserBySSN(requestTransaction.getSsn());
        if (approverAccount.getBalance() < requestTransaction.getAmount()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "no sufficient balance");
        }
        requestTransaction.setStatus(TransactionStatus.SUCCESS);
        requestTransactionRepo.save(requestTransaction);
        approverAccount.setBalance(approverAccount.getBalance() - requestTransaction.getAmount());
        requesteeAccount.setBalance(requesteeAccount.getBalance() + requestTransaction.getAmount());
        userAccountService.updateUserAccount(approverAccount);
        userAccountService.updateUserAccount(requesteeAccount);
    }

    public void cancelTransaction(int id) {
        RequestTransaction requestTransaction = requestTransactionRepo.getRequestTransactionId(id);
        requestTransaction.setStatus(TransactionStatus.CANCELLED);
        requestTransactionRepo.save(requestTransaction);
    }
}
