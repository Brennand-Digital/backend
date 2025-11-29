package com.brennandDigital.Projeto.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aviso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String descricao;

    private String titulo;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime date;

    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }
}

/*
{
    "descricao": "",
    "titulo": "",
    "date": ""
}
 */
