package fr.univtln.tdomenge293.model.collections.dao;

import fr.univtln.tdomenge293.interfaces.IClient;
import fr.univtln.tdomenge293.interfaces.ICommande;
import fr.univtln.tdomenge293.interfaces.ILigneDeCommande;
import fr.univtln.tdomenge293.interfaces.dao.IDao;
import fr.univtln.tdomenge293.model.collections.Commande;
import fr.univtln.tdomenge293.utils.annotations.qualifier.Using;
import fr.univtln.tdomenge293.utils.annotations.qualifier.Using.Implementation;
import fr.univtln.tdomenge293.utils.exceptions.DataAccessException;
import fr.univtln.tdomenge293.utils.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Using(Implementation.COLLECTIONS)
@ApplicationScoped
public class CommandeDAO implements IDao<ICommande, Long> {

    @Resource(name = "java:app/jdbc/h2-pool")
    private javax.sql.DataSource dataSource;

    static final String CLASS_NAME = CommandeDAO.class.getSimpleName();

    static final String ATT_NUM = "numero";
    static final String ATT_DATE = "date_commande";
    static final String ATT_CEMAIL = "client_email";

    static final String FIND = "select %s, %s, %s from commande where %s = ?".formatted(ATT_NUM, ATT_DATE, ATT_CEMAIL, ATT_NUM);
    static final String FIND_ALL = "select * from commande limit ? offset ?";
    static final String PERSIST = "insert into commande(%s, %s, %s) values(?, ?, ?)".formatted(ATT_NUM, ATT_DATE, ATT_CEMAIL);
    static final String UPDATE = "update commande set %s=?, %s=?, %s=? where %s=?".formatted(ATT_NUM, ATT_DATE, ATT_CEMAIL, ATT_NUM);
    static final String REMOVE = "delete from commande where %s=?".formatted(ATT_NUM);

    @Inject @Using(Implementation.COLLECTIONS) LigneDeCommandeDAO ligneDeCommandeDAO;
    @Inject @Using(Implementation.COLLECTIONS) ClientDAO clientDAO;

    private Date getDateSql(java.util.Date d) { return Date.valueOf( d.toString() ); }

    @Override
    public ICommande find(Long id) throws DataAccessException {
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(FIND); ) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long numero = rs.getLong(ATT_NUM);
                Date date = rs.getDate(ATT_DATE);
                IClient client = clientDAO.find( rs.getString(ATT_CEMAIL) );
                List<ILigneDeCommande> panier = ligneDeCommandeDAO.findByCommande(numero);
                ICommande c = Commande.of( numero, date, panier );
                c.setClient(client);
                panier.stream().forEach(p -> p.setCommande(c));
                return c;
            }
        } catch(SQLException e) { throw new DataAccessException("<find>", CLASS_NAME, e.getMessage()); }

        return null;
    }

    @Override
    public Page<ICommande> findAll(int pageNumber, int pageSize) throws DataAccessException {
        
        List<ICommande> commandes = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;

        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL); ) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long numero = rs.getLong(ATT_NUM);
                Date date = rs.getDate(ATT_DATE);
                IClient client = clientDAO.find( rs.getString(ATT_CEMAIL) );
                List<ILigneDeCommande> panier = ligneDeCommandeDAO.findByCommande(numero);
                ICommande c = Commande.of( numero, date, panier );
                c.setClient(client);
                panier.stream().forEach(p -> p.setCommande(c));
                commandes.add( c );
            }
        } catch(SQLException e) { throw new DataAccessException("<findAll>", CLASS_NAME, e.getMessage()); }

        return new Page<>(pageNumber, pageSize, commandes);
    }

    @Override
    public ICommande persist(ICommande entity) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(PERSIST); ) {
            stmt.setLong(1, entity.getNumero());
            stmt.setDate(2, getDateSql( entity.getDate() ));
            stmt.setString(3, entity.getClient().getEmail());
            stmt.executeUpdate();

            //persist panier
            if (!entity.getPanier().isEmpty())
                ligneDeCommandeDAO.persist( entity.getPanier() );

        } catch(SQLException e) { throw new DataAccessException("<persist>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public ICommande update(ICommande entity) throws DataAccessException {
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(UPDATE); ) {
            stmt.setLong(1, entity.getNumero());
            stmt.setDate(2, getDateSql( entity.getDate() ));
            stmt.setString(3, entity.getClient().getEmail());
            stmt.setLong(4, entity.getNumero());

            stmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<update>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public void remove(ICommande entity) throws DataAccessException {
        removeById( entity.getNumero() );
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
