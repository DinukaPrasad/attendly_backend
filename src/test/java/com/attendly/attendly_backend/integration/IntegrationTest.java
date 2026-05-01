package com.attendly.attendly_backend.integration;

import com.attendly.attendly_backend.modules.attendance.repo.AttendanceRepository;
import com.attendly.attendly_backend.modules.programme.model.Programme;
import com.attendly.attendly_backend.modules.programme.repo.ProgrammeRepository;
import com.attendly.attendly_backend.modules.session.repo.SessionRepository;
import com.attendly.attendly_backend.modules.user.model.User;
import com.attendly.attendly_backend.modules.user.repo.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = AFTER_CLASS)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private static String lecturerToken;
    private static String studentToken;
    private static Long studentId;
    private static Long programmeId;

    @BeforeAll
    void setUp() {
        User lecturer = new User(null, "Test Lecturer", "LECTURER",
                "lecturer@integration.com", passwordEncoder.encode("Test1234!"));
        userRepository.save(lecturer);

        User student = new User(null, "Test Student", "STUDENT",
                "student@integration.com", passwordEncoder.encode("Test1234!"));
        User savedStudent = userRepository.save(student);
        studentId = savedStudent.getId();

        Programme programme = new Programme();
        programme.setCode("INT101");
        programme.setName("Integration Test Programme");
        programme.setDescription("Test");
        Programme savedProgramme = programmeRepository.save(programme);
        programmeId = savedProgramme.getId();
    }

    @Test
    @Order(1)
    void IT01_EndToEndLoginFlow() throws Exception {
        Map<String, String> lecturerLogin = new HashMap<>();
        lecturerLogin.put("email", "lecturer@integration.com");
        lecturerLogin.put("password", "Test1234!");

        MvcResult lecturerResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturerLogin)))
                .andReturn();

        String lecturerBody = lecturerResult.getResponse().getContentAsString();
        JsonNode lecturerJson = objectMapper.readTree(lecturerBody);
        lecturerToken = lecturerJson.path("data").path("token").asText();

        Map<String, String> studentLogin = new HashMap<>();
        studentLogin.put("email", "student@integration.com");
        studentLogin.put("password", "Test1234!");

        MvcResult studentResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentLogin)))
                .andReturn();

        String studentBody = studentResult.getResponse().getContentAsString();
        JsonNode studentJson = objectMapper.readTree(studentBody);
        studentToken = studentJson.path("data").path("token").asText();

        assertNotNull(lecturerToken);
        assertFalse(lecturerToken.isEmpty());
        assertNotNull(studentToken);
        assertFalse(studentToken.isEmpty());
        assertTrue(lecturerJson.path("data").has("role"));

        System.out.println("IT-01 End-to-end login flow PASS ✅");
    }

    @Test
    @Order(2)
    void IT02_EndToEndSessionCreation() throws Exception {
        Map<String, Object> sessionBody = new HashMap<>();
        sessionBody.put("programmeId", programmeId);
        sessionBody.put("module", "INT101");
        sessionBody.put("lecturer", "Prof Test");
        sessionBody.put("title", "Integration Test Session");
        sessionBody.put("description", "Test session");
        sessionBody.put("startTime", "2026-05-01T09:00:00");
        sessionBody.put("endTime", "2026-05-01T10:30:00");
        sessionBody.put("sessionStatus", "ONGOING");
        sessionBody.put("code", "INT101-IT02");
        sessionBody.put("venue", "Room IT");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + lecturerToken)
                        .content(objectMapper.writeValueAsString(sessionBody)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(json.path("success").asBoolean());
        assertTrue(sessionRepository.count() > 0);

        System.out.println("IT-02 End-to-end session creation PASS ✅");
    }

    @Test
    @Order(3)
    void IT03_EndToEndAttendanceMarking() throws Exception {
        Map<String, Object> attendanceBody = new HashMap<>();
        attendanceBody.put("userId", studentId);
        attendanceBody.put("programmeId", programmeId);
        attendanceBody.put("time", "2026-05-01T09:05:00");
        attendanceBody.put("status", "PRESENT");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + studentToken)
                        .content(objectMapper.writeValueAsString(attendanceBody)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(json.path("success").asBoolean());
        assertTrue(json.path("data").has("userName"));
        assertTrue(json.path("data").has("programmeName"));
        assertTrue(attendanceRepository.count() > 0);

        System.out.println("IT-03 End-to-end attendance marking PASS ✅");
    }

    @Test
    @Order(4)
    void IT04_ApiResponseWrapperConsistency() throws Exception {
        MvcResult healthResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andReturn();
        assertEquals(200, healthResult.getResponse().getStatus());
        JsonNode healthJson = objectMapper.readTree(healthResult.getResponse().getContentAsString());
        assertTrue(healthJson.has("timestamp"));

        MvcResult sessionsResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/sessions")
                        .header("Authorization", "Bearer " + lecturerToken))
                .andReturn();
        assertEquals(200, sessionsResult.getResponse().getStatus());
        JsonNode sessionsJson = objectMapper.readTree(sessionsResult.getResponse().getContentAsString());
        assertTrue(sessionsJson.has("success"));
        assertTrue(sessionsJson.has("timestamp"));

        MvcResult programmesResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/programmes")
                        .header("Authorization", "Bearer " + lecturerToken))
                .andReturn();
        assertEquals(200, programmesResult.getResponse().getStatus());
        JsonNode programmesJson = objectMapper.readTree(programmesResult.getResponse().getContentAsString());
        assertTrue(programmesJson.has("success"));
        assertTrue(programmesJson.has("timestamp"));

        MvcResult attendanceResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/attendance")
                        .header("Authorization", "Bearer " + lecturerToken))
                .andReturn();
        assertEquals(200, attendanceResult.getResponse().getStatus());
        JsonNode attendanceJson = objectMapper.readTree(attendanceResult.getResponse().getContentAsString());
        assertTrue(attendanceJson.has("success"));
        assertTrue(attendanceJson.has("timestamp"));

        System.out.println("IT-04 ApiResponse wrapper consistency PASS ✅");
    }

    @Test
    @Order(5)
    void IT05_DatabasePersistenceVerification() throws Exception {
        assertDoesNotThrow(() -> {
            User dbTestUser = new User(null, "DB Test User", "STUDENT",
                    "dbtest@integration.com", passwordEncoder.encode("Test1234!"));
            userRepository.save(dbTestUser);

            Optional<User> found = userRepository.findByEmail("dbtest@integration.com");
            assertTrue(found.isPresent());
            assertEquals("DB Test User", found.get().getName());
            assertEquals("STUDENT", found.get().getRole());

            userRepository.delete(found.get());
        });

        System.out.println("IT-05 Database persistence verification PASS ✅");
    }

    @Test
    @Order(6)
    void IT06_JwtTokenCrossRequestPersistence() throws Exception {
        MvcResult sessionsResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/sessions")
                        .header("Authorization", "Bearer " + studentToken))
                .andReturn();
        assertEquals(200, sessionsResult.getResponse().getStatus());

        MvcResult programmesResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/programmes")
                        .header("Authorization", "Bearer " + studentToken))
                .andReturn();
        assertEquals(200, programmesResult.getResponse().getStatus());

        MvcResult userAttendanceResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/attendance/user/" + studentId)
                                .header("Authorization", "Bearer " + studentToken))
                .andReturn();
        assertEquals(200, userAttendanceResult.getResponse().getStatus());

        System.out.println("IT-06 JWT token cross-request persistence PASS ✅");
    }
}
