package org.rgomez.springcloud.msvc.cursos.repositories;

import org.rgomez.springcloud.msvc.cursos.models.entities.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {

    @Modifying
    @Query("delete from CursoUsuario cu where cu.usuarioId =:id")
    void eliminarCursoUsuarioPorId(Long id);

}
