package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.config.LdapConfig;
import net.andresbustamante.yafoot.config.LdapTestConfig;
import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.util.LdapConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.Name;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author andresbustamante
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapTestConfig.class, LdapConfig.class})
class UserDAOTest {

    private static final Utilisateur USR_TEST = new Utilisateur("test@email.com", "password", "TEST",
            "Utilisateur", "0123456789");

    @Value("${ldap.config.users.dn}")
    private String dnUtilisateurs;

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    void setUp() throws Exception {
        userDAO.saveUser(USR_TEST, RolesEnum.JOUEUR);
    }

    @AfterEach
    void tearDown() throws Exception {
        userDAO.deleteUser(USR_TEST, new RolesEnum[]{RolesEnum.JOUEUR});
    }

    @Test
    void saveUser() throws Exception {
        Utilisateur nouvelUtilisateur = buildNewUser();

        userDAO.saveUser(nouvelUtilisateur, RolesEnum.JOUEUR);

        Utilisateur utilisateur = userDAO.findUserByUid(getUid(nouvelUtilisateur).toString());
        assertNotNull(utilisateur);
        assertNotNull(utilisateur.getNom());
        assertNotNull(utilisateur.getPrenom());
        assertNotNull(utilisateur.getEmail());
        assertNull(utilisateur.getMotDePasse());

        userDAO.deleteUser(nouvelUtilisateur, new RolesEnum[]{RolesEnum.JOUEUR});
    }

    @Test
    void updateUser() throws Exception {
        Utilisateur utilisateurModifie = new Utilisateur();
        utilisateurModifie.setEmail(USR_TEST.getEmail());
        utilisateurModifie.setNom(USR_TEST.getNom() + " autre");
        utilisateurModifie.setPrenom(USR_TEST.getPrenom() + " autre");

        userDAO.updateUser(utilisateurModifie);

        Utilisateur utilisateur = userDAO.findUserByUid(getUid(utilisateurModifie).toString());
        assertNotNull(utilisateur);
        assertEquals(USR_TEST.getEmail(), utilisateur.getEmail());
        assertNotNull(utilisateur.getNom());
        assertEquals(USR_TEST.getNom() + " autre", utilisateur.getNom());
        assertNotNull(utilisateur.getPrenom());
        assertEquals(USR_TEST.getPrenom() + " autre", utilisateur.getPrenom());
        assertNotNull(utilisateur.getNom());

        // Remettre les informations de l'utilisateur
        utilisateur.setNom(USR_TEST.getNom());
        utilisateur.setPrenom(USR_TEST.getPrenom());
        utilisateur.setTelephone(USR_TEST.getTelephone());
        userDAO.updateUser(utilisateur);
    }

    @Test
    void updateUserPassword() throws Exception {
        // Given
        Utilisateur utilisateurModifie = new Utilisateur();
        utilisateurModifie.setEmail(USR_TEST.getEmail());
        utilisateurModifie.setNom(USR_TEST.getNom() + " autre");
        utilisateurModifie.setPrenom(USR_TEST.getPrenom() + " autre");
        utilisateurModifie.setMotDePasse("monNouveauMotDePasse");

        // When
        userDAO.updateUser(utilisateurModifie);
        Utilisateur utilisateur = userDAO.findUserByUid(getUid(utilisateurModifie).toString());

        // Then
        assertNotNull(utilisateur);
        assertEquals(USR_TEST.getNom(), utilisateur.getNom()); // Non modifié
        assertEquals(USR_TEST.getPrenom(), utilisateur.getPrenom()); // Non modifié
        assertNull(utilisateur.getMotDePasse()); // Non chargé pour sécurité

        // Remettre les informations de l'utilisateur
        utilisateur.setNom(USR_TEST.getNom());
        utilisateur.setPrenom(USR_TEST.getPrenom());
        utilisateur.setTelephone(USR_TEST.getTelephone());
        userDAO.updateUser(utilisateur);
    }

    @Test
    void deleteUser() throws Exception {
        Utilisateur nouvelUtilisateur = buildNewUser();
        userDAO.saveUser(nouvelUtilisateur, RolesEnum.JOUEUR);

        userDAO.deleteUser(nouvelUtilisateur, new RolesEnum[]{RolesEnum.JOUEUR});

        Utilisateur utilisateur = userDAO.findUserByUid(getUid(nouvelUtilisateur).toString());
        assertNull(utilisateur);
    }

    @Test
    void findUser() throws Exception {
        Utilisateur utilisateur = userDAO.findUserByUid(getUid(USR_TEST).toString());
        assertNotNull(utilisateur);
        assertNotNull(utilisateur.getEmail());
        assertEquals(USR_TEST.getEmail(), utilisateur.getEmail());
        assertNotNull(utilisateur.getNom());
        assertEquals(USR_TEST.getNom(), utilisateur.getNom());
        assertNotNull(utilisateur.getPrenom());
        assertEquals(USR_TEST.getPrenom(), utilisateur.getPrenom());
        assertNotNull(utilisateur.getNom());
        assertNull(utilisateur.getMotDePasse());
    }

    private Utilisateur buildNewUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("nouvel.utilisateur@email.com");
        utilisateur.setMotDePasse("motDePasse");
        utilisateur.setPrenom("Utilisateur");
        utilisateur.setNom("NOUVEL");
        utilisateur.setTelephone("0122334455");
        return utilisateur;
    }

    private Name getUid(Utilisateur usr) {
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(LdapConstants.UID, usr.getEmail()).build();
    }
}