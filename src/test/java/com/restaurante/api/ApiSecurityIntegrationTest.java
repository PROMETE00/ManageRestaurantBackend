package com.restaurante.api;

import com.restaurante.api.model.Mesa;
import com.restaurante.api.model.Role;
import com.restaurante.api.model.Usuario;
import com.restaurante.api.repository.MesaRepository;
import com.restaurante.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ApiSecurityIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("restaurante")
            .withUsername("root")
            .withPassword("change-me");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("app.security.allowed-origins", () -> "http://localhost:3000,http://localhost:3001");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mesaRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setNombre("Admin QA");
        admin.setEmail("admin@example.com");
        admin.setPasswordHash(passwordEncoder.encode("SuperSecure123"));
        admin.setRole(Role.ADMIN);
        usuarioRepository.save(admin);

        Mesa mesa = new Mesa();
        mesa.setNumero(1);
        mesa.setCapacidad(4);
        mesa.setUbicacion("terraza");
        mesaRepository.save(mesa);
    }

    @Test
    void publicMenuShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/public/menu"))
                .andExpect(status().isOk());
    }

    @Test
    void adminEndpointsShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginShouldCreateHttpSession() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@example.com",
                                  "password": "SuperSecure123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JSESSIONID"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void publicReservationShouldReturnReference() throws Exception {
        mockMvc.perform(post("/api/public/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "María Torres",
                                  "email": "maria@example.com",
                                  "phone": "5551112233",
                                  "date": "2030-05-01",
                                  "time": "19:30:00",
                                  "partySize": 2,
                                  "tableId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reference").isString());
    }
}
