package org.rgomez.springcloud.msvc.usuarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo nombre no puede ser vacio")
    private String nombre;

    @Email
    @NotBlank(message = "El email no puede ir vacio")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Debes introducir un password")
    private String password;


}
