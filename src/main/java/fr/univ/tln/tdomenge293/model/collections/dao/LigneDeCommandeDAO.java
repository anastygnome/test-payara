package fr.univ.tln.tdomenge293.model.collections.dao;

import fr.univ.tln.tdomenge293.interfaces.ICommande;
import fr.univ.tln.tdomenge293.interfaces.ILigneDeCommande;
import fr.univ.tln.tdomenge293.interfaces.IProduit;
import fr.univ.tln.tdomenge293.interfaces.dao.IDao;
import fr.univ.tln.tdomenge293.interfaces.dao.IDaoByCommande;
import fr.univ.tln.tdomenge293.interfaces.dao.IDaoByProduit;
import fr.univ.tln.tdomenge293.model.collections.LigneDeCommande;
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
public class LigneDeCommandeDAO implements IDao<List<ILigneDeCommande>, Long>, IDaoByCommande<List<ILigneDeCommande>, ICommande>, IDaoByProduit<List<ILigneDeCommande>, IProduit> {

    @Resource(name = "java:app/jdbc/h2-pool")
    private javax.sql.DataSource dataSource;
    
    static final String CLASS_NAME = LigneDeCommandeDAO.class.getSimpleName();

    public static final String ATT_QUANTITE = "quantite";
    public static final String ATT_CNUM = "commande_num";
    public static final String ATT_PCODE = "produit_code";

    static final String FIND_BY_COMMANDE = "select %s, %s, %s from ligne_de_commande where %s = ?".formatted(ATT_QUANTITE, ATT_CNUM, ATT_PCODE, ATT_CNUM);
    static final String FIND_BY_PRODUIT = "select %s, %s, %s from ligne_de_commande where %s = ?".formatted(ATT_QUANTITE, ATT_CNUM, ATT_PCODE, ATT_PCODE);
    static final String FIND_ALL = "select * from ligne_de_commande limit ? offset ?";
    static final String PERSIST = "insert into ligne_de_commande(%s, %s, %s) values(?, ?, ?)".formatted(ATT_QUANTITE, ATT_CNUM, ATT_PCODE);
    static final String UPDATE_BY_COMMANDE = "update ligne_de_commande set %s=?, %s=?, %s=? where %s=?".formatted(ATT_QUANTITE, ATT_CNUM, ATT_PCODE, ATT_CNUM);
    static final String UPDATE_BY_PRODUIT = "update ligne_de_commande set %s=?, %s=?, %s=? where %s=?".formatted(ATT_QUANTITE, ATT_CNUM, ATT_PCODE, ATT_PCODE);
    static final String REMOVE_BY_COMMANDE = "delete from ligne_de_commande where %s=?".formatted(ATT_CNUM);
    static final String REMOVE_BY_PRODUIT = "delete from ligne_de_commande where %s=?".formatted(ATT_PCODE);
    static final String REMOVE_BY_PAIR = "delete from ligne_de_commande where %s=? and %s=?".formatted(ATT_CNUM, ATT_PCODE);

    PreparedStatement findByStmt;
    PreparedStatement updateByStmt;
    PreparedStatement removeByStmt;

    @Inject @Using(Implementation.COLLECTIONS) ProduitDAO produitDAO;

    //#region FIND
    @Override
    public List<ILigneDeCommande> find(Long id) throws DataAccessException {
        List<ILigneDeCommande> panier = new ArrayList<>();
        try {
            findByStmt.setLong(1, id);
            ResultSet rs = findByStmt.executeQuery();

            while (rs.next()) {
                int quantite = rs.getInt(ATT_QUANTITE);
                IProduit p = produitDAO.find( rs.getLong(ATT_PCODE) );
                
                panier.add( LigneDeCommande.of(quantite, null, p) );
            }
        } catch(SQLException e) { throw new DataAccessException("<find>", CLASS_NAME, e.getMessage()); }

        return panier;
    }

    @Override
    public List<ILigneDeCommande> findByCommande(Long id) throws DataAccessException {
        try (Connection conn = dataSource.getConnection();){
            findByStmt = conn.prepareStatement(FIND_BY_COMMANDE);
            return find( id );
        } catch(SQLException e) { throw new DataAccessException("<findByCommande>", CLASS_NAME, e.getMessage()); }
    }

    @Override
    public List<ILigneDeCommande> findByProduit(Long id) throws DataAccessException {
        try (Connection conn = dataSource.getConnection();) {
            findByStmt = conn.prepareStatement(FIND_BY_PRODUIT);
            return find( (long) id );
        } catch(SQLException e) { throw new DataAccessException("<findByProduit>", CLASS_NAME, e.getMessage()); }
    }

    @Override
    public Page<List<ILigneDeCommande>> findAll(int pageNumber, int pageSize) throws DataAccessException {
        
        List<ILigneDeCommande> panier = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        
        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL); ) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                
                int quantite = rs.getInt(ATT_QUANTITE);
                IProduit p = produitDAO.find( rs.getLong(ATT_PCODE) );
                panier.add( LigneDeCommande.of(quantite, null, p) );
            }
        } catch(SQLException e) { throw new DataAccessException("<findAll>", CLASS_NAME, e.getMessage()); }

        return new Page<>(pageNumber, pageSize, List.of(panier));
    }
    //#endregion

    @Override
    public List<ILigneDeCommande> persist(List<ILigneDeCommande> entity) throws DataAccessException {
        
        if (entity.isEmpty()) return entity;

        try(Connection conn = dataSource.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(PERSIST); ) {

            for (ILigneDeCommande e : entity){
                stmt.setInt(1, e.getQuantite());
                stmt.setLong(2, e.getCommande().getNumero());
                stmt.setLong(3, e.getProduit().getCode());

                stmt.executeUpdate();
            }            
        } catch(SQLException e) { throw new DataAccessException("<persist>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    //#region UPDATE
    @Override
    public List<ILigneDeCommande> update(List<ILigneDeCommande> entity) throws DataAccessException {
        
        try {
            for (ILigneDeCommande e : entity) {
                updateByStmt.setInt(1, e.getQuantite());
                updateByStmt.setLong(2, e.getCommande().getNumero());
                updateByStmt.setLong(3, e.getProduit().getCode());

                updateByStmt.executeUpdate();
            }
        } catch(SQLException e) { throw new DataAccessException("<update>", CLASS_NAME, e.getMessage()); }

        return entity;
    }

    @Override
    public List<ILigneDeCommande> updateByCommande(ICommande ref) throws DataAccessException {
        try (Connection conn = dataSource.getConnection();){
            updateByStmt = conn.prepareStatement(UPDATE_BY_COMMANDE);
            return update( findByCommande(ref) );
        } catch (SQLException e) { throw new DataAccessException("<updateByCommande>", CLASS_NAME, e.getMessage()); }
    }

    @Override
    public List<ILigneDeCommande> updateByProduit(IProduit ref) throws DataAccessException {
        try (Connection conn = dataSource.getConnection();){
            updateByStmt = conn.prepareStatement(UPDATE_BY_PRODUIT);
            return update( findByProduit(ref) );
        } catch (SQLException e) { throw new DataAccessException("<updateByProduit>", CLASS_NAME, e.getMessage()); }
    }
    //#endregion

    //#region REMOVE
    @Override
    public void removeByCommande(ICommande ref) throws DataAccessException {
        try (Connection conn = dataSource.getConnection();){
            removeByStmt = conn.prepareStatement(REMOVE_BY_COMMANDE);
            removeById( ref.getNumero() );
        } catch(SQLException e) { throw new DataAccessException("<removeByCommande>", CLASS_NAME, e.getMessage()); }
    }

    @Override
    public void removeByProduit(IProduit ref) throws DataAccessException {
        try (Connection conn = dataSource.getConnection();){
            removeByStmt = conn.prepareStatement(REMOVE_BY_PRODUIT);
            removeById((long) ref.getCode());
        } catch(SQLException e) { throw new DataAccessException("<removeByProduit>", CLASS_NAME, e.getMessage()); }
    }

    @Override
    public void remove(List<ILigneDeCommande> entity) throws DataAccessException {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(REMOVE_BY_PAIR); ) {
            for (ILigneDeCommande e : entity) {
                stmt.setLong(1, e.getCommande().getNumero());
                stmt.setLong(2, e.getProduit().getCode());
                stmt.executeUpdate();
            }
        } catch(SQLException e) { throw new DataAccessException("<removeEntity>", CLASS_NAME, e.getMessage()); }
    }

    @Override
    public void removeById(Long id) throws DataAccessException {
        try {
            removeByStmt.setLong(1, id);
            removeByStmt.executeUpdate();
        } catch(SQLException e) { throw new DataAccessException("<remove>", CLASS_NAME, e.getMessage()); }
    }
    //#endregion
}
