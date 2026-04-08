CREATE TABLE IF NOT EXISTS categoria_producto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS tipo_producto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS ingrediente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    cantidad DOUBLE NULL,
    unidad VARCHAR(45) NULL
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NULL,
    email VARCHAR(150) NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS mesero (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    sexo ENUM('M', 'F') NULL,
    fecha_nacimiento DATE NULL,
    turno VARCHAR(50) NULL,
    salario DOUBLE NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STAFF', 'CUSTOMER') NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS mesa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero INT NOT NULL,
    capacidad INT NULL,
    ubicacion VARCHAR(100) NOT NULL,
    estado ENUM('libre', 'reservada', 'ocupada', 'atendida') NOT NULL DEFAULT 'libre',
    mesero_id INT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT uk_mesa_numero UNIQUE (numero),
    CONSTRAINT fk_mesa_mesero FOREIGN KEY (mesero_id) REFERENCES mesero (id)
);

CREATE TABLE IF NOT EXISTS producto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    ruta_foto VARCHAR(255) NULL,
    tipo_id INT NULL,
    categoria_id INT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NULL DEFAULT 0,
    calificacion DECIMAL(3,2) NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_producto_tipo FOREIGN KEY (tipo_id) REFERENCES tipo_producto (id),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_producto (id)
);

CREATE TABLE IF NOT EXISTS producto_ingrediente (
    id_producto_ingrediente INT PRIMARY KEY AUTO_INCREMENT,
    cantidad_utilizada DOUBLE NULL,
    unidad VARCHAR(45) NULL,
    producto_id INT NULL,
    ingrediente_id INT NULL,
    CONSTRAINT fk_producto_ingrediente_producto FOREIGN KEY (producto_id) REFERENCES producto (id),
    CONSTRAINT fk_producto_ingrediente_ingrediente FOREIGN KEY (ingrediente_id) REFERENCES ingrediente (id)
);

CREATE TABLE IF NOT EXISTS reserva (
    id INT PRIMARY KEY AUTO_INCREMENT,
    public_id VARCHAR(36) NOT NULL,
    fecha DATE NULL,
    hora TIME NULL,
    cantidad INT NULL,
    cliente_id INT NULL,
    mesa_id INT NULL,
    mesero_id INT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT uk_reserva_public_id UNIQUE (public_id),
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    CONSTRAINT fk_reserva_mesa FOREIGN KEY (mesa_id) REFERENCES mesa (id),
    CONSTRAINT fk_reserva_mesero FOREIGN KEY (mesero_id) REFERENCES mesero (id)
);

CREATE INDEX idx_reserva_mesa_fecha_hora ON reserva (mesa_id, fecha, hora);

CREATE TABLE IF NOT EXISTS pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATETIME NULL,
    hora TIME NULL,
    total DECIMAL(10,2) NULL,
    estatus ENUM('pagado', 'pendiente', 'cancelado') NOT NULL DEFAULT 'pendiente',
    cliente_id INT NULL,
    reservacion_id INT NULL,
    mesa_id INT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    CONSTRAINT fk_pedido_reserva FOREIGN KEY (reservacion_id) REFERENCES reserva (id),
    CONSTRAINT fk_pedido_mesa FOREIGN KEY (mesa_id) REFERENCES mesa (id)
);

CREATE TABLE IF NOT EXISTS pedido_producto (
    id_pedido_producto VARCHAR(45) PRIMARY KEY,
    cantidad INT NULL,
    precio_unitario DOUBLE NULL,
    subtotal DOUBLE NULL,
    pedido_id INT NULL,
    producto_id INT NULL,
    CONSTRAINT fk_pedido_producto_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id),
    CONSTRAINT fk_pedido_producto_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);

CREATE TABLE IF NOT EXISTS mesa_mesero (
    id INT PRIMARY KEY AUTO_INCREMENT,
    mesa_id INT NOT NULL,
    mesero_id INT NOT NULL,
    asignado_en DATETIME NOT NULL,
    CONSTRAINT uk_mesa_mesero UNIQUE (mesa_id, mesero_id),
    CONSTRAINT fk_mesa_mesero_mesa FOREIGN KEY (mesa_id) REFERENCES mesa (id),
    CONSTRAINT fk_mesa_mesero_mesero FOREIGN KEY (mesero_id) REFERENCES mesero (id)
);

CREATE TABLE IF NOT EXISTS menu_mesa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    mesa_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    CONSTRAINT uk_menu_mesa UNIQUE (mesa_id, producto_id),
    CONSTRAINT fk_menu_mesa_mesa FOREIGN KEY (mesa_id) REFERENCES mesa (id),
    CONSTRAINT fk_menu_mesa_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);
