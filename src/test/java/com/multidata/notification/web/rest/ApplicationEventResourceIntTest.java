package com.multidata.notification.web.rest;

import com.multidata.notification.NotificationApp;

import com.multidata.notification.domain.ApplicationEvent;
import com.multidata.notification.repository.ApplicationEventRepository;
import com.multidata.notification.service.ApplicationEventService;
import com.multidata.notification.service.dto.ApplicationEventDTO;
import com.multidata.notification.service.mapper.ApplicationEventMapper;
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
 * Test class for the ApplicationEventResource REST controller.
 *
 * @see ApplicationEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationApp.class)
public class ApplicationEventResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ApplicationEventRepository applicationEventRepository;

    @Autowired
    private ApplicationEventMapper applicationEventMapper;
    
    @Autowired
    private ApplicationEventService applicationEventService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationEventMockMvc;

    private ApplicationEvent applicationEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationEventResource applicationEventResource = new ApplicationEventResource(applicationEventService);
        this.restApplicationEventMockMvc = MockMvcBuilders.standaloneSetup(applicationEventResource)
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
    public static ApplicationEvent createEntity(EntityManager em) {
        ApplicationEvent applicationEvent = new ApplicationEvent()
            .description(DEFAULT_DESCRIPTION);
        return applicationEvent;
    }

    @Before
    public void initTest() {
        applicationEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationEvent() throws Exception {
        int databaseSizeBeforeCreate = applicationEventRepository.findAll().size();

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);
        restApplicationEventMockMvc.perform(post("/api/application-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationEventDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationEvent in the database
        List<ApplicationEvent> applicationEventList = applicationEventRepository.findAll();
        assertThat(applicationEventList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationEvent testApplicationEvent = applicationEventList.get(applicationEventList.size() - 1);
        assertThat(testApplicationEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createApplicationEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationEventRepository.findAll().size();

        // Create the ApplicationEvent with an existing ID
        applicationEvent.setId(1L);
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationEventMockMvc.perform(post("/api/application-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        List<ApplicationEvent> applicationEventList = applicationEventRepository.findAll();
        assertThat(applicationEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApplicationEvents() throws Exception {
        // Initialize the database
        applicationEventRepository.saveAndFlush(applicationEvent);

        // Get all the applicationEventList
        restApplicationEventMockMvc.perform(get("/api/application-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getApplicationEvent() throws Exception {
        // Initialize the database
        applicationEventRepository.saveAndFlush(applicationEvent);

        // Get the applicationEvent
        restApplicationEventMockMvc.perform(get("/api/application-events/{id}", applicationEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationEvent.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationEvent() throws Exception {
        // Get the applicationEvent
        restApplicationEventMockMvc.perform(get("/api/application-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationEvent() throws Exception {
        // Initialize the database
        applicationEventRepository.saveAndFlush(applicationEvent);

        int databaseSizeBeforeUpdate = applicationEventRepository.findAll().size();

        // Update the applicationEvent
        ApplicationEvent updatedApplicationEvent = applicationEventRepository.findById(applicationEvent.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationEvent are not directly saved in db
        em.detach(updatedApplicationEvent);
        updatedApplicationEvent
            .description(UPDATED_DESCRIPTION);
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(updatedApplicationEvent);

        restApplicationEventMockMvc.perform(put("/api/application-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationEventDTO)))
            .andExpect(status().isOk());

        // Validate the ApplicationEvent in the database
        List<ApplicationEvent> applicationEventList = applicationEventRepository.findAll();
        assertThat(applicationEventList).hasSize(databaseSizeBeforeUpdate);
        ApplicationEvent testApplicationEvent = applicationEventList.get(applicationEventList.size() - 1);
        assertThat(testApplicationEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationEvent() throws Exception {
        int databaseSizeBeforeUpdate = applicationEventRepository.findAll().size();

        // Create the ApplicationEvent
        ApplicationEventDTO applicationEventDTO = applicationEventMapper.toDto(applicationEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationEventMockMvc.perform(put("/api/application-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationEvent in the database
        List<ApplicationEvent> applicationEventList = applicationEventRepository.findAll();
        assertThat(applicationEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicationEvent() throws Exception {
        // Initialize the database
        applicationEventRepository.saveAndFlush(applicationEvent);

        int databaseSizeBeforeDelete = applicationEventRepository.findAll().size();

        // Get the applicationEvent
        restApplicationEventMockMvc.perform(delete("/api/application-events/{id}", applicationEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApplicationEvent> applicationEventList = applicationEventRepository.findAll();
        assertThat(applicationEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationEvent.class);
        ApplicationEvent applicationEvent1 = new ApplicationEvent();
        applicationEvent1.setId(1L);
        ApplicationEvent applicationEvent2 = new ApplicationEvent();
        applicationEvent2.setId(applicationEvent1.getId());
        assertThat(applicationEvent1).isEqualTo(applicationEvent2);
        applicationEvent2.setId(2L);
        assertThat(applicationEvent1).isNotEqualTo(applicationEvent2);
        applicationEvent1.setId(null);
        assertThat(applicationEvent1).isNotEqualTo(applicationEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationEventDTO.class);
        ApplicationEventDTO applicationEventDTO1 = new ApplicationEventDTO();
        applicationEventDTO1.setId(1L);
        ApplicationEventDTO applicationEventDTO2 = new ApplicationEventDTO();
        assertThat(applicationEventDTO1).isNotEqualTo(applicationEventDTO2);
        applicationEventDTO2.setId(applicationEventDTO1.getId());
        assertThat(applicationEventDTO1).isEqualTo(applicationEventDTO2);
        applicationEventDTO2.setId(2L);
        assertThat(applicationEventDTO1).isNotEqualTo(applicationEventDTO2);
        applicationEventDTO1.setId(null);
        assertThat(applicationEventDTO1).isNotEqualTo(applicationEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationEventMapper.fromId(null)).isNull();
    }
}
