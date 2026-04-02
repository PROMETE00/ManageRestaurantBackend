# 🍽️ Restaurant Backend API

API REST para gestión de restaurante con Spring Boot + MySQL + Cloudflare Tunnel

## 🚀 Despliegue Rápido

### 1️⃣ Clonar o hacer pull del proyecto

```bash
git pull origin main
```

### 2️⃣ Configurar variables de entorno

```bash
# Copiar plantilla
cp .env.example .env

# Editar y pegar tu token de Cloudflare
nano .env
```

**Configurar en `.env`:**
```env
CLOUDFLARED_TOKEN_BACKEND=tu_token_aqui
DB_PASSWORD=tu_contraseña_segura
```

### 3️⃣ Levantar servicios

```bash
docker-compose up -d --build
```

### 4️⃣ Verificar estado

```bash
# Ver logs
docker-compose logs -f

# Verificar contenedores
docker ps

# Probar API localmente
curl http://localhost:8081/api/health
```

## 🌐 Acceso Público

- **Backend API:** https://restaurantebackend.prome.works
- **Puerto local:** http://localhost:8081

## 📦 Stack Tecnológico

- **Java 21** + Spring Boot
- **MySQL 8.0**
- **Docker** + Docker Compose
- **Cloudflare Tunnel** (Zero Trust)

## 🛠️ Comandos Útiles

```bash
# Detener servicios
docker-compose down

# Ver logs de un servicio específico
docker-compose logs -f backend
docker-compose logs -f db

# Reconstruir solo el backend
docker-compose up -d --build backend

# Limpiar volúmenes (¡CUIDADO! Borra la DB)
docker-compose down -v
```

## 🔐 Seguridad

- **NO** commitear el archivo `.env` (ya está en `.gitignore`)
- Tokens de Cloudflare se gestionan vía variables de entorno
- Contraseñas de DB solo en `.env`

## 📚 Endpoints Principales

```
GET  /api/productos        - Listar productos
GET  /api/mesas           - Listar mesas
GET  /api/pedidos         - Listar pedidos
POST /api/pedidos         - Crear pedido
GET  /api/meseros         - Listar meseros
```

## 🐛 Troubleshooting

**Problema:** Tunnel no conecta
```bash
# Ver logs del tunnel
docker logs tunnel_backend

# Verificar token
echo $CLOUDFLARED_TOKEN_BACKEND
```

**Problema:** Base de datos no inicia
```bash
# Ver logs de MySQL
docker-compose logs db

# Verificar health check
docker inspect restaurant_db | grep Health -A 10
```

**Problema:** Backend no puede conectar a DB
```bash
# Verificar red interna
docker network inspect restaurante_backend_default

# Esperar a que DB esté healthy
docker-compose up -d
```
