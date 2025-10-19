package com.bento.loginService.domain.model;

import com.bento.loginService.dto.login.LoginRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;


@Entity
@Table(name = "tb_login")
public class LoginEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "pessoa_id", nullable = false)
    private long pessoaId;

    @Column(name = "nomeusuario") // <-- agora corresponde corretamente à coluna no banco
    private String nomeUsuario;

    private String senha;

    private String perfis;

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getPerfis() {
        return perfis;
    }

    public void setPerfis(String perfis) {
        this.perfis = perfis;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.senha(), this.senha);
    }
}
