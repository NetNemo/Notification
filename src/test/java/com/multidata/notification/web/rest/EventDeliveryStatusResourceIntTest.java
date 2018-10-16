package com.multidata.notification.web.rest;

import com.multidata.notification.NotificationApp;

import com.multidata.notification.domain.EventDeliveryStatus;
import com.multidata.notification.repository.EventDeliveryStatusRepository;
import com.multidata.notification.service.EventDeliveryStatusService;
import com.multidata.notification.service.dto.EventDeliveryStatusDTO;
import com.multidata.notification.service.mapper.EventDeliveryStatusMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.multidata.notification.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventDeliveryStatusResource REST controller.
 *
 * @see EventDeliveryStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationApp.class)
public class EventDeliveryStatusResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private EventDeliveryStatusRepository eventDeliveryStatusRepository;

    @Autowired
    private EventDeliveryStatusMapper eventDeliveryStatusMapper;
    
    @Autowired
    private EventDeliveryStatusService eventDeliveryStatusService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventDeliveryStatusMockMvc;

    private EventDeliveryStatus eventDeliveryStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventDeliveryStatusResource eventDeliveryStatusResource = new EventDeliveryStatusResource(eventDeliveryStatusService);
        this.restEventDeliveryStatusMockMvc = MockMvcBuilders.standaloneSetup(eventDeliveryStatusResource)
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
    public static EventDeliveryStatus createEntity(EntityManager em) {
        EventDeliveryStatus eventDeliveryStatus = new EventDeliveryStatus()
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
        return eventDeliveryStatus;
    }

    @Before
    public void initTest() {
        eventDeliveryStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventDeliveryStatus() throws Exception {
        int databaseSizeBeforeCreate = eventDeliveryStatusRepository.findAll().size();

        // Create the EventDeliveryStatus
        EventDeliveryStatusDTO eventDeliveryStatusDTO = eventDeliveryStatusMapper.toDto(eventDeliveryStatus);
        restEventDeliveryStatusMockMvc.perform(post("/api/event-delivery-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDeliveryStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the EventDeliveryStatus in the database
        List<EventDeliveryStatus> eventDeliveryStatusList = eventDeliveryStatusRepository.findAll();
        assertThat(eventDeliveryStatusList).hasSize(databaseSizeBeforeCreate + 1);
        EventDeliveryStatus testEventDeliveryStatus = eventDeliveryStatusList.get(eventDeliveryStatusList.size() - 1);
        assertThat(testEventDeliveryStatus.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEventDeliveryStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createEventDeliveryStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventDeliveryStatusRepository.findAll().size();

        // Create the EventDeliveryStatus with an existing ID
        eventDeliveryStatus.setId(1L);
        EventDeliveryStatusDTO eventDeliveryStatusDTO = eventDeliveryStatusMapper.toDto(eventDeliveryStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventDeliveryStatusMockMvc.perform(post("/api/event-delivery-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDeliveryStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventDeliveryStatus in the database
        List<EventDeliveryStatus> eventDeliveryStatusList = eventDeliveryStatusRepository.findAll();
        assertThat(eventDeliveryStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEventDeliveryStatuses() throws Exception {
        // Initialize the database
        eventDeliveryStatusRepository.saveAndFlush(eventDeliveryStatus);

        // Get all the eventDeliveryStatusList
        restEventDeliveryStatusMockMvc.perform(get("/api/event-delivery-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventDeliveryStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getEventDeliveryStatus() throws Exception {
        // Initialize the database
        eventDeliveryStatusRepository.saveAndFlush(eventDeliveryStatus);

        // Get the eventDeliveryStatus
        restEventDeliveryStatusMockMvc.perform(get("/api/event-delivery-statuses/{id}", eventDeliveryStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventDeliveryStatus.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventDeliveryStatus() throws Exception {
        // Get the eventDeliveryStatus
        restEventDeliveryStatusMockMvc.perform(get("/api/event-delivery-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventDeliveryStatus() throws Exception {
        // Initialize the database
        eventDeliveryStatusRepository.saveAndFlush(eventDeliveryStatus);

        int databaseSizeBeforeUpdate = eventDeliveryStatusRepository.findAll().size();

        // Update the eventDeliveryStatus
        EventDeliveryStatus updatedEventDeliveryStatus = eventDeliveryStatusRepository.findById(eventDeliveryStatus.getId()).get();
        // Disconnect from session so that the updates on updatedEventDeliveryStatus are not directly saved in db
        em.detach(updatedEventDeliveryStatus);
        updatedEventDeliveryStatus
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        EventDeliveryStatusDTO eventDeliveryStatusDTO = eventDeliveryStatusMapper.toDto(updatedEventDeliveryStatus);

        restEventDeliveryStatusMockMvc.perform(put("/api/event-delivery-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDeliveryStatusDTO)))
            .andExpect(status().isOk());

        // Validate the EventDeliveryStatus in the database
        List<EventDeliveryStatus> eventDeliveryStatusList = eventDeliveryStatusRepository.findAll();
        assertThat(eventDeliveryStatusList).hasSize(databaseSizeBeforeUpdate);
        EventDeliveryStatus testEventDeliveryStatus = eventDeliveryStatusList.get(eventDeliveryStatusList.size() - 1);
        assertThat(testEventDeliveryStatus.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEventDeliveryStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingEventDeliveryStatus() throws Exception {
        int databaseSizeBeforeUpdate = eventDeliveryStatusRepository.findAll().size();

        // Create the EventDeliveryStatus
        EventDeliveryStatusDTO eventDeliveryStatusDTO = eventDeliveryStatusMapper.toDto(eventDeliveryStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDeliveryStatusMockMvc.perform(put("/api/event-delivery-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDeliveryStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventDeliveryStatus in the database
        List<EventDeliveryStatus> eventDeliveryStatusList = eventDeliveryStatusRepository.findAll();
        assertThat(eventDeliveryStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventDeliveryStatus() throws Exception {
        // Initialize the database
        eventDeliveryStatusRepository.saveAndFlush(eventDeliveryStatus);

        int databaseSizeBeforeDelete = eventDeliveryStatusRepository.findAll().size();

        // Get the eventDeliveryStatus
        restEventDeliveryStatusMockMvc.perform(delete("/api/event-delivery-statuses/{id}", eventDeliveryStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EventDeliveryStatus> eventDeliveryStatusList = eventDeliveryStatusRepository.findAll();
        assertThat(eventDeliveryStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDeliveryStatus.class);
        EventDeliveryStatus eventDeliveryStatus1 = new EventDeliveryStatus();
        eventDeliveryStatus1.setId(1L);
        EventDeliveryStatus eventDeliveryStatus2 = new EventDeliveryStatus();
        eventDeliveryStatus2.setId(eventDeliveryStatus1.getId());
        assertThat(eventDeliveryStatus1).isEqualTo(eventDeliveryStatus2);
        eventDeliveryStatus2.setId(2L);
        assertThat(eventDeliveryStatus1).isNotEqualTo(eventDeliveryStatus2);
        eventDeliveryStatus1.setId(null);
        assertThat(eventDeliveryStatus1).isNotEqualTo(eventDeliveryStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDeliveryStatusDTO.class);
        EventDeliveryStatusDTO eventDeliveryStatusDTO1 = new EventDeliveryStatusDTO();
        eventDeliveryStatusDTO1.setId(1L);
        EventDeliveryStatusDTO eventDeliveryStatusDTO2 = new EventDeliveryStatusDTO();
        assertThat(eventDeliveryStatusDTO1).isNotEqualTo(eventDeliveryStatusDTO2);
        eventDeliveryStatusDTO2.setId(eventDeliveryStatusDTO1.getId());
        assertThat(eventDeliveryStatusDTO1).isEqualTo(eventDeliveryStatusDTO2);
        eventDeliveryStatusDTO2.setId(2L);
        assertThat(eventDeliveryStatusDTO1).isNotEqualTo(eventDeliveryStatusDTO2);
        eventDeliveryStatusDTO1.setId(null);
        assertThat(eventDeliveryStatusDTO1).isNotEqualTo(eventDeliveryStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eventDeliveryStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eventDeliveryStatusMapper.fromId(null)).isNull();
    }
}
