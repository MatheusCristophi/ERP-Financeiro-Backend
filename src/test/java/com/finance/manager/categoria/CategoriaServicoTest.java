package com.finance.manager.categoria;

import com.finance.manager.categoria.dto.CategoriaRequisicao;
import com.finance.manager.empresa.*;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServicoTest {

    @Mock
    private CategoriaRepositorio categoriaRepositorio;
    @Mock
    private EmpresaRepositorio empresaRepositorio;
    @Mock
    private UsuarioRepositorio usuarioRepositorio;
    @Mock
    private UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;


    @InjectMocks
    private CategoriaServico categoriaServico;

    private UUID usuarioId;
    private UUID empresaId;
    private UUID categoriaId;
    private UUID usuarioEmpresaId;
    private Usuario usuario;
    private Empresas empresa;
    private UsuarioEmpresa usuarioEmpresa;
    private Categoria categoria;
    private CategoriaRequisicao requisicao;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        empresaId = UUID.randomUUID();
        usuarioEmpresaId = UUID.randomUUID();
        categoriaId = UUID.randomUUID();

        usuario = new Usuario();

        usuario.setId(usuarioId);
        usuario.setNome("Ricardo");
        usuario.setEmail("ricardo@example.com");
        usuario.setSenha("Senha123");
        usuario.setAtivo(true);

        empresa = new Empresas();

        empresa.setId(empresaId);
        empresa.setDescricao("Tek-System");
        empresa.setCnpj("86682093000105");
        empresa.setEmpresaDono(usuario);
        empresa.setEmpresaRegime(EmpresaRegimeTributario.SIMPLES_NACIONAL);
        empresa.setEmpresaTipo(EmpresaTiposAtividade.FINANCEIRA);
        empresa.setEmpresaNaturezaPessoa(EmpresaNaturezaPessoa.EMPRESARIO_INDIVIDUAL);
        empresa.setStatus(true);

        usuarioEmpresa = new UsuarioEmpresa();

        usuarioEmpresa.setId(usuarioEmpresaId);
        usuarioEmpresa.setUsuario(usuario);
        usuarioEmpresa.setEmpresa(empresa);
        usuarioEmpresa.setRole(UsuarioRoles.DONO);

        requisicao = new CategoriaRequisicao(
                "Categoria Teste",
                CategoriaTipo.A_PAGAR,
                true
        );
    }

    private void carregarEmpresa() {
        when(empresaRepositorio.findById(empresaId)).thenReturn(Optional.of(empresa));
    }

    private void carregarUsuario() {
        when(usuarioRepositorio.findById(usuarioId)).thenReturn(Optional.of(usuario));
    }

    private void carregarExistUsuarioEmpresa() {
        when(usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioId, empresaId)).thenReturn(true);
    }

    private void carregarUsuarioEmpresa() {
        when(usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)).thenReturn(Optional.of(usuarioEmpresa));
    }

    @Nested
    @DisplayName("Testes realizados para criar a Categoria")
    class criarCategoria {
        @Test
        @DisplayName("Deve criar uma categoria com sucesso")
        void deveCriarCategoriaComSucesso() {

            carregarEmpresa();
            carregarUsuario();
            carregarExistUsuarioEmpresa();

            when(categoriaRepositorio.save(any(Categoria.class))).thenAnswer(invocation -> {
                Categoria catSalva = invocation.getArgument(0);
                catSalva.setId(UUID.randomUUID());
                return catSalva;
            });

            assertDoesNotThrow(
                    () -> categoriaServico.criarCategoria(empresaId, usuarioId, requisicao)
            );

            ArgumentCaptor<Categoria> categoriaCaptor = ArgumentCaptor.forClass(Categoria.class);
            verify(categoriaRepositorio).save(categoriaCaptor.capture());

            Categoria categoriaCapturada = categoriaCaptor.getValue();
            assertEquals("Categoria Teste", categoriaCapturada.getDescricao());
            assertEquals(CategoriaTipo.A_PAGAR, categoriaCapturada.getTipo());
            assertTrue(categoriaCapturada.isStatus());
            assertEquals(empresa, categoriaCapturada.getCategoriaEmpresa());
        }
    }

    @Test
    void buscarTodasCategorias() {
    }

    @Nested
    class buscarCategoria{
        @Test
        @DisplayName("Deve retornar a Exception esperada quando não encontrar a empresa")
        void deveRetornarExceptionQuandoNaoEncontrarEmpresa() {

            NaoEncontradoException exception = assertThrows(
                    NaoEncontradoException.class,
                    () -> categoriaServico.buscarCategoria(empresaId, usuarioId, categoriaId)
            );

            assertEquals("Não foi possível buscar a Empresa", exception.getMessage());
        }

        @Test
        @DisplayName("Deve retornar a Exception esperada quando não encontrar o usuário")
        void deveRetornarExceptionQuandoNaoEncontrarUsuario() {
            carregarEmpresa();

            NaoEncontradoException exception = assertThrows(
                    NaoEncontradoException.class,
                    () -> categoriaServico.buscarCategoria(empresaId, usuarioId, categoriaId)
            );

            assertEquals("Não foi possível buscar o Usuário", exception.getMessage());
        }

        @Test
        @DisplayName("Deve retornar a Exception esperada quando não encontrar o vínculo entre o usuário e a empresa")
        void deveRetornarExceptionQuandoNaoEncontrarVinculo() {
            carregarEmpresa();
            carregarUsuario();

            VinculoNaoEncontrado exception = assertThrows(
                    VinculoNaoEncontrado.class,
                    () -> categoriaServico.buscarCategoria(empresaId, usuarioId, categoriaId)
            );

            assertEquals("Vinculo não encontrado entre o usuário "+ usuarioId +" e a empresa "+empresaId, exception.getMessage());
        }

        @Test
        @DisplayName("Deve retornar a Exception esperada quando não encontrar a categoria")
        void deveRetornarExceptionQuandoNaoEncontrarCategoria() {
            carregarEmpresa();
            carregarUsuario();
            carregarUsuarioEmpresa();
            when(categoriaRepositorio.findById(categoriaId)).thenReturn(Optional.empty());

            NaoEncontradoException exception = assertThrows(
                    NaoEncontradoException.class,
                    () -> categoriaServico.buscarCategoria(empresaId, usuarioId, categoriaId)
            );

            assertEquals("Não foi possível buscar a Categoria", exception.getMessage());
        }
    }

    @Test
    void atualizarCategoria() {
    }

    @Test
    void desativarCategoria() {
    }
}