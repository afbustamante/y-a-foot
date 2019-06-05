package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecuriteUtils {

    private static final String DIGEST = "SHA";
    private static final Logger LOG = LoggerFactory.getLogger(SecuriteUtils.class);

    private SecuriteUtils() {}

    /**
     * Crypter un mot de passe en clair en SHA1
     *
     * @param motDePasse Mot de passe à crypter (en clair)
     * @return Mot de passe crypté en base 64 et SHA1
     */
    public static String crypterMotDePasse(String motDePasse) throws ApplicationException {
        return crypterMotDePasse(motDePasse, DIGEST);
    }

    /**
     * Crypter un mot de passe en clair avec un algorithme d'encryption passé en paramètre
     *
     * @param motDePasse Mot de passe à crypter (en clair)
     * @param algorithme Algorithme d'encryption à utiliser
     * @return Mot de passe crypté en base 64 suite à l'application du digest
     */
    public static String crypterMotDePasse(String motDePasse, String algorithme) throws ApplicationException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithme);
            byte[] mdpDigest = digest.digest(motDePasse.getBytes());
            return Base64.getEncoder().encodeToString(mdpDigest);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Format de cryptage non supporté", e);
            throw new ApplicationException("security.digest.format.not.supported");
        }
    }
}
