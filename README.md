# Back Stack

Stack de backend del proyecto. Aquí vive la API Spring Boot, la base de datos y la configuración del modelo de datos.

## Estructura

```text
back/
├── src/                 # Código Java y recursos
├── pom.xml              # Dependencias Maven
├── Dockerfile           # Imagen de la API
├── docker-compose.yml   # Stack backend (API + MySQL)
├── .env.example
└── web2.sql             # Referencia histórica del esquema previo
```

## Contexto del stack

- API Spring Boot 3.5
- Seguridad con sesión, roles y CORS restringido
- Migraciones con Flyway
- Persistencia MySQL
- Endpoints separados en:
  - `/api/auth`
  - `/api/admin`
  - `/api/staff`
  - `/api/public`

## Ejecutar con Docker

```bash
cd /home/prome/restaurante/back
docker compose up --build
```

Puertos:

- API: `http://localhost:8081`
- MySQL: `3306`

Variables principales:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `APP_ALLOWED_ORIGINS`
- `BOOTSTRAP_ADMIN_NAME`
- `BOOTSTRAP_ADMIN_EMAIL`
- `BOOTSTRAP_ADMIN_PASSWORD`

## Ejecutar en local

```bash
cd /home/prome/restaurante/back
./mvnw spring-boot:run
```

## Notas

- Para compilar o correr tests hace falta un JDK completo con `javac`.
- El archivo `web2.sql` ya no es la fuente activa del esquema; la base actual vive en `src/main/resources/db/migration`.
