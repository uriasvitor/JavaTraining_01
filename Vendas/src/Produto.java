
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class Produto {

    private Connection connection;

    public Produto(Connection connection) {
        this.connection = connection;
    }

    public List<String> listarProdutos() throws SQLException {
        List<String> produtos = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM produtos");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String descricao = resultSet.getString("descricao");
            double preco = resultSet.getDouble("preco");
            boolean ativo = resultSet.getBoolean("ativo");
            produtos.add("ID: " + id + ", Descrição: " + descricao + ", Preço: " + preco + ", Ativo: " + ativo);
        }
        resultSet.close();
        statement.close();
        return produtos;
    }


    public void gravarProduto(int id, String descricao, double preco, boolean ativo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO produtos (id, descricao, preco, ativo) VALUES (?, ?, ?, ?)");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, descricao);
        preparedStatement.setDouble(3, preco);
        preparedStatement.setBoolean(4, ativo);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Produto gravado com sucesso.");
    }

    public void atualizarProduto(int id, String novaDescricao, double novoPreco) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE produtos SET descricao = ?, preco = ? WHERE id = ?");
        preparedStatement.setString(1, novaDescricao);
        preparedStatement.setDouble(2, novoPreco);
        preparedStatement.setInt(3, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Produto atualizado com sucesso.");
    }

    public void excluirProduto(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM produtos WHERE Id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Produto excluído com sucesso.");
    }

    public List<String> pesquisarProduto(String parametro) throws SQLException {
        List<String> produtosEncontrados = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM produtos WHERE Id = ? OR descricao = ?");
        preparedStatement.setString(1, parametro);
        preparedStatement.setString(2, parametro);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("Id");
            String descricao = resultSet.getString("Descricao");
            double preco = resultSet.getDouble("Preco");
            boolean ativo = resultSet.getBoolean("Ativo");
            String produtoString = "ID: " + id + ", Descrição: " + descricao + ", Preço: " + preco + ", Ativo: " + ativo;
            produtosEncontrados.add(produtoString);
        }

        resultSet.close();
        preparedStatement.close();
        return produtosEncontrados;
    }


}
