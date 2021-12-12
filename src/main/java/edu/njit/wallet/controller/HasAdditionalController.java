package edu.njit.wallet.controller;

import edu.njit.wallet.model.HasAdditional;
import edu.njit.wallet.service.HasAdditionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("has-additional")
public class HasAdditionalController {
    @Autowired
    private HasAdditionalService hasAdditionalService;
    @PostMapping
    public ResponseEntity<HasAdditional> saveHasAdditional(@RequestBody HasAdditional hasAdditional){
        try{
            hasAdditionalService.saveHasAdditional(hasAdditional);
            return ResponseEntity.status(HttpStatus.CREATED).body(hasAdditional);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping
    @RequestMapping("/{ssn}")
    public ResponseEntity<List<HasAdditional>> getHasAdditional(@PathVariable String ssn) {
        return ResponseEntity.ok().body(hasAdditionalService.getAdditionalBanks(ssn));
    }

}
