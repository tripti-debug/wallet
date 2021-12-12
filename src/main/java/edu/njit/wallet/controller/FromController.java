package edu.njit.wallet.controller;
import edu.njit.wallet.model.Fromm;
import edu.njit.wallet.service.FromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RequestMapping("from")
public class FromController {
    @Autowired
    private FromService fromService;
    @PostMapping
    public void saveFromDetails(String identifier, int percentage) {
        try {
            fromService.saveFrom(identifier,percentage);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
