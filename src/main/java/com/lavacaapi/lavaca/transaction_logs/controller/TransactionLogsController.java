package com.lavacaapi.lavaca.transaction_logs.controller;

import com.lavacaapi.lavaca.transaction_logs.TransactionLogs;
import com.lavacaapi.lavaca.transaction_logs.service.TransactionLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction-logs")
public class TransactionLogsController {

    @Autowired
    private TransactionLogsService transactionLogsService;

    @GetMapping
    public ResponseEntity<List<TransactionLogs>> getAllTransactionLogs() {
        return ResponseEntity.ok(transactionLogsService.getAllTransactionLogs());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<TransactionLogs>> getAllTransactionLogsPaginated(Pageable pageable) {
        return ResponseEntity.ok(transactionLogsService.getAllTransactionLogsPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionLogs> getTransactionLogById(@PathVariable UUID id) {
        Optional<TransactionLogs> transactionLog = transactionLogsService.getTransactionLogById(id);
        return transactionLog.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<TransactionLogs>> getTransactionLogsByTransaction(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(transactionLogsService.getTransactionLogsByTransactionId(transactionId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionLogs>> getTransactionLogsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionLogsService.getTransactionLogsByUserId(userId));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<TransactionLogs>> getTransactionLogsByAction(@PathVariable String action) {
        return ResponseEntity.ok(transactionLogsService.getTransactionLogsByAction(action));
    }

    @GetMapping("/actions")
    public ResponseEntity<List<TransactionLogs>> getTransactionLogsByMultipleActions(@RequestParam List<String> actions) {
        return ResponseEntity.ok(transactionLogsService.getLogsByMultipleActions(actions));
    }

    @GetMapping("/after")
    public ResponseEntity<List<TransactionLogs>> getTransactionLogsAfterDate(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        // Convertir Date a Timestamp
        Timestamp timestamp = new Timestamp(date.getTime());
        return ResponseEntity.ok(transactionLogsService.getTransactionLogsAfterTimestamp(timestamp));
    }

    @GetMapping("/between")
    public ResponseEntity<List<TransactionLogs>> getTransactionLogsBetweenDates(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        // Convertir Dates a Timestamps
        Timestamp start = new Timestamp(startDate.getTime());
        Timestamp end = new Timestamp(endDate.getTime());
        return ResponseEntity.ok(transactionLogsService.getLogsBetweenDates(start, end));
    }

    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<TransactionLogs>> getRecentLogs(@PathVariable int limit) {
        return ResponseEntity.ok(transactionLogsService.getRecentLogs(limit));
    }

    @GetMapping("/count/action/{action}")
    public ResponseEntity<Map<String, Long>> countLogsByAction(@PathVariable String action) {
        long count = transactionLogsService.countLogsByAction(action);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/transaction/{transactionId}")
    public ResponseEntity<Map<String, Long>> countLogsByTransaction(@PathVariable UUID transactionId) {
        long count = transactionLogsService.countLogsByTransaction(transactionId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionLogs> createTransactionLog(@RequestBody TransactionLogs transactionLog) {
        return new ResponseEntity<>(transactionLogsService.createTransactionLog(transactionLog), HttpStatus.CREATED);
    }

    @PostMapping("/create-log")
    public ResponseEntity<TransactionLogs> logCreate(
            @RequestParam UUID transactionId,
            @RequestParam UUID userId,
            @RequestParam String newData) {
        return new ResponseEntity<>(
                transactionLogsService.logCreate(transactionId, userId, newData),
                HttpStatus.CREATED);
    }

    @PostMapping("/update-log")
    public ResponseEntity<TransactionLogs> logUpdate(
            @RequestParam UUID transactionId,
            @RequestParam UUID userId,
            @RequestParam String oldData,
            @RequestParam String newData) {
        return new ResponseEntity<>(
                transactionLogsService.logUpdate(transactionId, userId, oldData, newData),
                HttpStatus.CREATED);
    }

    @PostMapping("/delete-log")
    public ResponseEntity<TransactionLogs> logDelete(
            @RequestParam UUID transactionId,
            @RequestParam UUID userId,
            @RequestParam String oldData) {
        return new ResponseEntity<>(
                transactionLogsService.logDelete(transactionId, userId, oldData),
                HttpStatus.CREATED);
    }

    @PostMapping("/approval-log")
    public ResponseEntity<TransactionLogs> logApproval(
            @RequestParam UUID transactionId,
            @RequestParam UUID userId,
            @RequestParam String oldData,
            @RequestParam String newData) {
        return new ResponseEntity<>(
                transactionLogsService.logApproval(transactionId, userId, oldData, newData),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable UUID id) {
        transactionLogsService.deleteTransactionLog(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, Integer>> deleteOldLogs(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date before) {
        // Convertir Date a Timestamp
        Timestamp timestamp = new Timestamp(before.getTime());
        int deletedCount = transactionLogsService.deleteOldLogs(timestamp);

        Map<String, Integer> response = new HashMap<>();
        response.put("deletedCount", deletedCount);
        return ResponseEntity.ok(response);
    }
}
