package com.smartbin.controller;

import com.smartbin.dto.AreaDTO;
import com.smartbin.service.AreaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/smartbin")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/areas")
    public ResponseEntity<Map<String, Object>> getAllAreas() {
        try {
            List<AreaDTO> areas = areaService.getAllAreasWithBinCount();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", areas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching areas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<Map<String, Object>> getAreaById(@PathVariable Long id) {
        try {
            return areaService.getAreaById(id)
                    .map(area -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("data", area);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "Area not found with id: " + id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    });
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching area: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/areas/search")
    public ResponseEntity<Map<String, Object>> searchAreas(@RequestParam String name) {
        try {
            List<AreaDTO> areas = areaService.searchAreasByName(name);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", areas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error searching areas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/areas")
    public ResponseEntity<Map<String, Object>> createArea(@Valid @RequestBody AreaDTO areaDTO) {
        try {
            AreaDTO createdArea = areaService.createArea(areaDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", createdArea);
            response.put("message", "Area created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating area: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/areas/{id}")
    public ResponseEntity<Map<String, Object>> updateArea(@PathVariable Long id, @Valid @RequestBody AreaDTO areaDTO) {
        try {
            AreaDTO updatedArea = areaService.updateArea(id, areaDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedArea);
            response.put("message", "Area updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating area: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Map<String, Object>> deleteArea(@PathVariable Long id) {
        try {
            areaService.deleteArea(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Area deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error deleting area: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
