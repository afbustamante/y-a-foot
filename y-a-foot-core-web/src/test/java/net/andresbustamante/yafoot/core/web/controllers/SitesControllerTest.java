package net.andresbustamante.yafoot.core.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.commons.model.UserContext;
import net.andresbustamante.yafoot.core.model.Site;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.services.SiteManagementService;
import net.andresbustamante.yafoot.core.services.SiteSearchService;
import net.andresbustamante.yafoot.core.web.mappers.SiteMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = {SitesController.class, ObjectMapper.class}, properties = {
        "api.config.public.url=http://myurl",
        "api.sites.one.path=/sites/%d"
}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class SitesControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SiteSearchService siteSearchService;

    @MockBean
    private SiteManagementService siteManagementService;

    @MockBean
    private PlayerSearchService playerSearchService;

    @MockBean
    private SiteMapper siteMapper;

    @Test
    void loadSites() throws Exception {
        // Given
        Site site1 = new Site(1);
        site1.setName("Site 1");
        Site site2 = new Site(2);
        site2.setName("Site 2");

        given(siteSearchService.findSites(any(UserContext.class))).willReturn(List.of(site1, site2));

        // When
        mvc.perform(get("/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void loadEmptySitesList() throws Exception {
        // Given
        given(siteSearchService.findSites(any(UserContext.class))).willReturn(Collections.emptyList());

        // When
        mvc.perform(get("/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void loadSitesWhileDatabaseIsUnavailable() throws Exception {
        // Given
        given(siteSearchService.findSites(any(UserContext.class))).willThrow(DatabaseException.class);
        given(playerSearchService.findPlayerByEmail(anyString(), any(UserContext.class)))
                .willThrow(DatabaseException.class);

        // When
        mvc.perform(get("/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addNewSite() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.SiteForm site = new net.andresbustamante.yafoot.web.dto.SiteForm();
        site.setName("Site name");
        site.setAddress("123 Fake Address");
        site.setPhoneNumber("01234567890");
        Integer id = 1;

        given(siteMapper.map(any(net.andresbustamante.yafoot.web.dto.SiteForm.class))).willReturn(new Site());
        given(siteManagementService.saveSite(any(Site.class), any(UserContext.class))).willReturn(id);

        // When
        mvc.perform(post("/sites")
                .content(objectMapper.writeValueAsString(site))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.LOCATION, String.format("http://myurl/sites/%d", id)));
    }

    @Test
    void addNewSiteWhileDatabaseIsUnavailable() throws Exception {
        // Given
        net.andresbustamante.yafoot.web.dto.SiteForm site = new net.andresbustamante.yafoot.web.dto.SiteForm();
        site.setName("Site name");
        site.setAddress("123 Fake Address");
        site.setPhoneNumber("01234567890");

        given(siteMapper.map(any(net.andresbustamante.yafoot.web.dto.SiteForm.class))).willReturn(new Site());
        given(siteManagementService.saveSite(any(Site.class), any(UserContext.class)))
                .willThrow(DatabaseException.class);

        // When
        mvc.perform(post("/sites")
                .content(objectMapper.writeValueAsString(site))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addNewInvalidSite() throws Exception {
        // Given
        // When
        mvc.perform(post("/sites")
                .content("{ \"abcd\": null }")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest());
    }
}
