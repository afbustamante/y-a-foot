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
class UtilisateurDAOTest {

    private static final Utilisateur USR_TEST = new Utilisateur("test@email.com", "password", "TEST",
            "Utilisateur", "0123456789");

    @Value("${ldap.config.users.dn}")
    private String dnUtilisateurs;

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    @BeforeEach
    void setUp() throws Exception {
        utilisateurDAO.creerUtilisateur(USR_TEST, RolesEnum.JOUEUR);
    }

    @AfterEach
    void tearDown() throws Exception {
        utilisateurDAO.supprimerUtilisateur(USR_TEST, new RolesEnum[]{RolesEnum.JOUEUR});
    }

    @Test
    void creerUtilisateur() throws Exception {
        Utilisateur nouvelUtilisateur = getNouvelUtilisateur();

        utilisateurDAO.creerUtilisateur(nouvelUtilisateur, RolesEnum.JOUEUR);

        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(nouvelUtilisateur).toString());
        assertNotNull(utilisateur);
        assertNotNull(utilisateur.getNom());
        assertNotNull(utilisateur.getPrenom());
        assertNotNull(utilisateur.getEmail());
        assertNull(utilisateur.getMotDePasse());

        utilisateurDAO.supprimerUtilisateur(nouvelUtilisateur, new RolesEnum[]{RolesEnum.JOUEUR});
    }

    @Test
    void actualiserUtilisateur() throws Exception {
        Utilisateur utilisateurModifie = new Utilisateur();
        utilisateurModifie.setEmail(USR_TEST.getEmail());
        utilisateurModifie.setNom(USR_TEST.getNom() + " autre");
        utilisateurModifie.setPrenom(USR_TEST.getPrenom() + " autre");

        utilisateurDAO.actualiserUtilisateur(utilisateurModifie);

        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(utilisateurModifie).toString());
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
        utilisateurDAO.actualiserUtilisateur(utilisateur);
    }

    @Test
    void supprimerUtilisateur() throws Exception {
        Utilisateur nouvelUtilisateur = getNouvelUtilisateur();
        utilisateurDAO.creerUtilisateur(nouvelUtilisateur, RolesEnum.JOUEUR);

        utilisateurDAO.supprimerUtilisateur(nouvelUtilisateur, new RolesEnum[]{RolesEnum.JOUEUR});

        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(nouvelUtilisateur).toString());
        assertNull(utilisateur);
    }

    @Test
    void chercherUtilisateur() throws Exception {
        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(USR_TEST).toString());
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

    private Utilisateur getNouvelUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("nouvel.utilisateur@email.com");
        utilisateur.setMotDePasse("motDePasse");
        utilisateur.setPrenom("Utilisateur");
        utilisateur.setNom("NOUVEL");
        utilisateur.setTelephone("0122334455");
        return utilisateur;
    }

    private Name getIdAnnuaire(Utilisateur usr) {
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(LdapConstants.UID, usr.getEmail()).build();
    }
}