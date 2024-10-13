package org.rgomez.springcloud.msvc.cursos.services;

import org.rgomez.springcloud.msvc.cursos.models.Usuario;
import org.rgomez.springcloud.msvc.cursos.models.entities.Curso;

import java.util.List;
import java.util.Optional;

public interface ICursoService {

    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);
    void eliminarCursoUsuarioPorId(Long id);
    Optional<Curso> porIdConUsuarios(Long id, String token);
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId, String token);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId, String token);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId, String token);
}
