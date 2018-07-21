package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.util.ConstantesLdapUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.Name;

import static org.junit.Assert.*;

/**
 * @author andresbustamante
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/applicationContext.xml")
public class UtilisateurDAOTest {

    private static final Utilisateur USR_TEST = new Utilisateur("test@email.com", "password", "TEST",
            "Utilisateur", "0123456789");

    @Value("${ldap.users.dn}")
    private String dnUtilisateurs;

    @Autowired
    private UtilisateurDAO utilisateurDAO;

    @Before
    public void setUp() throws Exception {
        utilisateurDAO.creerUtilisateur(USR_TEST, RolesEnum.JOUEUR);
    }

    @After
    public void tearDown() throws Exception {
        utilisateurDAO.supprimerUtilisateur(USR_TEST, new RolesEnum[]{RolesEnum.JOUEUR});
    }

    @Test
    public void creerUtilisateur() throws Exception {
        Utilisateur nouvelUtilisateur = getNouvelUtilisateur();

        utilisateurDAO.creerUtilisateur(nouvelUtilisateur, RolesEnum.JOUEUR);

        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(nouvelUtilisateur).toString());
        assertNotNull(utilisateur);

        utilisateurDAO.supprimerUtilisateur(nouvelUtilisateur, new RolesEnum[]{RolesEnum.JOUEUR});
    }

    @Test
    public void actualiserUtilisateur() throws Exception {
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
    public void supprimerUtilisateur() throws Exception {
        Utilisateur nouvelUtilisateur = getNouvelUtilisateur();
        utilisateurDAO.creerUtilisateur(nouvelUtilisateur, RolesEnum.JOUEUR);

        utilisateurDAO.supprimerUtilisateur(nouvelUtilisateur, new RolesEnum[]{RolesEnum.JOUEUR});

        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(nouvelUtilisateur).toString());
        assertNull(utilisateur);
    }

    @Test
    public void chercherUtilisateur() throws Exception {
        Utilisateur utilisateur = utilisateurDAO.chercherUtilisateur(getIdAnnuaire(USR_TEST).toString());
        assertNotNull(utilisateur);
        assertNotNull(utilisateur.getEmail());
        assertEquals(USR_TEST.getEmail(), utilisateur.getEmail());
        assertNotNull(utilisateur.getNom());
        assertEquals(USR_TEST.getNom(), utilisateur.getNom());
        assertNotNull(utilisateur.getPrenom());
        assertEquals(USR_TEST.getPrenom(), utilisateur.getPrenom());
        assertNotNull(utilisateur.getNom());
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
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(ConstantesLdapUtils.UID, usr.getEmail()).build();
    }
}