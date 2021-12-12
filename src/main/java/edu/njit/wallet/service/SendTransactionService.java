package edu.njit.wallet.service;
import edu.njit.wallet.Repository.RequestTransactionRepo;
import edu.njit.wallet.Repository.SendTransactionRepo;
import edu.njit.wallet.Repository.ReceiveRepo;
import edu.njit.wallet.Repository.UserAccountRepo;
import edu.njit.wallet.model.SendTransaction;
import edu.njit.wallet.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class SendTransactionService {
    @Autowired
    private SendTransactionRepo sendTransactionRepo;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ReceiveRepo receiveRepo;
    @Autowired
    private UserAccountRepo userAccountRepo;
    public void saveSendTransactionDetails(SendTransaction sendTransaction){
        try{
            sendTransactionRepo.saveSendDetails(new Date(),sendTransaction.getMemo(),sendTransaction.getCancel_reason(),sendTransaction.getIdentifier(),sendTransaction.getSsn(),sendTransaction.getAmount(), 0.0, TransactionStatus.PENDING.name());
        }catch(Exception e){
            throw e;
        }
    }

    public List<SendTransaction> getAllSendTransaction(String ssn) {
        return sendTransactionRepo.findBySSN(ssn);
    }

    public void cancelTransaction(int id, String cancelReason) {
        SendTransaction sendTransaction = sendTransactionRepo.getSendTransactionId(id);
        if (!sendTransaction.getStatus().equals(TransactionStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "transaction already executed");
        }
        sendTransaction.setStatus(TransactionStatus.CANCELLED);
        sendTransaction.setCancel_reason(cancelReason);
        sendTransactionRepo.save(sendTransaction);
    }

    public List<SendTransaction> getAllSendTransaction(TransactionStatus status) {
        return sendTransactionRepo.getAllByStatus(status.name());
    }

    public void saveSendTransactionDetailsWithRepo(SendTransaction sendTransaction) {
        sendTransactionRepo.save(sendTransaction);
    }

    public List<SendTransaction> getAllSendTransaction(List<String> identifiers) {
        return sendTransactionRepo.getAllByIdentifier(identifiers);
    }
}
