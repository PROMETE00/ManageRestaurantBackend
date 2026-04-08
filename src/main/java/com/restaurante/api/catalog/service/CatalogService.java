package com.restaurante.api.catalog.service;

import com.restaurante.api.catalog.dto.PublicMenuItem;
import com.restaurante.api.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<PublicMenuItem> getPublicMenu() {
        return productoRepository.findAll().stream()
                .map(producto -> new PublicMenuItem(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getCategoria() != null ? producto.getCategoria().getNombre() : "General",
                        producto.getTipo() != null ? producto.getTipo().getNombre() : "General",
                        producto.getRutaFoto(),
                        producto.getCalificacion(),
                        producto.getCantidad()
                ))
                .toList();
    }
}
