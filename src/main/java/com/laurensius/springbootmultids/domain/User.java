package com.laurensius.springbootmultids.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;

@Entity
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String fullname;
    @Column
    private Boolean active;
    @Column
    private Long createTimestamp;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    @Column
    private Instant createTime;
    //private OffsetDateTime time;
    @Column
    private Instant updateTime;
    @Column
    private LocalDate birthdate;
    @Column
    private String address;
    @Column
    private Double latitude;
    @Column
    private Double longitude;


}
