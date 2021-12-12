package edu.njit.wallet.jobs;

import edu.njit.wallet.model.SendTransaction;
import edu.njit.wallet.model.TransactionStatus;
import edu.njit.wallet.model.UserAccount;
import edu.njit.wallet.service.SendTransactionService;
import edu.njit.wallet.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class AutomaticSendClearingJob {

    private SendTransactionService sendTransactionService;
    private UserAccountService userAccountService;

    @Autowired
    public AutomaticSendClearingJob(SendTransactionService sendTransactionService, UserAccountService userAccountService) {
        this.sendTransactionService = sendTransactionService;
        this.userAccountService = userAccountService;
    }

    @Scheduled(fixedRate = 60000)
    public void clearPendingSendTransactions() {
        System.out.println("clear pending transactions started -- " + new Date());
        List<SendTransaction> sendTransactions = sendTransactionService
                .getAllSendTransaction(TransactionStatus.PENDING)
                .stream()
                .filter(item -> item.getDateortime().before(new Date(System.currentTimeMillis() - 60000)))
                .collect(Collectors.toList());
        sendTransactions.forEach(sendTransaction -> {
            UserAccount senderAccount = userAccountService.getUserBySSN(sendTransaction.getSsn());
            UserAccount receiverAccount = userAccountService.getUserByIdentifier(sendTransaction.getIdentifier());
            if(senderAccount.getBalance() < sendTransaction.getAmount()) {
                sendTransaction.setCancel_reason("insufficient balance");
                sendTransaction.setStatus(TransactionStatus.REJECTED);
                sendTransactionService.saveSendTransactionDetailsWithRepo(sendTransaction);
            } else {
                sendTransaction.setStatus(TransactionStatus.SUCCESS);
                sendTransactionService.saveSendTransactionDetailsWithRepo(sendTransaction);
                senderAccount.setBalance(senderAccount.getBalance() - sendTransaction.getAmount());
                receiverAccount.setBalance(receiverAccount.getBalance() + sendTransaction.getAmount());
                userAccountService.updateUserAccount(senderAccount);
                userAccountService.updateUserAccount(receiverAccount);
            }
        });
        System.out.println("clear pending transactions finished -- " + new Date());
    }
}
