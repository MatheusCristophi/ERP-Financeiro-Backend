package com.finance.manager.excecoes;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNaoEncontrado(NoResourceFoundException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Página não encontrada, verifique a url e tente novamente por favor."
        );
        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAcessoNegado(AccessDeniedException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Acesso negado."
        );
        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleDadosInvalidos(BadCredentialsException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Dados Inválidos."
        );
        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleErroNaRequisicao(BadRequestException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Ocorreu um erro na requisição, verifique as informações."
        );
        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(InternalError.class)
    public ProblemDetail handleErroInterno(AccessDeniedException ex) {
        ProblemDetail problemDetail =  ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Ocorreu um problema interno. Por favor, contate o Desenvolvedor"
        );
        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(NullPointerException.class)
    public ProblemDetail handlerValorNull(NullPointerException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );

        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ProblemDetail handleNaoEncontrado(NaoEncontradoException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(SemPermissaoException.class)
    public ProblemDetail handleSemPermissao(SemPermissaoException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(EmailJaExisteException.class)
    public ProblemDetail handleEmaiJaExiste(EmailJaExisteException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(VinculoNaoEncontrado.class)
    public ProblemDetail handleNaoEncontrado(VinculoNaoEncontrado exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setInstance(null);
        problemDetail.setType(java.net.URI.create("about:blank"));
        return problemDetail;
    }
}
