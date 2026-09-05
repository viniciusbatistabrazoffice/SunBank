package com.backend.repository;

import com.backend.entity.Auth;
import com.backend.entity.CriptoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CriptoAtivoRepository extends JpaRepository<CriptoAtivo, Long> {

    Optional<CriptoAtivo> findByUsuarioAndSimbolo(Auth usuario, String simbolo);

    List<CriptoAtivo> findByUsuario(Auth usuario);
}
