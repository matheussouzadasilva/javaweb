package br.com.matheussilva.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.matheussilva.persistencia.entidade.Usuario;

public class UsuarioDAO 
{
    private Connection con = ConexaoFactory.getConnection();

    public int buscaUltimo() 
    {
        String sql = "SELECT MAX(id)+1 AS codigo FROM usuario";
        
        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            ResultSet resultado = preparador.executeQuery();
            // Posicionando o cursor no primeiro registro
            if (resultado.next()) {
                return resultado.getInt("codigo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void cadastrar(Usuario usu) 
    {
        String sql = "INSERT INTO usuario (id, nome, login, senha) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            preparador.setInt(1, this.buscaUltimo()); // substitui o ? pelo nome do usuário
            preparador.setString(2, usu.getNome()); // substitui o ? pelo nome do usuário
            preparador.setString(3, usu.getLogin());
            preparador.setString(4, usu.getSenha());
            // Executando o comando SQL no banco
            preparador.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void alterar(Usuario usu) 
    {
        String sql = "UPDATE usuario SET nome = ?, login = ? , senha = ? WHERE id = ?";
        
        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            preparador.setString(1, usu.getNome()); // substitui o ? pelo dado do usuário
            preparador.setString(2, usu.getLogin());
            preparador.setString(3, usu.getSenha());
            preparador.setInt(4, usu.getId());
            // Executando o comando SQL no banco
            preparador.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(Usuario usu) 
    {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            preparador.setInt(1, usu.getId());
            // Executando o comando SQL no banco
            preparador.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Usuario usuario) 
    {
        if (usuario.getId() != null && usuario.getId()!=0) {
            alterar(usuario);
        } else {
            cadastrar(usuario);
        }
    }

    /**
     * Buscar de um registro no banco de dados pelo número do id do usuário
     * 
     * @param id É um inteiro que representa o número do id do usuário a ser buscado
     * @return Um objeto usuário quando encontra ou null quando não encontra
     */
    public Usuario buscarPorId(Integer id) 
    {
        String sql = "SELECT id, nome, login, senha FROM usuario WHERE id = ?";

        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            preparador.setInt(1, id);

            ResultSet resultado = preparador.executeQuery();
            // Posicionando o cursor no primeiro registro
            if (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setNome(resultado.getString("nome"));
                usuario.setLogin(resultado.getString("login"));
                usuario.setSenha(resultado.getString("senha"));

                return usuario;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Realiza a busca de todos registros da tabela de usuários
     * 
     * @return Um lista de objetos Usuario contendo 0 elementos quando tiver
     *         registro ou n elementos quando tive resultado
     */
    public List<Usuario> buscarTodos() 
    {
        String sql = "SELECT id, nome, login, senha FROM usuario";
        List<Usuario> lista = new ArrayList<Usuario>();
        
        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            ResultSet resultado = preparador.executeQuery();
            // Posicionando o cursor no primeiro registro
            while (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setNome(resultado.getString("nome"));
                usuario.setLogin(resultado.getString("login"));
                usuario.setSenha(resultado.getString("senha"));
                // Adicionando usuario na lista
                lista.add(usuario);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lista;
    }

    public Usuario autenticar(Usuario usuConsulta) 
    {
        String sql = "SELECT id, nome, login, senha FROM usuario WHERE login = ? AND senha = ?";

        try (PreparedStatement preparador = con.prepareStatement(sql)) {
            preparador.setString(1, usuConsulta.getLogin());
            preparador.setString(2, usuConsulta.getSenha());
            ResultSet resultado = preparador.executeQuery();

            if (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setNome(resultado.getString("nome"));
                usuario.setLogin(resultado.getString("login"));
                usuario.setSenha(resultado.getString("senha"));

                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}