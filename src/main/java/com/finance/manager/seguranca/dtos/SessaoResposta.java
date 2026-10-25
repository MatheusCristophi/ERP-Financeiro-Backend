package com.finance.manager.seguranca.dtos;

import com.finance.manager.usuarioempresa.UsuarioRoles;

import java.util.UUID;

public record SessaoResposta(String nome, String email, UUID empresaId, UsuarioRoles role) {}