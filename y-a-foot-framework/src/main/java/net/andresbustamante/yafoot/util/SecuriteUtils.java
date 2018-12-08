package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecuriteUtils {

    private static final String DIGEST = "SHA";
    private static final Log LOG = LogFactory.getLog(SecuriteUtils.class);

    /**
     * Crypter un mot de passe en clair en SHA1
     *
     * @param motDePasse Mot de passe à crypter (en clair)
     * @return Mot de passe crypté en base 64 et SHA1
     */
    public static String crypterMotDePasse(String motDePasse) throws ApplicationException {
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST);
            byte[] mdpDigest = digest.digest(motDePasse.getBytes());
            return Base64.getEncoder().encodeToString(mdpDigest);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Format de cryptage non supporté", e);
            throw new ApplicationException("security.digest.format.not.supported");
        }
    }
}