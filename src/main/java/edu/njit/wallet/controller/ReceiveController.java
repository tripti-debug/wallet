package edu.njit.wallet.controller;

import edu.njit.wallet.model.HasAdditional;
import edu.njit.wallet.model.SendTransaction;
import edu.njit.wallet.model.Transaction;
import edu.njit.wallet.model.UserAccount;
import edu.njit.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("transaction")
public class ReceiveController {
    @Autowired
    private TransactionService transactionService;
    @GetMapping("byDate")
    public ResponseEntity<List<Transaction>> getResultByDate(@RequestBody Map<String, String> transaction){

        try {
              List<Transaction> transactionList = transactionService.viewTransactionByDate(transaction.get("ssn"),transaction.get("date"));
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionList);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping("byMonth")
    public Map<String,String> getResultByMonth(@RequestBody Map<String,String> requestBody){

        try {
            return transactionService.viewTransactionByMonth(requestBody.get("ssn"),requestBody.get("month"));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping("/received")
    public List<Map<String, Object>> getReceivedTransactions(@RequestParam String ssn) {
        return transactionService.getReceivedTransactions(ssn);
    }

}
