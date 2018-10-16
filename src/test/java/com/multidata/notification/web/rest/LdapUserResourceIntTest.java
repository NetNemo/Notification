package com.multidata.notification.web.rest;

import com.multidata.notification.NotificationApp;

import com.multidata.notification.domain.LdapUser;
import com.multidata.notification.repository.LdapUserRepository;
import com.multidata.notification.service.LdapUserService;
import com.multidata.notification.service.dto.LdapUserDTO;
import com.multidata.notification.service.mapper.LdapUserMapper;
import com.multidata.notification.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.multidata.notification.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LdapUserResource REST controller.
 *
 * @see LdapUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationApp.class)
public class LdapUserResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private LdapUserRepository ldapUserRepository;

    @Autowired
    private LdapUserMapper ldapUserMapper;
    
    @Autowired
    private LdapUserService ldapUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLdapUserMockMvc;

    private LdapUser ldapUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LdapUserResource ldapUserResource = new LdapUserResource(ldapUserService);
        this.restLdapUserMockMvc = MockMvcBuilders.standaloneSetup(ldapUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LdapUser createEntity(EntityManager em) {
        LdapUser ldapUser = new LdapUser()
            .userID(DEFAULT_USER_ID)
            .name(DEFAULT_NAME);
        return ldapUser;
    }

    @Before
    public void initTest() {
        ldapUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createLdapUser() throws Exception {
        int databaseSizeBeforeCreate = ldapUserRepository.findAll().size();

        // Create the LdapUser
        LdapUserDTO ldapUserDTO = ldapUserMapper.toDto(ldapUser);
        restLdapUserMockMvc.perform(post("/api/ldap-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ldapUserDTO)))
            .andExpect(status().isCreated());

        // Validate the LdapUser in the database
        List<LdapUser> ldapUserList = ldapUserRepository.findAll();
        assertThat(ldapUserList).hasSize(databaseSizeBeforeCreate + 1);
        LdapUser testLdapUser = ldapUserList.get(ldapUserList.size() - 1);
        assertThat(testLdapUser.getUserID()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testLdapUser.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createLdapUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ldapUserRepository.findAll().size();

        // Create the LdapUser with an existing ID
        ldapUser.setId(1L);
        LdapUserDTO ldapUserDTO = ldapUserMapper.toDto(ldapUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLdapUserMockMvc.perform(post("/api/ldap-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ldapUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LdapUser in the database
        List<LdapUser> ldapUserList = ldapUserRepository.findAll();
        assertThat(ldapUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLdapUsers() throws Exception {
        // Initialize the database
        ldapUserRepository.saveAndFlush(ldapUser);

        // Get all the ldapUserList
        restLdapUserMockMvc.perform(get("/api/ldap-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ldapUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getLdapUser() throws Exception {
        // Initialize the database
        ldapUserRepository.saveAndFlush(ldapUser);

        // Get the ldapUser
        restLdapUserMockMvc.perform(get("/api/ldap-users/{id}", ldapUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ldapUser.getId().intValue()))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLdapUser() throws Exception {
        // Get the ldapUser
        restLdapUserMockMvc.perform(get("/api/ldap-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLdapUser() throws Exception {
        // Initialize the database
        ldapUserRepository.saveAndFlush(ldapUser);

        int databaseSizeBeforeUpdate = ldapUserRepository.findAll().size();

        // Update the ldapUser
        LdapUser updatedLdapUser = ldapUserRepository.findById(ldapUser.getId()).get();
        // Disconnect from session so that the updates on updatedLdapUser are not directly saved in db
        em.detach(updatedLdapUser);
        updatedLdapUser
            .userID(UPDATED_USER_ID)
            .name(UPDATED_NAME);
        LdapUserDTO ldapUserDTO = ldapUserMapper.toDto(updatedLdapUser);

        restLdapUserMockMvc.perform(put("/api/ldap-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ldapUserDTO)))
            .andExpect(status().isOk());

        // Validate the LdapUser in the database
        List<LdapUser> ldapUserList = ldapUserRepository.findAll();
        assertThat(ldapUserList).hasSize(databaseSizeBeforeUpdate);
        LdapUser testLdapUser = ldapUserList.get(ldapUserList.size() - 1);
        assertThat(testLdapUser.getUserID()).isEqualTo(UPDATED_USER_ID);
        assertThat(testLdapUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingLdapUser() throws Exception {
        int databaseSizeBeforeUpdate = ldapUserRepository.findAll().size();

        // Create the LdapUser
        LdapUserDTO ldapUserDTO = ldapUserMapper.toDto(ldapUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLdapUserMockMvc.perform(put("/api/ldap-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ldapUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LdapUser in the database
        List<LdapUser> ldapUserList = ldapUserRepository.findAll();
        assertThat(ldapUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLdapUser() throws Exception {
        // Initialize the database
        ldapUserRepository.saveAndFlush(ldapUser);

        int databaseSizeBeforeDelete = ldapUserRepository.findAll().size();

        // Get the ldapUser
        restLdapUserMockMvc.perform(delete("/api/ldap-users/{id}", ldapUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LdapUser> ldapUserList = ldapUserRepository.findAll();
        assertThat(ldapUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LdapUser.class);
        LdapUser ldapUser1 = new LdapUser();
        ldapUser1.setId(1L);
        LdapUser ldapUser2 = new LdapUser();
        ldapUser2.setId(ldapUser1.getId());
        assertThat(ldapUser1).isEqualTo(ldapUser2);
        ldapUser2.setId(2L);
        assertThat(ldapUser1).isNotEqualTo(ldapUser2);
        ldapUser1.setId(null);
        assertThat(ldapUser1).isNotEqualTo(ldapUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LdapUserDTO.class);
        LdapUserDTO ldapUserDTO1 = new LdapUserDTO();
        ldapUserDTO1.setId(1L);
        LdapUserDTO ldapUserDTO2 = new LdapUserDTO();
        assertThat(ldapUserDTO1).isNotEqualTo(ldapUserDTO2);
        ldapUserDTO2.setId(ldapUserDTO1.getId());
        assertThat(ldapUserDTO1).isEqualTo(ldapUserDTO2);
        ldapUserDTO2.setId(2L);
        assertThat(ldapUserDTO1).isNotEqualTo(ldapUserDTO2);
        ldapUserDTO1.setId(null);
        assertThat(ldapUserDTO1).isNotEqualTo(ldapUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ldapUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ldapUserMapper.fromId(null)).isNull();
    }
}
