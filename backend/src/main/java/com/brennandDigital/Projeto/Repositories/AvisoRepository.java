package com.brennandDigital.Projeto.Repositories;
import com.brennandDigital.Projeto.Domain.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, String> {
}
