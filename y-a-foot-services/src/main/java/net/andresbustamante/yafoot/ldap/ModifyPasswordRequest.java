package net.andresbustamante.yafoot.ldap;

import javax.naming.CannotProceedException;
import javax.naming.SizeLimitExceededException;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;

/**
 * A class conforming to the ExtendedRequest that implements the
 * Modify Password extended operation.
 *
 * @see javax.naming.ldap.ExtendedRequest
 */
public class ModifyPasswordRequest implements ExtendedRequest {

    /**
     * The BER tag for the modify password dn entry
     */
    private static final byte LDAP_TAG_EXOP_X_MODIFY_PASS_ID = (byte) 0x80;
    /**
     * The BER tag for the modify password new password entry
     */
    private static final byte LDAP_TAG_EXOP_X_MODIFY_PASS_NEW = (byte) 0x82;
    /**
     * The dn we want to change
     */
    private String mDn;
    /**
     * The password to change to
     */
    private String mPassword;

    /**
     * Creates a new <code>ModifyPasswordRequest</code> instance.
     *
     * @param dn       the dn whose password is to change
     * @param password the new password
     * @throws NullPointerException       if dn or password is null
     * @throws CannotProceedException When the dn is empty
     * @throws SizeLimitExceededException when the dn or password
     *                                    is too long
     */
    public ModifyPasswordRequest(String dn, String password)
            throws NullPointerException, CannotProceedException, SizeLimitExceededException {
        if (dn == null) {
            throw new NullPointerException("dn cannot be null");
        }

        if (password == null) {
            throw new NullPointerException("password cannot be null");
        }

        int dnlen = dn.length();
        int passlen = password.length();
        int totallen = 4 + dnlen + passlen;

        if (dnlen <= 0) {
            throw new CannotProceedException("dn cannot be empty");
        }

        if (dnlen > 0xFF) {
            throw new SizeLimitExceededException(
                    "dn cannot be larger then 255 characters");
        }

        if (passlen <= 0) {
            throw new CannotProceedException(
                    "password cannot be empty");
        }

        if (passlen > 0xFF) {
            throw new SizeLimitExceededException(
                    "password cannot be larger then 255 characters");
        }

        if (totallen > 0xFF) {
            throw new SizeLimitExceededException(
                    "the lengh of the dn + the lengh of the password cannot" +
                            " exceed 251 characters");
        }

        this.mDn = dn;
        this.mPassword = password;
    }

    /**
     * Returns the ID of this extended operation.
     *
     * @return a String with the OID of this operation
     */
    @Override
    public String getID() {
        return "1.3.6.1.4.1.4203.1.11.1";
    }

    /**
     * Get the BER encoded value for this operation.
     *
     * @return a bytearray containing the BER sequence.
     */
    @Override
    public byte[] getEncodedValue() {
        byte[] password = mPassword.getBytes();
        byte[] dn = mDn.getBytes();

        // Sequence tag (1) + sequence length (1) + dn tag (1) +
        // dn length (1) + dn (variable) + password tag (1) +
        // password length (1) + password (variable)
        int encodedLength = 6 + dn.length + password.length;

        byte[] encoded = new byte[encodedLength];

        int i = 0;
        encoded[i++] = (byte) 0x30; // sequence start
        // length of body
        encoded[i++] = (byte) (4 + dn.length + password.length);


        encoded[i++] = LDAP_TAG_EXOP_X_MODIFY_PASS_ID;
        encoded[i++] = (byte) dn.length;

        System.arraycopy(dn, 0, encoded, i, dn.length);
        i += dn.length;

        encoded[i++] = LDAP_TAG_EXOP_X_MODIFY_PASS_NEW;
        encoded[i++] = (byte) password.length;

        System.arraycopy(password, 0, encoded, i, password.length);

        return encoded;
    }

    /**
     * Creates the extended response.  With OpenLDAP, the extended
     * operation for Password modification doesn't create a
     * response so we just return null here.
     *
     * @param id       the OID of the response
     * @param berValue the BER encoded value of the response
     * @param offset   the offset
     * @param length   the length of the response
     * @return returns null as the modify password operation doesn't
     * generate a response.
     */
    public ExtendedResponse createExtendedResponse(String id, byte[] berValue, int offset, int length) {
        return null;
    }
}
