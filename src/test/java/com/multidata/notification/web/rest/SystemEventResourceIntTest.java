package com.multidata.notification.web.rest;

import com.multidata.notification.NotificationApp;

import com.multidata.notification.domain.SystemEvent;
import com.multidata.notification.repository.SystemEventRepository;
import com.multidata.notification.service.SystemEventService;
import com.multidata.notification.service.dto.SystemEventDTO;
import com.multidata.notification.service.mapper.SystemEventMapper;
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
 * Test class for the SystemEventResource REST controller.
 *
 * @see SystemEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationApp.class)
public class SystemEventResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SystemEventRepository systemEventRepository;

    @Autowired
    private SystemEventMapper systemEventMapper;
    
    @Autowired
    private SystemEventService systemEventService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemEventMockMvc;

    private SystemEvent systemEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemEventResource systemEventResource = new SystemEventResource(systemEventService);
        this.restSystemEventMockMvc = MockMvcBuilders.standaloneSetup(systemEventResource)
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
    public static SystemEvent createEntity(EntityManager em) {
        SystemEvent systemEvent = new SystemEvent()
            .description(DEFAULT_DESCRIPTION);
        return systemEvent;
    }

    @Before
    public void initTest() {
        systemEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemEvent() throws Exception {
        int databaseSizeBeforeCreate = systemEventRepository.findAll().size();

        // Create the SystemEvent
        SystemEventDTO systemEventDTO = systemEventMapper.toDto(systemEvent);
        restSystemEventMockMvc.perform(post("/api/system-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventDTO)))
            .andExpect(status().isCreated());

        // Validate the SystemEvent in the database
        List<SystemEvent> systemEventList = systemEventRepository.findAll();
        assertThat(systemEventList).hasSize(databaseSizeBeforeCreate + 1);
        SystemEvent testSystemEvent = systemEventList.get(systemEventList.size() - 1);
        assertThat(testSystemEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSystemEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemEventRepository.findAll().size();

        // Create the SystemEvent with an existing ID
        systemEvent.setId(1L);
        SystemEventDTO systemEventDTO = systemEventMapper.toDto(systemEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemEventMockMvc.perform(post("/api/system-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemEvent in the database
        List<SystemEvent> systemEventList = systemEventRepository.findAll();
        assertThat(systemEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSystemEvents() throws Exception {
        // Initialize the database
        systemEventRepository.saveAndFlush(systemEvent);

        // Get all the systemEventList
        restSystemEventMockMvc.perform(get("/api/system-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getSystemEvent() throws Exception {
        // Initialize the database
        systemEventRepository.saveAndFlush(systemEvent);

        // Get the systemEvent
        restSystemEventMockMvc.perform(get("/api/system-events/{id}", systemEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemEvent.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemEvent() throws Exception {
        // Get the systemEvent
        restSystemEventMockMvc.perform(get("/api/system-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemEvent() throws Exception {
        // Initialize the database
        systemEventRepository.saveAndFlush(systemEvent);

        int databaseSizeBeforeUpdate = systemEventRepository.findAll().size();

        // Update the systemEvent
        SystemEvent updatedSystemEvent = systemEventRepository.findById(systemEvent.getId()).get();
        // Disconnect from session so that the updates on updatedSystemEvent are not directly saved in db
        em.detach(updatedSystemEvent);
        updatedSystemEvent
            .description(UPDATED_DESCRIPTION);
        SystemEventDTO systemEventDTO = systemEventMapper.toDto(updatedSystemEvent);

        restSystemEventMockMvc.perform(put("/api/system-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventDTO)))
            .andExpect(status().isOk());

        // Validate the SystemEvent in the database
        List<SystemEvent> systemEventList = systemEventRepository.findAll();
        assertThat(systemEventList).hasSize(databaseSizeBeforeUpdate);
        SystemEvent testSystemEvent = systemEventList.get(systemEventList.size() - 1);
        assertThat(testSystemEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemEvent() throws Exception {
        int databaseSizeBeforeUpdate = systemEventRepository.findAll().size();

        // Create the SystemEvent
        SystemEventDTO systemEventDTO = systemEventMapper.toDto(systemEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemEventMockMvc.perform(put("/api/system-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemEvent in the database
        List<SystemEvent> systemEventList = systemEventRepository.findAll();
        assertThat(systemEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSystemEvent() throws Exception {
        // Initialize the database
        systemEventRepository.saveAndFlush(systemEvent);

        int databaseSizeBeforeDelete = systemEventRepository.findAll().size();

        // Get the systemEvent
        restSystemEventMockMvc.perform(delete("/api/system-events/{id}", systemEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SystemEvent> systemEventList = systemEventRepository.findAll();
        assertThat(systemEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemEvent.class);
        SystemEvent systemEvent1 = new SystemEvent();
        systemEvent1.setId(1L);
        SystemEvent systemEvent2 = new SystemEvent();
        systemEvent2.setId(systemEvent1.getId());
        assertThat(systemEvent1).isEqualTo(systemEvent2);
        systemEvent2.setId(2L);
        assertThat(systemEvent1).isNotEqualTo(systemEvent2);
        systemEvent1.setId(null);
        assertThat(systemEvent1).isNotEqualTo(systemEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemEventDTO.class);
        SystemEventDTO systemEventDTO1 = new SystemEventDTO();
        systemEventDTO1.setId(1L);
        SystemEventDTO systemEventDTO2 = new SystemEventDTO();
        assertThat(systemEventDTO1).isNotEqualTo(systemEventDTO2);
        systemEventDTO2.setId(systemEventDTO1.getId());
        assertThat(systemEventDTO1).isEqualTo(systemEventDTO2);
        systemEventDTO2.setId(2L);
        assertThat(systemEventDTO1).isNotEqualTo(systemEventDTO2);
        systemEventDTO1.setId(null);
        assertThat(systemEventDTO1).isNotEqualTo(systemEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(systemEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(systemEventMapper.fromId(null)).isNull();
    }
}
