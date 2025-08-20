package com.smartbin.controller;

import com.smartbin.dto.BinDTO;
import com.smartbin.dto.StatsDTO;
import com.smartbin.entity.Bin.BinStatus;
import com.smartbin.service.BinService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/smartbin")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class BinController {

    @Autowired
    private BinService binService;

    @GetMapping("/bins")
    public ResponseEntity<Map<String, Object>> getAllBins() {
        try {
            List<BinDTO> bins = binService.getAllBins();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", bins);
            response.put("total", bins.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching bins: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/bins/{id}")
    public ResponseEntity<Map<String, Object>> getBinById(@PathVariable Long id) {
        try {
            return binService.getBinById(id)
                    .map(bin -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("data", bin);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "Bin not found with id: " + id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    });
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching bin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/bins/search")
    public ResponseEntity<Map<String, Object>> searchBins(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long area,
            @RequestParam(required = false) BinStatus status,
            @RequestParam(required = false) Integer levelMin,
            @RequestParam(required = false) Integer levelMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            Page<BinDTO> binPage = binService.searchBins(q, area, status, levelMin, levelMax,
                    page, limit, sortBy, sortDir);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", binPage.getContent());
            response.put("total", binPage.getTotalElements());
            response.put("page", binPage.getNumber());
            response.put("size", binPage.getSize());
            response.put("totalPages", binPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error searching bins: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/bins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createBin(@Valid @RequestBody BinDTO binDTO) {
        try {
            BinDTO createdBin = binService.createBin(binDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", createdBin);
            response.put("message", "Bin created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating bin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/bins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateBin(@PathVariable Long id, @Valid @RequestBody BinDTO binDTO) {
        try {
            BinDTO updatedBin = binService.updateBin(id, binDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedBin);
            response.put("message", "Bin updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating bin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/bins/{id}/level")
    public ResponseEntity<Map<String, Object>> updateBinLevel(@PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer level = request.get("level");
            if (level == null || level < 0 || level > 100) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Level must be between 0 and 100");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            BinDTO updatedBin = binService.updateBinLevel(id, level);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedBin);
            response.put("message", "Bin level updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating bin level: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/bins/{id}/empty")
    public ResponseEntity<Map<String, Object>> markBinAsEmptied(@PathVariable Long id) {
        try {
            BinDTO updatedBin = binService.markBinAsEmptied(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedBin);
            response.put("message", "Bin marked as emptied successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error marking bin as emptied: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/bins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteBin(@PathVariable Long id) {
        try {
            binService.deleteBin(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Bin deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error deleting bin: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        try {
            StatsDTO stats = binService.getStats();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
