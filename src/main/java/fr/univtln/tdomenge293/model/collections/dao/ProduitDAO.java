package fr.univtln.tdomenge293.model.collections.dao;

import fr.univtln.tdomenge293.interfaces.IProduit;
import fr.univtln.tdomenge293.interfaces.dao.IDao;
import fr.univtln.tdomenge293.model.collections.Produit;
import fr.univtln.tdomenge293.utils.annotations.qualifier.Using;
import fr.univtln.tdomenge293.utils.annotations.qualifier.Using.Implementation;
import fr.univtln.tdomenge293.utils.exceptions.DataAccessException;
import fr.univtln.tdomenge293.utils.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Using(Implementation.COLLECTIONS)
@ApplicationScoped
public class ProduitDAO implements IDao<IProduit, Long> {

    @Resource(name = "java:app/jdbc/h2-pool")
    private javax.sql.DataSource dataSource;
    
    static final String CLASS_NAME = ProduitDAO.class.getSimpleName();

    static final String ATT_CODE = "code";
    static final String ATT_NOM = "nom";
    static final String ATT_PRIX = "prix";

    static final String FIND = "select %s, %s, %s from produit where %s = ?".formatted(ATT_CODE, ATT_NOM, ATT_PRIX, ATT_CODE);
    static final String FIND_ALL = "select * from produit limit ? offset ?";
    static final String PERSIST = "insert into produit(%s, %s, %s) values(?, ?, ?)".formatted(ATT_CODE, ATT_NOM, ATT_PRIX);
    static final String UPDATE = "update produit set %s=?, %s=?, %s=? where %s=?".formatted(ATT_CODE, ATT_NOM, ATT_PRIX, ATT_CODE);
    static final String REMOVE = "delete from produit where %s=?".formatted(ATT_CODE);

    @Override
    public IProduit find(Long id) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(FIND); ) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return Produit.of(rs.getLong(ATT_CODE), rs.getString(ATT_NOM), rs.getInt(ATT_PRIX));
        } catch(SQLException e) { throw new DataAccessException("<find>", CLASS_NAME, e.getMessage()); }

        return null;
    }

    @Override
    public Page<IProduit> findAll(int pageNumber, int pageSize) throws DataAccessException {
        
        List<IProduit> produits = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;

        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                produits.add( Produit.of(rs.getLong(ATT_CODE), rs.getString(ATT_NOM), rs.getInt(ATT_PRIX)) );
        } catch(SQLException e) { throw new DataAccessException("<findAll>", CLASS_NAME, e.getMessage()); }
    
        return new Page<>(pageNumber, pageSize, produits);
    }

    @Override
    public IProduit persist(IProduit entity) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(PERSIST); ) {
            stmt.setLong(1, entity.getCode());
            stmt.setString(2, entity.getName());
            stmt.setInt(3, entity.getPrixUnit());

            stmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<persist>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public IProduit update(IProduit entity) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(UPDATE); ) {
            stmt.setLong(1, entity.getCode());
            stmt.setString(2, entity.getName());
            stmt.setInt(3, entity.getPrixUnit());
            stmt.setLong(4, entity.getCode());

            stmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<update>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public void remove(IProduit entity) throws DataAccessException {
        removeById( entity.getCode() );
    }

    @Override
    public void removeById(Long id) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(REMOVE); ) {
            stmt.setLong(1, id);

            stmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<remove>", CLASS_NAME, e.getMessage()); }
    }
}
