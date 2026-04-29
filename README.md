# Franchise Management API

API REST reactiva para gestión de franquicias, sucursales y productos.

**Stack:** Java 21 · Spring Boot 3.5 · WebFlux · MongoDB · Docker

---

## Requisitos previos

- Docker y Docker Compose
- (Opcional local) Java 21 + Maven 3.9 + MongoDB 7 corriendo en `localhost:27017`

---

## Levantar con Docker Compose

```bash
docker-compose up --build
```

La API queda disponible en `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Levantar en local sin Docker

```bash
# Requiere MongoDB corriendo en localhost:27017
./mvnw spring-boot:run
```

Para apuntar a otra URI de MongoDB:

```bash
SPRING_DATA_MONGODB_URI=mongodb://host:27017/franchisedb ./mvnw spring-boot:run
```

---

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SPRING_DATA_MONGODB_URI` | `mongodb://localhost:27017/franchisedb` | URI de conexión a MongoDB |

---

## Endpoints

### Franquicias
| Método | Path | Descripción |
|---|---|---|
| `POST` | `/api/franchises` | Crear franquicia |
| `POST` | `/api/franchises/{franchiseId}/branches` | Agregar sucursal |
| `PATCH` | `/api/franchises/{id}/name` | Actualizar nombre |
| `GET` | `/api/franchises/{franchiseId}/top-stock` | Producto con más stock por sucursal |

### Sucursales
| Método | Path | Descripción |
|---|---|---|
| `POST` | `/api/branches/{branchId}/products` | Agregar producto |
| `DELETE` | `/api/branches/{branchId}/products/{productId}` | Eliminar producto |
| `PATCH` | `/api/branches/{id}/name` | Actualizar nombre |

### Productos
| Método | Path | Descripción |
|---|---|---|
| `PATCH` | `/api/products/{id}/stock` | Modificar stock |
| `PATCH` | `/api/products/{id}/name` | Actualizar nombre |

Documentación completa disponible en Swagger UI una vez levantada la app.

---

## Modelo de datos

```
Franchise
  ├── id
  ├── name
  └── branches[]
        ├── id
        ├── name
        └── products[]
              ├── id
              ├── name
              └── stock
```

---

## Git Flow

```
main  ← releases estables
└── develop
    ├── feature/franchise-crud
    ├── feature/branch-crud
    ├── feature/product-crud
    ├── feature/top-stock-endpoint
    └── feature/docker
```
