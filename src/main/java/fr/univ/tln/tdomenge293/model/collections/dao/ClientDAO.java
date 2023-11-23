package fr.univ.tln.tdomenge293.model.collections.dao;

import fr.univ.tln.tdomenge293.interfaces.IClient;
import fr.univ.tln.tdomenge293.interfaces.dao.IDao;
import fr.univ.tln.tdomenge293.model.collections.Client;
import fr.univ.tln.tdomenge293.utils.annotations.qualifier.Using;
import fr.univ.tln.tdomenge293.utils.annotations.qualifier.Using.Implementation;
import fr.univ.tln.tdomenge293.utils.exceptions.DataAccessException;
import fr.univ.tln.tdomenge293.utils.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
public class ClientDAO implements IDao<IClient, String> {
    
    @Resource(name = "java:app/jdbc/h2-pool")
    private javax.sql.DataSource dataSource;

    static final String CLASS_NAME = ClientDAO.class.getSimpleName();

    static final String ATT_NOM = "nom";
    static final String ATT_PRENOM = "prenom";
    static final String ATT_EMAIL = "email";

    static final String FIND = "select %s, %s, %s from client where %s = ?".formatted(ATT_NOM, ATT_PRENOM, ATT_EMAIL, ATT_EMAIL);
    static final String FIND_ALL = "select * from client limit ? offset ?";
    static final String PERSIST = "insert into client(%s, %s, %s) values(?, ?, ?)".formatted(ATT_NOM, ATT_PRENOM, ATT_EMAIL);
    static final String UPDATE = "update client set %s=?, %s=?, %s=? where %s=?".formatted(ATT_NOM, ATT_PRENOM, ATT_EMAIL, ATT_EMAIL);
    static final String REMOVE = "delete from client where %s=?".formatted(ATT_EMAIL);

    @Inject @Using(Implementation.COLLECTIONS) CommandeDAO commandeDAO;

    @Override
    public IClient find(String email) throws DataAccessException {
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(FIND); ) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return Client.of(rs.getString(ATT_NOM), rs.getString(ATT_PRENOM), rs.getString(ATT_EMAIL));
        } catch(SQLException e) { throw new DataAccessException("<find>", CLASS_NAME, e.getMessage()); }

        return null;
    }

    @Override
    public Page<IClient> findAll(int pageNumber, int pageSize) throws DataAccessException {
        
        List<IClient> clients = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL); ) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String mail = rs.getString(ATT_EMAIL);
                clients.add( Client.of(rs.getString(ATT_NOM), rs.getString(ATT_PRENOM), mail));
            }
        } catch(SQLException e) { throw new DataAccessException("<findAll>", CLASS_NAME, e.getMessage()); }

        return new Page<>(pageNumber, pageSize, clients);
    }

    @Override
    public IClient persist(IClient entity) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(PERSIST); ) {
            stmt.setString(1, entity.getNom());
            stmt.setString(2, entity.getPrenom());
            stmt.setString(3, entity.getEmail());
            stmt.executeUpdate();

            if (!entity.getCommandes().isEmpty()) {
                entity.getCommandes().stream().forEach(c -> c.setClient(entity));
                commandeDAO.persistAll(entity.getCommandes());
            }

        } catch(SQLException e) { throw new DataAccessException("<persist>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public IClient update(IClient entity) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(UPDATE); ) {
            stmt.setString(1, entity.getNom());
            stmt.setString(2, entity.getPrenom());
            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getEmail());

            stmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<update>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public void remove(IClient entity) throws DataAccessException {
        removeById( entity.getEmail() );
    }

    @Override
    public void removeById(String email) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(REMOVE); ) {
            stmt.setString(1, email);

            commandeDAO.findAll().result().stream().filter(c -> c.getClient().getEmail().equals(email)).forEach(c -> c.setClient(null));
            // ClientCommandeDAO.of().remove(email, ClientCommandeDAO.of().findByClient(email)); //ne devrait pas être nécéssaire, on delete cascade sur db

            stmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<remove>", CLASS_NAME, e.getMessage()); }
    }
}
