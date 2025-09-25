package com.brennandDigital.Projeto.Repositories;

import com.brennandDigital.Projeto.Domain.Aviso.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
