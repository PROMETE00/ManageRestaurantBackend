package com.restaurante.api.catalog.controller;

import com.restaurante.api.catalog.dto.PublicMenuItem;
import com.restaurante.api.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/menu")
@RequiredArgsConstructor
public class PublicCatalogController {

    private final CatalogService catalogService;

    @GetMapping
    public List<PublicMenuItem> getMenu() {
        return catalogService.getPublicMenu();
    }
}
