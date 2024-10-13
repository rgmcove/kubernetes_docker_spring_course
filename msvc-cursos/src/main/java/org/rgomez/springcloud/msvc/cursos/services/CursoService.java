package org.rgomez.springcloud.msvc.cursos.services;

import org.rgomez.springcloud.msvc.cursos.clients.UsuarioClientsRest;
import org.rgomez.springcloud.msvc.cursos.models.Usuario;
import org.rgomez.springcloud.msvc.cursos.models.entities.Curso;
import org.rgomez.springcloud.msvc.cursos.models.entities.CursoUsuario;
import org.rgomez.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService implements ICursoService {

    @Autowired
    CursoRepository cursoRepository;
    @Autowired
    private UsuarioClientsRest clients;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id, String token) {
        Optional<Curso> o = cursoRepository.findById(id);
        if (o.isPresent()) {
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios().stream().map(cu -> cu.getUsuarioId())
                        .collect(Collectors.toList());

                List<Usuario> usuarios = clients.obtenerAlumnosporCurso(ids, token);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId, String token) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc = clients.detalle(usuario.getId(), token);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCurdoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId, String token) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioNuevoMsvc = clients.crear(usuario, token);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCurdoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId, String token) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc = clients.detalle(usuario.getId(), token);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
