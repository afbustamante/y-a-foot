package net.andresbustamante.yafoot.ws;

import javax.jws.WebService;

/**
 * @author andresbustamante
 */
@WebService(serviceName = "AuthentificationUtilisateurService", portName = "AuthentificationUtilisateurPort",
        endpointInterface = "net.andresbustamante.yafoot.ws.AuthentificationUtilisateurPortType",
        targetNamespace = "http://andresbustamante.net/yafoot/ws",
        wsdlLocation = "WEB-INF/wsdl/authentification_utilisateur.wsdl")
public class AuthentificationUtilisateurWS {

    public void authentifierUtilisateur(javax.xml.ws.Holder<net.andresbustamante.yafoot.xs.UtilisateurType> utilisateur,
                                        net.andresbustamante.yafoot.xs.Contexte contexte)
            throws BDDException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean recupererMdpOublie(java.lang.String email, net.andresbustamante.yafoot.xs.Contexte contexte)
            throws BDDException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
