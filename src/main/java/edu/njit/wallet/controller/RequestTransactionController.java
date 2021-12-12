package edu.njit.wallet.controller;

import edu.njit.wallet.model.RequestTransaction;
import edu.njit.wallet.service.FromService;
import edu.njit.wallet.service.RequestTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("request-transaction")
public class RequestTransactionController {
    @Autowired
    private RequestTransactionService requestTransactionService;
    @Autowired
    private FromService fromService;

    @PostMapping
    public ResponseEntity<RequestTransaction> saveRequestTransaction(@RequestBody RequestTransaction requestTransaction){
        try{
            requestTransactionService.saveRequestTransaction(requestTransaction);
            fromService.saveFrom(requestTransaction.getIdentifier(),100);
            return ResponseEntity.status(HttpStatus.CREATED).body(requestTransaction);

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping
    public List<RequestTransaction> getRequestTransactionBySSN(@RequestParam String ssn) {
        List<RequestTransaction> requestTransactions = requestTransactionService.getRequestTransactionBySSN(ssn);
        Collections.sort(requestTransactions, (t1, t2) -> t2.getDateortime().compareTo(t1.getDateortime()));
        return requestTransactions;
    }

    @GetMapping("/approve")
    public ResponseEntity<String> approveTransaction(@RequestParam int id, @RequestParam String ssn) {
        requestTransactionService.approveTransaction(id, ssn);
        return ResponseEntity.ok().body("approved");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelTransaction(@RequestParam int id) {
        requestTransactionService.cancelTransaction(id);
        return ResponseEntity.ok().body("cancelled");
    }
}
