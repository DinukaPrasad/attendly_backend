package com.attendly.attendly_backend.modules.session.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data // @Getter + @Setter + @ToString + @EqualsAndHashCode
@NoArgsConstructor // JPA needs this! ⚠️
@AllArgsConstructor // All fields constructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String module;
    private String lecturer;
    private String title;
    private String description;
    private String venue;
    private String startTime;
    private String endTime;
    private String sessionStatus;
    private boolean attendanceStatus;
    private String code;

    @CreationTimestamp // ✅ Auto set when created
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // ✅ Auto set when updated
    private LocalDateTime updatedAt;
}
