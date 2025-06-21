package com.restaurante.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Producto {

    /* ---------- PK ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* ---------- Datos básicos ---------- */
    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(name = "ruta_foto", length = 255)          //  <<< NUEVO
    private String rutaFoto;                           //  camelCase habitual en Java

    /* ---------- Relaciones ---------- */
    /** FK a tipo_producto.id */
    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoProducto tipo;

    /** FK a categoria_producto.id */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaProducto categoria;

    /* ---------- Datos numéricos ---------- */
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    /** Stock disponible (por defecto 0 en BD) */
    private Integer cantidad;

    /** Calificación promedio 0-10 con dos decimales (ej. 8 .75) */
    @Column(precision = 3, scale = 2)
    private BigDecimal calificacion;
}
