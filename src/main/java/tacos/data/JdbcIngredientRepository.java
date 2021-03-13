package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Ingredient.Type;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbc) {
      this.jdbc = jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query("select id, name, type from Ingredient",
            this::mapRowToIngredient);
    }
  
    @Override
    public Ingredient findById(String id) {
        return jdbc.queryForObject(
            "select id, name, type from Ingredient where id=?",
            this::mapRowToIngredient, id);
    }
    
    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update(
            "insert into Ingredient (id, name, type) values (?, ?, ?)",
            ingredient.getId(), 
            ingredient.getName(),
            ingredient.getType().toString());
        return ingredient;
    }
    
    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        Type type = Ingredient.Type.valueOf(rs.getString("type"));
        return new Ingredient(id, name, type);
    }  
  
}
