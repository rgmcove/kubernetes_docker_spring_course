package org.rgomez.springcloud.msvc.usuarios.repositories;

import org.rgomez.springcloud.msvc.usuarios.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
