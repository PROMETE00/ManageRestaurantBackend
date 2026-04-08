package com.restaurante.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(
        name = "mesa",
        uniqueConstraints = @UniqueConstraint(name = "uk_mesa_numero", columnNames = "numero")
)
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer numero;
    private Integer capacidad;

    @Column(length = 100, nullable = false)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('libre','reservada','ocupada','atendida')")
    private EstadoMesaEnum estado = EstadoMesaEnum.libre;

    @ManyToOne
    @JoinColumn(name = "mesero_id", foreignKey = @ForeignKey(name = "fk_mesa_mesero"), nullable = true)
    private Mesero mesero;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public String getEstadoDescripcion() {
        return (estado != null) ? estado.name().toLowerCase() : "libre";
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
