package org.rgomez.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import org.rgomez.springcloud.msvc.cursos.models.Usuario;
import org.rgomez.springcloud.msvc.cursos.models.entities.Curso;
import org.rgomez.springcloud.msvc.cursos.services.ICursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private ICursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id, @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<Curso> curso = cursoService.porIdConUsuarios(id, token);
        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validateBinding(result);
        }
        Curso cursoDB = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validateBinding(result);
        }

        Optional<Curso> cursoDB = cursoService.porId(id);
        if (cursoDB.isPresent()) {
            Curso cursoNew = cursoDB.get();
            cursoNew.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoNew));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.porId(id);
        if (curso.isPresent()) {
            cursoService.eliminar(curso.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId,
                                            @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<Usuario> o;
        try {
            o = cursoService.asignarUsuario(usuario, cursoId, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(
                    "mensaje", "No existe el usuario por id o error en la comunicacion: " + e.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId,
                                          @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<Usuario> o;
        try {
            o = cursoService.crearUsuario(usuario, cursoId, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(
                    "mensaje", "No se pudo crear el usuario o error en la comunicacion: " + e.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId,
                                             @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<Usuario> o;
        try {
            o = cursoService.eliminarUsuario(usuario, cursoId, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(
                    "mensaje", "No existe el usuario por id o error en la comunicacion: " + e.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validateBinding(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(fieldError -> {
            errores.put(fieldError.getField(),  "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
