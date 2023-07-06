package org.pierre.shareazade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.pierre.shareazade.web.rest.TestUtil.sameInstant;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pierre.shareazade.IntegrationTest;
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.domain.ShareRide;
import org.pierre.shareazade.domain.ShareUser;
import org.pierre.shareazade.domain.enumeration.RideType;
import org.pierre.shareazade.repository.ShareRideRepository;
import org.pierre.shareazade.service.ShareRideService;
import org.pierre.shareazade.service.criteria.ShareRideCriteria;
import org.pierre.shareazade.service.dto.ShareRideDTO;
import org.pierre.shareazade.service.mapper.ShareRideMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ShareRideResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShareRideResourceIT {

    private static final ZonedDateTime DEFAULT_RIDE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RIDE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RIDE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final RideType DEFAULT_RIDE_TYPE = RideType.OFFER;
    private static final RideType UPDATED_RIDE_TYPE = RideType.REQUEST;

    private static final String DEFAULT_RIDE_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_RIDE_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/share-rides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShareRideRepository shareRideRepository;

    @Mock
    private ShareRideRepository shareRideRepositoryMock;

    @Autowired
    private ShareRideMapper shareRideMapper;

    @Mock
    private ShareRideService shareRideServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShareRideMockMvc;

    private ShareRide shareRide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareRide createEntity(EntityManager em) {
        ShareRide shareRide = new ShareRide()
            .rideDateTime(DEFAULT_RIDE_DATE_TIME)
            .rideType(DEFAULT_RIDE_TYPE)
            .rideComments(DEFAULT_RIDE_COMMENTS);
        return shareRide;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareRide createUpdatedEntity(EntityManager em) {
        ShareRide shareRide = new ShareRide()
            .rideDateTime(UPDATED_RIDE_DATE_TIME)
            .rideType(UPDATED_RIDE_TYPE)
            .rideComments(UPDATED_RIDE_COMMENTS);
        return shareRide;
    }

    @BeforeEach
    public void initTest() {
        shareRide = createEntity(em);
    }

    @Test
    @Transactional
    void createShareRide() throws Exception {
        int databaseSizeBeforeCreate = shareRideRepository.findAll().size();
        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);
        restShareRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareRideDTO)))
            .andExpect(status().isCreated());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeCreate + 1);
        ShareRide testShareRide = shareRideList.get(shareRideList.size() - 1);
        assertThat(testShareRide.getRideDateTime()).isEqualTo(DEFAULT_RIDE_DATE_TIME);
        assertThat(testShareRide.getRideType()).isEqualTo(DEFAULT_RIDE_TYPE);
        assertThat(testShareRide.getRideComments()).isEqualTo(DEFAULT_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void createShareRideWithExistingId() throws Exception {
        // Create the ShareRide with an existing ID
        shareRide.setId(1L);
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        int databaseSizeBeforeCreate = shareRideRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareRideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShareRides() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList
        restShareRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareRide.getId().intValue())))
            .andExpect(jsonPath("$.[*].rideDateTime").value(hasItem(sameInstant(DEFAULT_RIDE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rideType").value(hasItem(DEFAULT_RIDE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rideComments").value(hasItem(DEFAULT_RIDE_COMMENTS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShareRidesWithEagerRelationshipsIsEnabled() throws Exception {
        when(shareRideServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShareRideMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shareRideServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShareRidesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shareRideServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShareRideMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shareRideRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShareRide() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get the shareRide
        restShareRideMockMvc
            .perform(get(ENTITY_API_URL_ID, shareRide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shareRide.getId().intValue()))
            .andExpect(jsonPath("$.rideDateTime").value(sameInstant(DEFAULT_RIDE_DATE_TIME)))
            .andExpect(jsonPath("$.rideType").value(DEFAULT_RIDE_TYPE.toString()))
            .andExpect(jsonPath("$.rideComments").value(DEFAULT_RIDE_COMMENTS.toString()));
    }

    @Test
    @Transactional
    void getShareRidesByIdFiltering() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        Long id = shareRide.getId();

        defaultShareRideShouldBeFound("id.equals=" + id);
        defaultShareRideShouldNotBeFound("id.notEquals=" + id);

        defaultShareRideShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShareRideShouldNotBeFound("id.greaterThan=" + id);

        defaultShareRideShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShareRideShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime equals to DEFAULT_RIDE_DATE_TIME
        defaultShareRideShouldBeFound("rideDateTime.equals=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the shareRideList where rideDateTime equals to UPDATED_RIDE_DATE_TIME
        defaultShareRideShouldNotBeFound("rideDateTime.equals=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime in DEFAULT_RIDE_DATE_TIME or UPDATED_RIDE_DATE_TIME
        defaultShareRideShouldBeFound("rideDateTime.in=" + DEFAULT_RIDE_DATE_TIME + "," + UPDATED_RIDE_DATE_TIME);

        // Get all the shareRideList where rideDateTime equals to UPDATED_RIDE_DATE_TIME
        defaultShareRideShouldNotBeFound("rideDateTime.in=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime is not null
        defaultShareRideShouldBeFound("rideDateTime.specified=true");

        // Get all the shareRideList where rideDateTime is null
        defaultShareRideShouldNotBeFound("rideDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime is greater than or equal to DEFAULT_RIDE_DATE_TIME
        defaultShareRideShouldBeFound("rideDateTime.greaterThanOrEqual=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the shareRideList where rideDateTime is greater than or equal to UPDATED_RIDE_DATE_TIME
        defaultShareRideShouldNotBeFound("rideDateTime.greaterThanOrEqual=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime is less than or equal to DEFAULT_RIDE_DATE_TIME
        defaultShareRideShouldBeFound("rideDateTime.lessThanOrEqual=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the shareRideList where rideDateTime is less than or equal to SMALLER_RIDE_DATE_TIME
        defaultShareRideShouldNotBeFound("rideDateTime.lessThanOrEqual=" + SMALLER_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime is less than DEFAULT_RIDE_DATE_TIME
        defaultShareRideShouldNotBeFound("rideDateTime.lessThan=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the shareRideList where rideDateTime is less than UPDATED_RIDE_DATE_TIME
        defaultShareRideShouldBeFound("rideDateTime.lessThan=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideDateTime is greater than DEFAULT_RIDE_DATE_TIME
        defaultShareRideShouldNotBeFound("rideDateTime.greaterThan=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the shareRideList where rideDateTime is greater than SMALLER_RIDE_DATE_TIME
        defaultShareRideShouldBeFound("rideDateTime.greaterThan=" + SMALLER_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideType equals to DEFAULT_RIDE_TYPE
        defaultShareRideShouldBeFound("rideType.equals=" + DEFAULT_RIDE_TYPE);

        // Get all the shareRideList where rideType equals to UPDATED_RIDE_TYPE
        defaultShareRideShouldNotBeFound("rideType.equals=" + UPDATED_RIDE_TYPE);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideTypeIsInShouldWork() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideType in DEFAULT_RIDE_TYPE or UPDATED_RIDE_TYPE
        defaultShareRideShouldBeFound("rideType.in=" + DEFAULT_RIDE_TYPE + "," + UPDATED_RIDE_TYPE);

        // Get all the shareRideList where rideType equals to UPDATED_RIDE_TYPE
        defaultShareRideShouldNotBeFound("rideType.in=" + UPDATED_RIDE_TYPE);
    }

    @Test
    @Transactional
    void getAllShareRidesByRideTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        // Get all the shareRideList where rideType is not null
        defaultShareRideShouldBeFound("rideType.specified=true");

        // Get all the shareRideList where rideType is null
        defaultShareRideShouldNotBeFound("rideType.specified=false");
    }

    @Test
    @Transactional
    void getAllShareRidesByRideCityFromIsEqualToSomething() throws Exception {
        ShareCity rideCityFrom;
        if (TestUtil.findAll(em, ShareCity.class).isEmpty()) {
            shareRideRepository.saveAndFlush(shareRide);
            rideCityFrom = ShareCityResourceIT.createEntity(em);
        } else {
            rideCityFrom = TestUtil.findAll(em, ShareCity.class).get(0);
        }
        em.persist(rideCityFrom);
        em.flush();
        shareRide.setRideCityFrom(rideCityFrom);
        shareRideRepository.saveAndFlush(shareRide);
        Long rideCityFromId = rideCityFrom.getId();

        // Get all the shareRideList where rideCityFrom equals to rideCityFromId
        defaultShareRideShouldBeFound("rideCityFromId.equals=" + rideCityFromId);

        // Get all the shareRideList where rideCityFrom equals to (rideCityFromId + 1)
        defaultShareRideShouldNotBeFound("rideCityFromId.equals=" + (rideCityFromId + 1));
    }

    @Test
    @Transactional
    void getAllShareRidesByRideCityToIsEqualToSomething() throws Exception {
        ShareCity rideCityTo;
        if (TestUtil.findAll(em, ShareCity.class).isEmpty()) {
            shareRideRepository.saveAndFlush(shareRide);
            rideCityTo = ShareCityResourceIT.createEntity(em);
        } else {
            rideCityTo = TestUtil.findAll(em, ShareCity.class).get(0);
        }
        em.persist(rideCityTo);
        em.flush();
        shareRide.setRideCityTo(rideCityTo);
        shareRideRepository.saveAndFlush(shareRide);
        Long rideCityToId = rideCityTo.getId();

        // Get all the shareRideList where rideCityTo equals to rideCityToId
        defaultShareRideShouldBeFound("rideCityToId.equals=" + rideCityToId);

        // Get all the shareRideList where rideCityTo equals to (rideCityToId + 1)
        defaultShareRideShouldNotBeFound("rideCityToId.equals=" + (rideCityToId + 1));
    }

    @Test
    @Transactional
    void getAllShareRidesByRideUserIsEqualToSomething() throws Exception {
        ShareUser rideUser;
        if (TestUtil.findAll(em, ShareUser.class).isEmpty()) {
            shareRideRepository.saveAndFlush(shareRide);
            rideUser = ShareUserResourceIT.createEntity(em);
        } else {
            rideUser = TestUtil.findAll(em, ShareUser.class).get(0);
        }
        em.persist(rideUser);
        em.flush();
        shareRide.setRideUser(rideUser);
        shareRideRepository.saveAndFlush(shareRide);
        Long rideUserId = rideUser.getId();

        // Get all the shareRideList where rideUser equals to rideUserId
        defaultShareRideShouldBeFound("rideUserId.equals=" + rideUserId);

        // Get all the shareRideList where rideUser equals to (rideUserId + 1)
        defaultShareRideShouldNotBeFound("rideUserId.equals=" + (rideUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShareRideShouldBeFound(String filter) throws Exception {
        restShareRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareRide.getId().intValue())))
            .andExpect(jsonPath("$.[*].rideDateTime").value(hasItem(sameInstant(DEFAULT_RIDE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rideType").value(hasItem(DEFAULT_RIDE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rideComments").value(hasItem(DEFAULT_RIDE_COMMENTS.toString())));

        // Check, that the count call also returns 1
        restShareRideMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShareRideShouldNotBeFound(String filter) throws Exception {
        restShareRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShareRideMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShareRide() throws Exception {
        // Get the shareRide
        restShareRideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShareRide() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();

        // Update the shareRide
        ShareRide updatedShareRide = shareRideRepository.findById(shareRide.getId()).get();
        // Disconnect from session so that the updates on updatedShareRide are not directly saved in db
        em.detach(updatedShareRide);
        updatedShareRide.rideDateTime(UPDATED_RIDE_DATE_TIME).rideType(UPDATED_RIDE_TYPE).rideComments(UPDATED_RIDE_COMMENTS);
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(updatedShareRide);

        restShareRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareRideDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareRideDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
        ShareRide testShareRide = shareRideList.get(shareRideList.size() - 1);
        assertThat(testShareRide.getRideDateTime()).isEqualTo(UPDATED_RIDE_DATE_TIME);
        assertThat(testShareRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testShareRide.getRideComments()).isEqualTo(UPDATED_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingShareRide() throws Exception {
        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();
        shareRide.setId(count.incrementAndGet());

        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareRideDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShareRide() throws Exception {
        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();
        shareRide.setId(count.incrementAndGet());

        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShareRide() throws Exception {
        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();
        shareRide.setId(count.incrementAndGet());

        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareRideMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareRideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShareRideWithPatch() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();

        // Update the shareRide using partial update
        ShareRide partialUpdatedShareRide = new ShareRide();
        partialUpdatedShareRide.setId(shareRide.getId());

        partialUpdatedShareRide.rideType(UPDATED_RIDE_TYPE);

        restShareRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareRide))
            )
            .andExpect(status().isOk());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
        ShareRide testShareRide = shareRideList.get(shareRideList.size() - 1);
        assertThat(testShareRide.getRideDateTime()).isEqualTo(DEFAULT_RIDE_DATE_TIME);
        assertThat(testShareRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testShareRide.getRideComments()).isEqualTo(DEFAULT_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateShareRideWithPatch() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();

        // Update the shareRide using partial update
        ShareRide partialUpdatedShareRide = new ShareRide();
        partialUpdatedShareRide.setId(shareRide.getId());

        partialUpdatedShareRide.rideDateTime(UPDATED_RIDE_DATE_TIME).rideType(UPDATED_RIDE_TYPE).rideComments(UPDATED_RIDE_COMMENTS);

        restShareRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareRide))
            )
            .andExpect(status().isOk());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
        ShareRide testShareRide = shareRideList.get(shareRideList.size() - 1);
        assertThat(testShareRide.getRideDateTime()).isEqualTo(UPDATED_RIDE_DATE_TIME);
        assertThat(testShareRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testShareRide.getRideComments()).isEqualTo(UPDATED_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingShareRide() throws Exception {
        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();
        shareRide.setId(count.incrementAndGet());

        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shareRideDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShareRide() throws Exception {
        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();
        shareRide.setId(count.incrementAndGet());

        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShareRide() throws Exception {
        int databaseSizeBeforeUpdate = shareRideRepository.findAll().size();
        shareRide.setId(count.incrementAndGet());

        // Create the ShareRide
        ShareRideDTO shareRideDTO = shareRideMapper.toDto(shareRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareRideMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shareRideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareRide in the database
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShareRide() throws Exception {
        // Initialize the database
        shareRideRepository.saveAndFlush(shareRide);

        int databaseSizeBeforeDelete = shareRideRepository.findAll().size();

        // Delete the shareRide
        restShareRideMockMvc
            .perform(delete(ENTITY_API_URL_ID, shareRide.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShareRide> shareRideList = shareRideRepository.findAll();
        assertThat(shareRideList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
