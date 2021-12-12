package edu.njit.wallet.controller;

import edu.njit.wallet.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("metrics")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @GetMapping("/by_range")
    public ResponseEntity<Map<String, Object>> getTotalSpendingByRange(@RequestParam String ssn, @RequestParam String from, @RequestParam String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(from);
        Date toDate = sdf.parse(to);
        Map<String, Object> result = metricsService.getTotalSpendingByRange(ssn, fromDate, toDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by_month")
    public ResponseEntity<Map<String, Object>> getTotalSpendingByMonth(@RequestParam String ssn, @RequestParam String month, @RequestParam String year) {
        return ResponseEntity.ok(metricsService.getTotalSpendingByMonth(ssn, Integer.parseInt(month), Integer.parseInt(year)));
    }

    @GetMapping("/max_by_month")
    public ResponseEntity<Map<String, Object>> getMaxSpendingByMonth(@RequestParam String ssn, @RequestParam String month, @RequestParam String year) {
        return ResponseEntity.ok(metricsService.getMaxSpendingByMonth(ssn, Integer.parseInt(month), Integer.parseInt(year)));
    }

    @GetMapping("/best_users")
    public ResponseEntity<List<Map<String, String>>> getBestUsers(@RequestParam String ssn) {
        return ResponseEntity.ok(metricsService.getBestUsers(ssn));
    }
}
