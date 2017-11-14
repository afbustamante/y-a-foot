package net.andresbustamante.yafoot.uiservices;

import net.andresbustamante.yafoot.ws.BDDException;
import net.andresbustamante.yafoot.ws.OrganisationMatchsPortType;
import net.andresbustamante.yafoot.ws.OrganisationMatchsService;
import net.andresbustamante.yafoot.xs.Match;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author andresbustamante
 */
public class OrganisationMatchsUIService extends AbstractUIService {

    private final Log log = LogFactory.getLog(OrganisationMatchsUIService.class);

    public String creerMatch(Match match) throws BDDException {
        OrganisationMatchsService organisationMatchsService = new OrganisationMatchsService();
        OrganisationMatchsPortType organisationMatchsPortType = organisationMatchsService.getOrganisationMatchsPort();
        return organisationMatchsPortType.creerMatch(match, getContexte());
    }
}
