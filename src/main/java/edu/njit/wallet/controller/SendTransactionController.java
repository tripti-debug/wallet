package edu.njit.wallet.controller;

import edu.njit.wallet.model.SendTransaction;
import edu.njit.wallet.service.SendTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("send-transaction")
public class SendTransactionController {
    @Autowired
    private SendTransactionService sendTransactionService;
    @PostMapping
    public ResponseEntity<SendTransaction> saveSendTransaction(@RequestBody SendTransaction sendTransaction){
        try{
            sendTransactionService.saveSendTransactionDetails(sendTransaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(sendTransaction);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping
    public List<SendTransaction> getAllSendTransaction(@RequestParam String ssn) {
        List<SendTransaction> transactions = sendTransactionService.getAllSendTransaction(ssn);
        Collections.sort(transactions, (t1, t2) -> t2.getDateortime().compareTo(t1.getDateortime()));
        return transactions;
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelTransaction(@RequestParam int id, @RequestParam String cancelReason) {
        sendTransactionService.cancelTransaction(id, cancelReason);
        return (ResponseEntity) ResponseEntity.ok().body("Successfully cancelled");
    }
}
