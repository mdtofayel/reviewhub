package com.reviewhub.api.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "job_logs")
public class JobLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private ScrapeJobEntity job;

    @Column(name = "ts", nullable = false)
    private Instant ts;

    @Column(name = "level", length = 16, nullable = false)
    private String level;

    @Column(name = "message", length = 2000, nullable = false)
    private String message;

    public Long getId() {
        return id;
    }

    public ScrapeJobEntity getJob() {
        return job;
    }

    public void setJob(ScrapeJobEntity job) {
        this.job = job;
    }

    public Instant getTs() {
        return ts;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
