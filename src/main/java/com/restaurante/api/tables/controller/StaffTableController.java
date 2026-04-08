package com.restaurante.api.tables.controller;

import com.restaurante.api.tables.dto.AddTableItemRequest;
import com.restaurante.api.tables.dto.TableSummary;
import com.restaurante.api.tables.dto.UpdateTableStatusRequest;
import com.restaurante.api.tables.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff/tables")
@RequiredArgsConstructor
public class StaffTableController {

    private final TableService tableService;

    @GetMapping
    public List<TableSummary> listTables() {
        return tableService.listTables();
    }

    @PatchMapping("/{tableId}/status")
    public TableSummary updateStatus(
            @PathVariable Integer tableId,
            @Valid @RequestBody UpdateTableStatusRequest request
    ) {
        return tableService.updateStatus(tableId, request);
    }

    @PostMapping("/{tableId}/items")
    public TableSummary addItem(
            @PathVariable Integer tableId,
            @Valid @RequestBody AddTableItemRequest request
    ) {
        return tableService.addItem(tableId, request);
    }
}
