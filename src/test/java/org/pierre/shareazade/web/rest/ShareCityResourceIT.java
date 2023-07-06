package org.pierre.shareazade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pierre.shareazade.IntegrationTest;
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.domain.enumeration.ShareCountry;
import org.pierre.shareazade.repository.ShareCityRepository;
import org.pierre.shareazade.service.criteria.ShareCityCriteria;
import org.pierre.shareazade.service.dto.ShareCityDTO;
import org.pierre.shareazade.service.mapper.ShareCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShareCityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShareCityResourceIT {

    private static final String DEFAULT_CITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBBBBBBB";

    private static final ShareCountry DEFAULT_CITY_COUNTRY = ShareCountry.CH;
    private static final ShareCountry UPDATED_CITY_COUNTRY = ShareCountry.IT;

    private static final String ENTITY_API_URL = "/api/share-cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShareCityRepository shareCityRepository;

    @Autowired
    private ShareCityMapper shareCityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShareCityMockMvc;

    private ShareCity shareCity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareCity createEntity(EntityManager em) {
        ShareCity shareCity = new ShareCity().cityName(DEFAULT_CITY_NAME).cityCountry(DEFAULT_CITY_COUNTRY);
        return shareCity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareCity createUpdatedEntity(EntityManager em) {
        ShareCity shareCity = new ShareCity().cityName(UPDATED_CITY_NAME).cityCountry(UPDATED_CITY_COUNTRY);
        return shareCity;
    }

    @BeforeEach
    public void initTest() {
        shareCity = createEntity(em);
    }

    @Test
    @Transactional
    void createShareCity() throws Exception {
        int databaseSizeBeforeCreate = shareCityRepository.findAll().size();
        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);
        restShareCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareCityDTO)))
            .andExpect(status().isCreated());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeCreate + 1);
        ShareCity testShareCity = shareCityList.get(shareCityList.size() - 1);
        assertThat(testShareCity.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testShareCity.getCityCountry()).isEqualTo(DEFAULT_CITY_COUNTRY);
    }

    @Test
    @Transactional
    void createShareCityWithExistingId() throws Exception {
        // Create the ShareCity with an existing ID
        shareCity.setId(1L);
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        int databaseSizeBeforeCreate = shareCityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareCityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareCityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShareCities() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList
        restShareCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareCity.getId().intValue())))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].cityCountry").value(hasItem(DEFAULT_CITY_COUNTRY.toString())));
    }

    @Test
    @Transactional
    void getShareCity() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get the shareCity
        restShareCityMockMvc
            .perform(get(ENTITY_API_URL_ID, shareCity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shareCity.getId().intValue()))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME))
            .andExpect(jsonPath("$.cityCountry").value(DEFAULT_CITY_COUNTRY.toString()));
    }

    @Test
    @Transactional
    void getShareCitiesByIdFiltering() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        Long id = shareCity.getId();

        defaultShareCityShouldBeFound("id.equals=" + id);
        defaultShareCityShouldNotBeFound("id.notEquals=" + id);

        defaultShareCityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShareCityShouldNotBeFound("id.greaterThan=" + id);

        defaultShareCityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShareCityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityName equals to DEFAULT_CITY_NAME
        defaultShareCityShouldBeFound("cityName.equals=" + DEFAULT_CITY_NAME);

        // Get all the shareCityList where cityName equals to UPDATED_CITY_NAME
        defaultShareCityShouldNotBeFound("cityName.equals=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityNameIsInShouldWork() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityName in DEFAULT_CITY_NAME or UPDATED_CITY_NAME
        defaultShareCityShouldBeFound("cityName.in=" + DEFAULT_CITY_NAME + "," + UPDATED_CITY_NAME);

        // Get all the shareCityList where cityName equals to UPDATED_CITY_NAME
        defaultShareCityShouldNotBeFound("cityName.in=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityName is not null
        defaultShareCityShouldBeFound("cityName.specified=true");

        // Get all the shareCityList where cityName is null
        defaultShareCityShouldNotBeFound("cityName.specified=false");
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityNameContainsSomething() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityName contains DEFAULT_CITY_NAME
        defaultShareCityShouldBeFound("cityName.contains=" + DEFAULT_CITY_NAME);

        // Get all the shareCityList where cityName contains UPDATED_CITY_NAME
        defaultShareCityShouldNotBeFound("cityName.contains=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityNameNotContainsSomething() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityName does not contain DEFAULT_CITY_NAME
        defaultShareCityShouldNotBeFound("cityName.doesNotContain=" + DEFAULT_CITY_NAME);

        // Get all the shareCityList where cityName does not contain UPDATED_CITY_NAME
        defaultShareCityShouldBeFound("cityName.doesNotContain=" + UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityCountry equals to DEFAULT_CITY_COUNTRY
        defaultShareCityShouldBeFound("cityCountry.equals=" + DEFAULT_CITY_COUNTRY);

        // Get all the shareCityList where cityCountry equals to UPDATED_CITY_COUNTRY
        defaultShareCityShouldNotBeFound("cityCountry.equals=" + UPDATED_CITY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityCountryIsInShouldWork() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityCountry in DEFAULT_CITY_COUNTRY or UPDATED_CITY_COUNTRY
        defaultShareCityShouldBeFound("cityCountry.in=" + DEFAULT_CITY_COUNTRY + "," + UPDATED_CITY_COUNTRY);

        // Get all the shareCityList where cityCountry equals to UPDATED_CITY_COUNTRY
        defaultShareCityShouldNotBeFound("cityCountry.in=" + UPDATED_CITY_COUNTRY);
    }

    @Test
    @Transactional
    void getAllShareCitiesByCityCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        // Get all the shareCityList where cityCountry is not null
        defaultShareCityShouldBeFound("cityCountry.specified=true");

        // Get all the shareCityList where cityCountry is null
        defaultShareCityShouldNotBeFound("cityCountry.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShareCityShouldBeFound(String filter) throws Exception {
        restShareCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareCity.getId().intValue())))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].cityCountry").value(hasItem(DEFAULT_CITY_COUNTRY.toString())));

        // Check, that the count call also returns 1
        restShareCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShareCityShouldNotBeFound(String filter) throws Exception {
        restShareCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShareCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShareCity() throws Exception {
        // Get the shareCity
        restShareCityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShareCity() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();

        // Update the shareCity
        ShareCity updatedShareCity = shareCityRepository.findById(shareCity.getId()).get();
        // Disconnect from session so that the updates on updatedShareCity are not directly saved in db
        em.detach(updatedShareCity);
        updatedShareCity.cityName(UPDATED_CITY_NAME).cityCountry(UPDATED_CITY_COUNTRY);
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(updatedShareCity);

        restShareCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareCityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareCityDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
        ShareCity testShareCity = shareCityList.get(shareCityList.size() - 1);
        assertThat(testShareCity.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testShareCity.getCityCountry()).isEqualTo(UPDATED_CITY_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingShareCity() throws Exception {
        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();
        shareCity.setId(count.incrementAndGet());

        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareCityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShareCity() throws Exception {
        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();
        shareCity.setId(count.incrementAndGet());

        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShareCity() throws Exception {
        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();
        shareCity.setId(count.incrementAndGet());

        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareCityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareCityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShareCityWithPatch() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();

        // Update the shareCity using partial update
        ShareCity partialUpdatedShareCity = new ShareCity();
        partialUpdatedShareCity.setId(shareCity.getId());

        partialUpdatedShareCity.cityCountry(UPDATED_CITY_COUNTRY);

        restShareCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareCity))
            )
            .andExpect(status().isOk());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
        ShareCity testShareCity = shareCityList.get(shareCityList.size() - 1);
        assertThat(testShareCity.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testShareCity.getCityCountry()).isEqualTo(UPDATED_CITY_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateShareCityWithPatch() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();

        // Update the shareCity using partial update
        ShareCity partialUpdatedShareCity = new ShareCity();
        partialUpdatedShareCity.setId(shareCity.getId());

        partialUpdatedShareCity.cityName(UPDATED_CITY_NAME).cityCountry(UPDATED_CITY_COUNTRY);

        restShareCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareCity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareCity))
            )
            .andExpect(status().isOk());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
        ShareCity testShareCity = shareCityList.get(shareCityList.size() - 1);
        assertThat(testShareCity.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testShareCity.getCityCountry()).isEqualTo(UPDATED_CITY_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingShareCity() throws Exception {
        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();
        shareCity.setId(count.incrementAndGet());

        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shareCityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShareCity() throws Exception {
        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();
        shareCity.setId(count.incrementAndGet());

        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareCityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShareCity() throws Exception {
        int databaseSizeBeforeUpdate = shareCityRepository.findAll().size();
        shareCity.setId(count.incrementAndGet());

        // Create the ShareCity
        ShareCityDTO shareCityDTO = shareCityMapper.toDto(shareCity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareCityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shareCityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareCity in the database
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShareCity() throws Exception {
        // Initialize the database
        shareCityRepository.saveAndFlush(shareCity);

        int databaseSizeBeforeDelete = shareCityRepository.findAll().size();

        // Delete the shareCity
        restShareCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, shareCity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShareCity> shareCityList = shareCityRepository.findAll();
        assertThat(shareCityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
