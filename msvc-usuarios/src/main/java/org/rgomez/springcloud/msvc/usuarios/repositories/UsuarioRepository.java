package org.rgomez.springcloud.msvc.usuarios.repositories;

import org.rgomez.springcloud.msvc.usuarios.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
