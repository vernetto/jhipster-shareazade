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
import org.pierre.shareazade.domain.City;
import org.pierre.shareazade.domain.Ride;
import org.pierre.shareazade.domain.Users;
import org.pierre.shareazade.domain.enumeration.RideType;
import org.pierre.shareazade.repository.RideRepository;
import org.pierre.shareazade.service.RideService;
import org.pierre.shareazade.service.criteria.RideCriteria;
import org.pierre.shareazade.service.dto.RideDTO;
import org.pierre.shareazade.service.mapper.RideMapper;
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
 * Integration tests for the {@link RideResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RideResourceIT {

    private static final ZonedDateTime DEFAULT_RIDE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RIDE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RIDE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_RIDE_CITY_FROM = "AAAAAAAAAA";
    private static final String UPDATED_RIDE_CITY_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_RIDE_CITY_TO = "AAAAAAAAAA";
    private static final String UPDATED_RIDE_CITY_TO = "BBBBBBBBBB";

    private static final RideType DEFAULT_RIDE_TYPE = RideType.OFFER;
    private static final RideType UPDATED_RIDE_TYPE = RideType.REQUEST;

    private static final String DEFAULT_RIDE_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_RIDE_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RideRepository rideRepository;

    @Mock
    private RideRepository rideRepositoryMock;

    @Autowired
    private RideMapper rideMapper;

    @Mock
    private RideService rideServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRideMockMvc;

    private Ride ride;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ride createEntity(EntityManager em) {
        Ride ride = new Ride()
            .rideDateTime(DEFAULT_RIDE_DATE_TIME)
            .rideCityFrom(DEFAULT_RIDE_CITY_FROM)
            .rideCityTo(DEFAULT_RIDE_CITY_TO)
            .rideType(DEFAULT_RIDE_TYPE)
            .rideComments(DEFAULT_RIDE_COMMENTS);
        return ride;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ride createUpdatedEntity(EntityManager em) {
        Ride ride = new Ride()
            .rideDateTime(UPDATED_RIDE_DATE_TIME)
            .rideCityFrom(UPDATED_RIDE_CITY_FROM)
            .rideCityTo(UPDATED_RIDE_CITY_TO)
            .rideType(UPDATED_RIDE_TYPE)
            .rideComments(UPDATED_RIDE_COMMENTS);
        return ride;
    }

    @BeforeEach
    public void initTest() {
        ride = createEntity(em);
    }

    @Test
    @Transactional
    void createRide() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();
        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);
        restRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isCreated());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate + 1);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getRideDateTime()).isEqualTo(DEFAULT_RIDE_DATE_TIME);
        assertThat(testRide.getRideCityFrom()).isEqualTo(DEFAULT_RIDE_CITY_FROM);
        assertThat(testRide.getRideCityTo()).isEqualTo(DEFAULT_RIDE_CITY_TO);
        assertThat(testRide.getRideType()).isEqualTo(DEFAULT_RIDE_TYPE);
        assertThat(testRide.getRideComments()).isEqualTo(DEFAULT_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void createRideWithExistingId() throws Exception {
        // Create the Ride with an existing ID
        ride.setId(1L);
        RideDTO rideDTO = rideMapper.toDto(ride);

        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRides() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList
        restRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].rideDateTime").value(hasItem(sameInstant(DEFAULT_RIDE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rideCityFrom").value(hasItem(DEFAULT_RIDE_CITY_FROM)))
            .andExpect(jsonPath("$.[*].rideCityTo").value(hasItem(DEFAULT_RIDE_CITY_TO)))
            .andExpect(jsonPath("$.[*].rideType").value(hasItem(DEFAULT_RIDE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rideComments").value(hasItem(DEFAULT_RIDE_COMMENTS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRidesWithEagerRelationshipsIsEnabled() throws Exception {
        when(rideServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRideMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rideServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRidesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rideServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRideMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(rideRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get the ride
        restRideMockMvc
            .perform(get(ENTITY_API_URL_ID, ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ride.getId().intValue()))
            .andExpect(jsonPath("$.rideDateTime").value(sameInstant(DEFAULT_RIDE_DATE_TIME)))
            .andExpect(jsonPath("$.rideCityFrom").value(DEFAULT_RIDE_CITY_FROM))
            .andExpect(jsonPath("$.rideCityTo").value(DEFAULT_RIDE_CITY_TO))
            .andExpect(jsonPath("$.rideType").value(DEFAULT_RIDE_TYPE.toString()))
            .andExpect(jsonPath("$.rideComments").value(DEFAULT_RIDE_COMMENTS.toString()));
    }

    @Test
    @Transactional
    void getRidesByIdFiltering() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        Long id = ride.getId();

        defaultRideShouldBeFound("id.equals=" + id);
        defaultRideShouldNotBeFound("id.notEquals=" + id);

        defaultRideShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRideShouldNotBeFound("id.greaterThan=" + id);

        defaultRideShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRideShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime equals to DEFAULT_RIDE_DATE_TIME
        defaultRideShouldBeFound("rideDateTime.equals=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the rideList where rideDateTime equals to UPDATED_RIDE_DATE_TIME
        defaultRideShouldNotBeFound("rideDateTime.equals=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime in DEFAULT_RIDE_DATE_TIME or UPDATED_RIDE_DATE_TIME
        defaultRideShouldBeFound("rideDateTime.in=" + DEFAULT_RIDE_DATE_TIME + "," + UPDATED_RIDE_DATE_TIME);

        // Get all the rideList where rideDateTime equals to UPDATED_RIDE_DATE_TIME
        defaultRideShouldNotBeFound("rideDateTime.in=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime is not null
        defaultRideShouldBeFound("rideDateTime.specified=true");

        // Get all the rideList where rideDateTime is null
        defaultRideShouldNotBeFound("rideDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime is greater than or equal to DEFAULT_RIDE_DATE_TIME
        defaultRideShouldBeFound("rideDateTime.greaterThanOrEqual=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the rideList where rideDateTime is greater than or equal to UPDATED_RIDE_DATE_TIME
        defaultRideShouldNotBeFound("rideDateTime.greaterThanOrEqual=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime is less than or equal to DEFAULT_RIDE_DATE_TIME
        defaultRideShouldBeFound("rideDateTime.lessThanOrEqual=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the rideList where rideDateTime is less than or equal to SMALLER_RIDE_DATE_TIME
        defaultRideShouldNotBeFound("rideDateTime.lessThanOrEqual=" + SMALLER_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime is less than DEFAULT_RIDE_DATE_TIME
        defaultRideShouldNotBeFound("rideDateTime.lessThan=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the rideList where rideDateTime is less than UPDATED_RIDE_DATE_TIME
        defaultRideShouldBeFound("rideDateTime.lessThan=" + UPDATED_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRidesByRideDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideDateTime is greater than DEFAULT_RIDE_DATE_TIME
        defaultRideShouldNotBeFound("rideDateTime.greaterThan=" + DEFAULT_RIDE_DATE_TIME);

        // Get all the rideList where rideDateTime is greater than SMALLER_RIDE_DATE_TIME
        defaultRideShouldBeFound("rideDateTime.greaterThan=" + SMALLER_RIDE_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityFromIsEqualToSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityFrom equals to DEFAULT_RIDE_CITY_FROM
        defaultRideShouldBeFound("rideCityFrom.equals=" + DEFAULT_RIDE_CITY_FROM);

        // Get all the rideList where rideCityFrom equals to UPDATED_RIDE_CITY_FROM
        defaultRideShouldNotBeFound("rideCityFrom.equals=" + UPDATED_RIDE_CITY_FROM);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityFromIsInShouldWork() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityFrom in DEFAULT_RIDE_CITY_FROM or UPDATED_RIDE_CITY_FROM
        defaultRideShouldBeFound("rideCityFrom.in=" + DEFAULT_RIDE_CITY_FROM + "," + UPDATED_RIDE_CITY_FROM);

        // Get all the rideList where rideCityFrom equals to UPDATED_RIDE_CITY_FROM
        defaultRideShouldNotBeFound("rideCityFrom.in=" + UPDATED_RIDE_CITY_FROM);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityFrom is not null
        defaultRideShouldBeFound("rideCityFrom.specified=true");

        // Get all the rideList where rideCityFrom is null
        defaultRideShouldNotBeFound("rideCityFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllRidesByRideCityFromContainsSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityFrom contains DEFAULT_RIDE_CITY_FROM
        defaultRideShouldBeFound("rideCityFrom.contains=" + DEFAULT_RIDE_CITY_FROM);

        // Get all the rideList where rideCityFrom contains UPDATED_RIDE_CITY_FROM
        defaultRideShouldNotBeFound("rideCityFrom.contains=" + UPDATED_RIDE_CITY_FROM);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityFromNotContainsSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityFrom does not contain DEFAULT_RIDE_CITY_FROM
        defaultRideShouldNotBeFound("rideCityFrom.doesNotContain=" + DEFAULT_RIDE_CITY_FROM);

        // Get all the rideList where rideCityFrom does not contain UPDATED_RIDE_CITY_FROM
        defaultRideShouldBeFound("rideCityFrom.doesNotContain=" + UPDATED_RIDE_CITY_FROM);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityToIsEqualToSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityTo equals to DEFAULT_RIDE_CITY_TO
        defaultRideShouldBeFound("rideCityTo.equals=" + DEFAULT_RIDE_CITY_TO);

        // Get all the rideList where rideCityTo equals to UPDATED_RIDE_CITY_TO
        defaultRideShouldNotBeFound("rideCityTo.equals=" + UPDATED_RIDE_CITY_TO);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityToIsInShouldWork() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityTo in DEFAULT_RIDE_CITY_TO or UPDATED_RIDE_CITY_TO
        defaultRideShouldBeFound("rideCityTo.in=" + DEFAULT_RIDE_CITY_TO + "," + UPDATED_RIDE_CITY_TO);

        // Get all the rideList where rideCityTo equals to UPDATED_RIDE_CITY_TO
        defaultRideShouldNotBeFound("rideCityTo.in=" + UPDATED_RIDE_CITY_TO);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityToIsNullOrNotNull() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityTo is not null
        defaultRideShouldBeFound("rideCityTo.specified=true");

        // Get all the rideList where rideCityTo is null
        defaultRideShouldNotBeFound("rideCityTo.specified=false");
    }

    @Test
    @Transactional
    void getAllRidesByRideCityToContainsSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityTo contains DEFAULT_RIDE_CITY_TO
        defaultRideShouldBeFound("rideCityTo.contains=" + DEFAULT_RIDE_CITY_TO);

        // Get all the rideList where rideCityTo contains UPDATED_RIDE_CITY_TO
        defaultRideShouldNotBeFound("rideCityTo.contains=" + UPDATED_RIDE_CITY_TO);
    }

    @Test
    @Transactional
    void getAllRidesByRideCityToNotContainsSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideCityTo does not contain DEFAULT_RIDE_CITY_TO
        defaultRideShouldNotBeFound("rideCityTo.doesNotContain=" + DEFAULT_RIDE_CITY_TO);

        // Get all the rideList where rideCityTo does not contain UPDATED_RIDE_CITY_TO
        defaultRideShouldBeFound("rideCityTo.doesNotContain=" + UPDATED_RIDE_CITY_TO);
    }

    @Test
    @Transactional
    void getAllRidesByRideTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideType equals to DEFAULT_RIDE_TYPE
        defaultRideShouldBeFound("rideType.equals=" + DEFAULT_RIDE_TYPE);

        // Get all the rideList where rideType equals to UPDATED_RIDE_TYPE
        defaultRideShouldNotBeFound("rideType.equals=" + UPDATED_RIDE_TYPE);
    }

    @Test
    @Transactional
    void getAllRidesByRideTypeIsInShouldWork() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideType in DEFAULT_RIDE_TYPE or UPDATED_RIDE_TYPE
        defaultRideShouldBeFound("rideType.in=" + DEFAULT_RIDE_TYPE + "," + UPDATED_RIDE_TYPE);

        // Get all the rideList where rideType equals to UPDATED_RIDE_TYPE
        defaultRideShouldNotBeFound("rideType.in=" + UPDATED_RIDE_TYPE);
    }

    @Test
    @Transactional
    void getAllRidesByRideTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList where rideType is not null
        defaultRideShouldBeFound("rideType.specified=true");

        // Get all the rideList where rideType is null
        defaultRideShouldNotBeFound("rideType.specified=false");
    }

    @Test
    @Transactional
    void getAllRidesByRideUserIsEqualToSomething() throws Exception {
        Users rideUser;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            rideRepository.saveAndFlush(ride);
            rideUser = UsersResourceIT.createEntity(em);
        } else {
            rideUser = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(rideUser);
        em.flush();
        ride.setRideUser(rideUser);
        rideRepository.saveAndFlush(ride);
        Long rideUserId = rideUser.getId();

        // Get all the rideList where rideUser equals to rideUserId
        defaultRideShouldBeFound("rideUserId.equals=" + rideUserId);

        // Get all the rideList where rideUser equals to (rideUserId + 1)
        defaultRideShouldNotBeFound("rideUserId.equals=" + (rideUserId + 1));
    }

    @Test
    @Transactional
    void getAllRidesByRideCityFromIsEqualToSomething() throws Exception {
        City rideCityFrom;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            rideRepository.saveAndFlush(ride);
            rideCityFrom = CityResourceIT.createEntity(em);
        } else {
            rideCityFrom = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(rideCityFrom);
        em.flush();
        ride.setRideCityFrom(rideCityFrom);
        rideRepository.saveAndFlush(ride);
        Long rideCityFromId = rideCityFrom.getId();

        // Get all the rideList where rideCityFrom equals to rideCityFromId
        defaultRideShouldBeFound("rideCityFromId.equals=" + rideCityFromId);

        // Get all the rideList where rideCityFrom equals to (rideCityFromId + 1)
        defaultRideShouldNotBeFound("rideCityFromId.equals=" + (rideCityFromId + 1));
    }

    @Test
    @Transactional
    void getAllRidesByRideCityToIsEqualToSomething() throws Exception {
        City rideCityTo;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            rideRepository.saveAndFlush(ride);
            rideCityTo = CityResourceIT.createEntity(em);
        } else {
            rideCityTo = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(rideCityTo);
        em.flush();
        ride.setRideCityTo(rideCityTo);
        rideRepository.saveAndFlush(ride);
        Long rideCityToId = rideCityTo.getId();

        // Get all the rideList where rideCityTo equals to rideCityToId
        defaultRideShouldBeFound("rideCityToId.equals=" + rideCityToId);

        // Get all the rideList where rideCityTo equals to (rideCityToId + 1)
        defaultRideShouldNotBeFound("rideCityToId.equals=" + (rideCityToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRideShouldBeFound(String filter) throws Exception {
        restRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].rideDateTime").value(hasItem(sameInstant(DEFAULT_RIDE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].rideCityFrom").value(hasItem(DEFAULT_RIDE_CITY_FROM)))
            .andExpect(jsonPath("$.[*].rideCityTo").value(hasItem(DEFAULT_RIDE_CITY_TO)))
            .andExpect(jsonPath("$.[*].rideType").value(hasItem(DEFAULT_RIDE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rideComments").value(hasItem(DEFAULT_RIDE_COMMENTS.toString())));

        // Check, that the count call also returns 1
        restRideMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRideShouldNotBeFound(String filter) throws Exception {
        restRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRideMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRide() throws Exception {
        // Get the ride
        restRideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride
        Ride updatedRide = rideRepository.findById(ride.getId()).get();
        // Disconnect from session so that the updates on updatedRide are not directly saved in db
        em.detach(updatedRide);
        updatedRide
            .rideDateTime(UPDATED_RIDE_DATE_TIME)
            .rideCityFrom(UPDATED_RIDE_CITY_FROM)
            .rideCityTo(UPDATED_RIDE_CITY_TO)
            .rideType(UPDATED_RIDE_TYPE)
            .rideComments(UPDATED_RIDE_COMMENTS);
        RideDTO rideDTO = rideMapper.toDto(updatedRide);

        restRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rideDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rideDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getRideDateTime()).isEqualTo(UPDATED_RIDE_DATE_TIME);
        assertThat(testRide.getRideCityFrom()).isEqualTo(UPDATED_RIDE_CITY_FROM);
        assertThat(testRide.getRideCityTo()).isEqualTo(UPDATED_RIDE_CITY_TO);
        assertThat(testRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testRide.getRideComments()).isEqualTo(UPDATED_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();
        ride.setId(count.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rideDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();
        ride.setId(count.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();
        ride.setId(count.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRideWithPatch() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride using partial update
        Ride partialUpdatedRide = new Ride();
        partialUpdatedRide.setId(ride.getId());

        partialUpdatedRide
            .rideCityFrom(UPDATED_RIDE_CITY_FROM)
            .rideCityTo(UPDATED_RIDE_CITY_TO)
            .rideType(UPDATED_RIDE_TYPE)
            .rideComments(UPDATED_RIDE_COMMENTS);

        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRide))
            )
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getRideDateTime()).isEqualTo(DEFAULT_RIDE_DATE_TIME);
        assertThat(testRide.getRideCityFrom()).isEqualTo(UPDATED_RIDE_CITY_FROM);
        assertThat(testRide.getRideCityTo()).isEqualTo(UPDATED_RIDE_CITY_TO);
        assertThat(testRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testRide.getRideComments()).isEqualTo(UPDATED_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateRideWithPatch() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride using partial update
        Ride partialUpdatedRide = new Ride();
        partialUpdatedRide.setId(ride.getId());

        partialUpdatedRide
            .rideDateTime(UPDATED_RIDE_DATE_TIME)
            .rideCityFrom(UPDATED_RIDE_CITY_FROM)
            .rideCityTo(UPDATED_RIDE_CITY_TO)
            .rideType(UPDATED_RIDE_TYPE)
            .rideComments(UPDATED_RIDE_COMMENTS);

        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRide))
            )
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getRideDateTime()).isEqualTo(UPDATED_RIDE_DATE_TIME);
        assertThat(testRide.getRideCityFrom()).isEqualTo(UPDATED_RIDE_CITY_FROM);
        assertThat(testRide.getRideCityTo()).isEqualTo(UPDATED_RIDE_CITY_TO);
        assertThat(testRide.getRideType()).isEqualTo(UPDATED_RIDE_TYPE);
        assertThat(testRide.getRideComments()).isEqualTo(UPDATED_RIDE_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();
        ride.setId(count.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rideDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();
        ride.setId(count.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();
        ride.setId(count.incrementAndGet());

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRideMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeDelete = rideRepository.findAll().size();

        // Delete the ride
        restRideMockMvc
            .perform(delete(ENTITY_API_URL_ID, ride.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
