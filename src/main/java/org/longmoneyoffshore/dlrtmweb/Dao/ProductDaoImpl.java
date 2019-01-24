package org.longmoneyoffshore.dlrtmweb.Dao;

import org.longmoneyoffshore.dlrtmweb.Entities.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@org.longmoneyoffshore.dlrtmweb.Repository
//@Qualifier("mongoData")
//@Qualifier("fakeData")

@Component
public class ProductDaoImpl implements ProductDao {

    /*static {
        products = new HashMap<String, Product>() {
            {
                //put (1, new Product(1,"Mario", "Math"));
            }
        };
    }*/

    //private static Map<String, Product> products;

    //Connection mongoConnection;


    @Autowired
    private DataSource dataSource;

    //@Autowired
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {

        //this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    //TODO: is this relevant?
    public Product getProductXml(String productId) {

        Connection conn = null;

        try {

            conn = dataSource.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product where id = ?");
            ps.setString(1, productId);

            Product product = null;
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                //product =  new Product(productId,rs.getString("name"));
                product =  new Product(String.valueOf(productId),rs.getString("productName"));
            }

            rs.close();
            ps.close();

            return product;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    //old style implementation
    //TODO: this is going to be our implementation
    public Product getProduct (String productId) {

        Connection conn = null;

        try {
            String driver = "org.apache.derby.jdbc.ClientDriver";

            Class.forName(driver).getDeclaredConstructor().newInstance();
            //Class.forName(driver).newInstance();

            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/db");

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product where id = ?");
            //ps.setInt(1, productId);
            ps.setString(1, productId);

            Product product = null;
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product =  new Product(String.valueOf(productId),rs.getString("name"));
            }

            rs.close();
            ps.close();

            return product;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    public Product getProductById (String productId) {
        //return this.products.get(id);

        String sql = "SELECT * FROM CIRCLE WHERE ID = ?";
        //return jdbcTemplate.queryForObject(sql, new Object[] {productId}, new ProductMapper());

        //TODO: is it possible to do the above with namedParameter?

        Product productWithId = namedParameterJdbcTemplate.query(sql, new ProductMapper()).get(0);

        return productWithId;

    }


    public int getProductCount() {
        String sql = "SELECT COUNT(*) FROM product";

        //TODO: is this property set in the spring.xml?
        //jdbcTemplate.setDataSource(getDataSource());

        int count = jdbcTemplate.queryForObject(sql,Integer.class);

        ProductMapper productMapper = new ProductMapper();
        //int count = namedParameterJdbcTemplate.queryForObject(sql, commandsMap, new ProductMapper ());
        List<Product> productsWithProperty = namedParameterJdbcTemplate.query(sql, new ProductMapper());

        count = productsWithProperty.size();

        return count;

    }

    public String getProductName(String productId) {
        String sql = "SELECT NAME FROM CIRCLE WHERE ID = ?";

        //String productName = jdbcTemplate.queryForObject(sql,String.class);

        String productName = jdbcTemplate.queryForObject(sql, new Object[] {productId}, String.class); //TODO: this is the old implementation

        //TODO: new implementation
        productName = namedParameterJdbcTemplate.query(sql, new ProductMapper()).get(0).getProductName();

        //TODO: potentially redundant

        return productName;
    }



    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM CIRCLE";

        //return jdbcTemplate.query(sql, new ProductMapper());
        return namedParameterJdbcTemplate.query(sql, new ProductMapper());

        //return this.products.values(); — Bad
    }

    @Override
    public void insertProduct (Product product) {

        //this.products.put(product.getProductUniqueID(), product);

        //TODO: First Implementation
        //String sql = "INSERT INTO CIRCLE (ID, NAME) VALUES (?, ?)";
        //jdbcTemplate.update(sql, new Object[] {product.getProductUniqueID(), product.getProductName()});


        //TODO: Second Implementation w/ named parameters
        String sql = "INSERT INTO CIRCLE (ID, NAME) VALUES (:id, :name)";

        SqlParameterSource namedParameters = new MapSqlParameterSource("id", product.getProductUniqueID())
                .addValue("name", product.getProductName());

        namedParameterJdbcTemplate.update(sql, namedParameters);
    }


    @Override
    public void removeProductById(String id) {
        //this.products.remove(id);

        String sql = "DELETE FROM CIRCLE WHERE ID IN (?)";
        jdbcTemplate.update(sql,new Object[] {id});

    }

    //TODO: why is this here?
    public void createClientTable () {
        String sql = "CREATE TABLE TRIANGLE (ID INTEGER, NAME VARCHAR(50))";
        jdbcTemplate.execute(sql);
    }


    @Override
    public Collection<Product> getProductsByField(Object field) {
        //TODO: implement the capacity to search for object based on different fields
        // Heavy Duty

        return null;
    }

    @Override
    public void updateProduct(Product product) {

        //TODO: unclear whether this implementation actually works ——— Needs testing

        //Product p = products.get(product.getProductUniqueID());
        //p.setCourse(product.getCourse());
        //p.setName(product.getName());

        //this.products.put(product.getProductUniqueID(), product);

        //TODO: unclear whether this SQL command is good — Also, extend the named parameters to all the relevant parameters
        // of the Product being updated

        String sql = "UPDATE INTO CIRCLE (ID, NAME) VALUES (:id, :name)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", product.getProductUniqueID())
                .addValue("name", product.getProductName()); //.addValue()....

        namedParameterJdbcTemplate.update(sql, namedParameters);
    }


    //TODO: mapping the DB table colums to individual fields in the data object
    private static final class ProductMapper implements RowMapper<Product> {

        public ProductMapper() {};

        @Override
        public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Product product = new Product();

            product.setProductUniqueID(resultSet.getString("ID"));

            product.setProductName(resultSet.getString("NAME"));
            product.setProductManufacturer(resultSet.getString("MANUFACTURER"));
            product.setProductCountryOfOrigin(resultSet.getString("COUNTRY OF ORIGIN"));
            product.setProductDescription(resultSet.getString("DESCRIPTION"));

            product.setProductUnitPurchasePrice(resultSet.getDouble("PURCHASE PRICE"));
            product.setProductUnitPrice(resultSet.getDouble("UNIT PRICE"));
            product.setProductDiscounts(resultSet.getDouble("DISCOUNTS"));
            product.setProductAdjustments(resultSet.getDouble("ADJUSTMENTS"));
            product.setProductCredits(resultSet.getDouble("CREDITS"));
            product.setProductDeductions(resultSet.getDouble("DEDUCTIONS"));
            product.setProductSpecialOffers(resultSet.getString("SPECIAL OFFERS"));
            product.setCurrency (resultSet.getString("CURRENCY"));

            product.setProductItemsInStockInt(resultSet.getInt("ITEMS IN STOCK INT"));
            product.setProductItemsInStockDecimal(resultSet.getFloat("ITEMS IN STOCK DECIMAL"));
            product.setProductQuantityInStock(resultSet.getFloat("QUANTITY IN STOCK"));
            product.setProductMeasurementUnit(resultSet.getString("MEASUREMENT UNIT"));

            product.setProductNeedToReorder(resultSet.getInt("REORDER"));
            product.setProductSpecialMentions(resultSet.getString("SPECIAL MENTIONS"));

            return product;
        }
    }

}