package com.finance.manager.seguranca.dtos;

import com.finance.manager.usuarioempresa.UsuarioRoles;

import java.util.UUID;

public record SecurityResposta(String token,
                            UUID empresaId,
                            UsuarioRoles role) {}