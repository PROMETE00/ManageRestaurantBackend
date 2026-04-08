package com.restaurante.api.tables.service;

import com.restaurante.api.common.exception.ConflictException;
import com.restaurante.api.common.exception.ResourceNotFoundException;
import com.restaurante.api.model.EstadoMesaEnum;
import com.restaurante.api.model.MenuMesa;
import com.restaurante.api.model.Mesa;
import com.restaurante.api.model.Producto;
import com.restaurante.api.repository.MenuMesaRepository;
import com.restaurante.api.repository.MesaRepository;
import com.restaurante.api.repository.ProductoRepository;
import com.restaurante.api.tables.dto.AddTableItemRequest;
import com.restaurante.api.tables.dto.TableSummary;
import com.restaurante.api.tables.dto.UpdateTableStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final MesaRepository mesaRepository;
    private final MenuMesaRepository menuMesaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<TableSummary> listTables() {
        return mesaRepository.findAllByOrderByNumeroAsc().stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public TableSummary updateStatus(Integer tableId, UpdateTableStatusRequest request) {
        Mesa mesa = mesaRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        mesa.setEstado(parseState(request.state()));
        return toSummary(mesaRepository.save(mesa));
    }

    @Transactional
    public TableSummary addItem(Integer tableId, AddTableItemRequest request) {
        Mesa mesa = mesaRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        Producto producto = productoRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (producto.getCantidad() != null && producto.getCantidad() < request.quantity()) {
            throw new ConflictException("Not enough stock available");
        }

        MenuMesa item = menuMesaRepository.findFirstByMesaIdAndProductoId(tableId, request.productId())
                .orElseGet(() -> {
                    MenuMesa menuMesa = new MenuMesa();
                    menuMesa.setMesa(mesa);
                    menuMesa.setProducto(producto);
                    menuMesa.setCantidad(0);
                    return menuMesa;
                });

        item.setCantidad(item.getCantidad() + request.quantity());
        menuMesaRepository.save(item);

        if (producto.getCantidad() != null) {
            producto.setCantidad(producto.getCantidad() - request.quantity());
            productoRepository.save(producto);
        }

        if (mesa.getEstado() == EstadoMesaEnum.libre || mesa.getEstado() == EstadoMesaEnum.reservada) {
            mesa.setEstado(EstadoMesaEnum.ocupada);
            mesaRepository.save(mesa);
        }

        return toSummary(mesa);
    }

    private TableSummary toSummary(Mesa mesa) {
        List<MenuMesa> items = menuMesaRepository.findByMesaId(mesa.getId());
        int itemCount = items.stream()
                .map(MenuMesa::getCantidad)
                .reduce(0, Integer::sum);
        BigDecimal total = items.stream()
                .map(item -> item.getProducto().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TableSummary(
                mesa.getId(),
                mesa.getNumero(),
                mesa.getCapacidad(),
                mesa.getUbicacion(),
                mesa.getEstado().name(),
                mesa.getMesero() != null ? mesa.getMesero().getNombre() : null,
                itemCount,
                total
        );
    }

    private EstadoMesaEnum parseState(String rawState) {
        try {
            return EstadoMesaEnum.valueOf(rawState.trim().toLowerCase());
        } catch (IllegalArgumentException exception) {
            throw new ResourceNotFoundException("Unknown table state");
        }
    }
}
