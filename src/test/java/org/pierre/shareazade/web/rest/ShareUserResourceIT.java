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
import org.pierre.shareazade.domain.ShareUser;
import org.pierre.shareazade.domain.enumeration.UserRole;
import org.pierre.shareazade.domain.enumeration.UserStatus;
import org.pierre.shareazade.repository.ShareUserRepository;
import org.pierre.shareazade.service.criteria.ShareUserCriteria;
import org.pierre.shareazade.service.dto.ShareUserDTO;
import org.pierre.shareazade.service.mapper.ShareUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShareUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShareUserResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final UserRole DEFAULT_USER_ROLE = UserRole.ADMIN;
    private static final UserRole UPDATED_USER_ROLE = UserRole.USER;

    private static final String DEFAULT_USER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_USER_PHONE = "BBBBBBBBBB";

    private static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;
    private static final UserStatus UPDATED_USER_STATUS = UserStatus.SUSPENDED;

    private static final String ENTITY_API_URL = "/api/share-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShareUserRepository shareUserRepository;

    @Autowired
    private ShareUserMapper shareUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShareUserMockMvc;

    private ShareUser shareUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareUser createEntity(EntityManager em) {
        ShareUser shareUser = new ShareUser()
            .userName(DEFAULT_USER_NAME)
            .userEmail(DEFAULT_USER_EMAIL)
            .userRole(DEFAULT_USER_ROLE)
            .userPhone(DEFAULT_USER_PHONE)
            .userStatus(DEFAULT_USER_STATUS);
        return shareUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareUser createUpdatedEntity(EntityManager em) {
        ShareUser shareUser = new ShareUser()
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userRole(UPDATED_USER_ROLE)
            .userPhone(UPDATED_USER_PHONE)
            .userStatus(UPDATED_USER_STATUS);
        return shareUser;
    }

    @BeforeEach
    public void initTest() {
        shareUser = createEntity(em);
    }

    @Test
    @Transactional
    void createShareUser() throws Exception {
        int databaseSizeBeforeCreate = shareUserRepository.findAll().size();
        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);
        restShareUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareUserDTO)))
            .andExpect(status().isCreated());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeCreate + 1);
        ShareUser testShareUser = shareUserList.get(shareUserList.size() - 1);
        assertThat(testShareUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testShareUser.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testShareUser.getUserRole()).isEqualTo(DEFAULT_USER_ROLE);
        assertThat(testShareUser.getUserPhone()).isEqualTo(DEFAULT_USER_PHONE);
        assertThat(testShareUser.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
    }

    @Test
    @Transactional
    void createShareUserWithExistingId() throws Exception {
        // Create the ShareUser with an existing ID
        shareUser.setId(1L);
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        int databaseSizeBeforeCreate = shareUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShareUsers() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList
        restShareUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].userPhone").value(hasItem(DEFAULT_USER_PHONE)))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())));
    }

    @Test
    @Transactional
    void getShareUser() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get the shareUser
        restShareUserMockMvc
            .perform(get(ENTITY_API_URL_ID, shareUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shareUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.userRole").value(DEFAULT_USER_ROLE.toString()))
            .andExpect(jsonPath("$.userPhone").value(DEFAULT_USER_PHONE))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS.toString()));
    }

    @Test
    @Transactional
    void getShareUsersByIdFiltering() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        Long id = shareUser.getId();

        defaultShareUserShouldBeFound("id.equals=" + id);
        defaultShareUserShouldNotBeFound("id.notEquals=" + id);

        defaultShareUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShareUserShouldNotBeFound("id.greaterThan=" + id);

        defaultShareUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShareUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userName equals to DEFAULT_USER_NAME
        defaultShareUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the shareUserList where userName equals to UPDATED_USER_NAME
        defaultShareUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultShareUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the shareUserList where userName equals to UPDATED_USER_NAME
        defaultShareUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userName is not null
        defaultShareUserShouldBeFound("userName.specified=true");

        // Get all the shareUserList where userName is null
        defaultShareUserShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllShareUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userName contains DEFAULT_USER_NAME
        defaultShareUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the shareUserList where userName contains UPDATED_USER_NAME
        defaultShareUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userName does not contain DEFAULT_USER_NAME
        defaultShareUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the shareUserList where userName does not contain UPDATED_USER_NAME
        defaultShareUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userEmail equals to DEFAULT_USER_EMAIL
        defaultShareUserShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the shareUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultShareUserShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultShareUserShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the shareUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultShareUserShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userEmail is not null
        defaultShareUserShouldBeFound("userEmail.specified=true");

        // Get all the shareUserList where userEmail is null
        defaultShareUserShouldNotBeFound("userEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllShareUsersByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userEmail contains DEFAULT_USER_EMAIL
        defaultShareUserShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the shareUserList where userEmail contains UPDATED_USER_EMAIL
        defaultShareUserShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultShareUserShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the shareUserList where userEmail does not contain UPDATED_USER_EMAIL
        defaultShareUserShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userRole equals to DEFAULT_USER_ROLE
        defaultShareUserShouldBeFound("userRole.equals=" + DEFAULT_USER_ROLE);

        // Get all the shareUserList where userRole equals to UPDATED_USER_ROLE
        defaultShareUserShouldNotBeFound("userRole.equals=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserRoleIsInShouldWork() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userRole in DEFAULT_USER_ROLE or UPDATED_USER_ROLE
        defaultShareUserShouldBeFound("userRole.in=" + DEFAULT_USER_ROLE + "," + UPDATED_USER_ROLE);

        // Get all the shareUserList where userRole equals to UPDATED_USER_ROLE
        defaultShareUserShouldNotBeFound("userRole.in=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userRole is not null
        defaultShareUserShouldBeFound("userRole.specified=true");

        // Get all the shareUserList where userRole is null
        defaultShareUserShouldNotBeFound("userRole.specified=false");
    }

    @Test
    @Transactional
    void getAllShareUsersByUserPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userPhone equals to DEFAULT_USER_PHONE
        defaultShareUserShouldBeFound("userPhone.equals=" + DEFAULT_USER_PHONE);

        // Get all the shareUserList where userPhone equals to UPDATED_USER_PHONE
        defaultShareUserShouldNotBeFound("userPhone.equals=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userPhone in DEFAULT_USER_PHONE or UPDATED_USER_PHONE
        defaultShareUserShouldBeFound("userPhone.in=" + DEFAULT_USER_PHONE + "," + UPDATED_USER_PHONE);

        // Get all the shareUserList where userPhone equals to UPDATED_USER_PHONE
        defaultShareUserShouldNotBeFound("userPhone.in=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userPhone is not null
        defaultShareUserShouldBeFound("userPhone.specified=true");

        // Get all the shareUserList where userPhone is null
        defaultShareUserShouldNotBeFound("userPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllShareUsersByUserPhoneContainsSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userPhone contains DEFAULT_USER_PHONE
        defaultShareUserShouldBeFound("userPhone.contains=" + DEFAULT_USER_PHONE);

        // Get all the shareUserList where userPhone contains UPDATED_USER_PHONE
        defaultShareUserShouldNotBeFound("userPhone.contains=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userPhone does not contain DEFAULT_USER_PHONE
        defaultShareUserShouldNotBeFound("userPhone.doesNotContain=" + DEFAULT_USER_PHONE);

        // Get all the shareUserList where userPhone does not contain UPDATED_USER_PHONE
        defaultShareUserShouldBeFound("userPhone.doesNotContain=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userStatus equals to DEFAULT_USER_STATUS
        defaultShareUserShouldBeFound("userStatus.equals=" + DEFAULT_USER_STATUS);

        // Get all the shareUserList where userStatus equals to UPDATED_USER_STATUS
        defaultShareUserShouldNotBeFound("userStatus.equals=" + UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserStatusIsInShouldWork() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userStatus in DEFAULT_USER_STATUS or UPDATED_USER_STATUS
        defaultShareUserShouldBeFound("userStatus.in=" + DEFAULT_USER_STATUS + "," + UPDATED_USER_STATUS);

        // Get all the shareUserList where userStatus equals to UPDATED_USER_STATUS
        defaultShareUserShouldNotBeFound("userStatus.in=" + UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void getAllShareUsersByUserStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        // Get all the shareUserList where userStatus is not null
        defaultShareUserShouldBeFound("userStatus.specified=true");

        // Get all the shareUserList where userStatus is null
        defaultShareUserShouldNotBeFound("userStatus.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShareUserShouldBeFound(String filter) throws Exception {
        restShareUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].userPhone").value(hasItem(DEFAULT_USER_PHONE)))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())));

        // Check, that the count call also returns 1
        restShareUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShareUserShouldNotBeFound(String filter) throws Exception {
        restShareUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShareUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShareUser() throws Exception {
        // Get the shareUser
        restShareUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShareUser() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();

        // Update the shareUser
        ShareUser updatedShareUser = shareUserRepository.findById(shareUser.getId()).get();
        // Disconnect from session so that the updates on updatedShareUser are not directly saved in db
        em.detach(updatedShareUser);
        updatedShareUser
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userRole(UPDATED_USER_ROLE)
            .userPhone(UPDATED_USER_PHONE)
            .userStatus(UPDATED_USER_STATUS);
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(updatedShareUser);

        restShareUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
        ShareUser testShareUser = shareUserList.get(shareUserList.size() - 1);
        assertThat(testShareUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testShareUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testShareUser.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testShareUser.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testShareUser.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingShareUser() throws Exception {
        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();
        shareUser.setId(count.incrementAndGet());

        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShareUser() throws Exception {
        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();
        shareUser.setId(count.incrementAndGet());

        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShareUser() throws Exception {
        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();
        shareUser.setId(count.incrementAndGet());

        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShareUserWithPatch() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();

        // Update the shareUser using partial update
        ShareUser partialUpdatedShareUser = new ShareUser();
        partialUpdatedShareUser.setId(shareUser.getId());

        partialUpdatedShareUser.userEmail(UPDATED_USER_EMAIL).userRole(UPDATED_USER_ROLE).userPhone(UPDATED_USER_PHONE);

        restShareUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareUser))
            )
            .andExpect(status().isOk());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
        ShareUser testShareUser = shareUserList.get(shareUserList.size() - 1);
        assertThat(testShareUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testShareUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testShareUser.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testShareUser.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testShareUser.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateShareUserWithPatch() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();

        // Update the shareUser using partial update
        ShareUser partialUpdatedShareUser = new ShareUser();
        partialUpdatedShareUser.setId(shareUser.getId());

        partialUpdatedShareUser
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userRole(UPDATED_USER_ROLE)
            .userPhone(UPDATED_USER_PHONE)
            .userStatus(UPDATED_USER_STATUS);

        restShareUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareUser))
            )
            .andExpect(status().isOk());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
        ShareUser testShareUser = shareUserList.get(shareUserList.size() - 1);
        assertThat(testShareUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testShareUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testShareUser.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testShareUser.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testShareUser.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingShareUser() throws Exception {
        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();
        shareUser.setId(count.incrementAndGet());

        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shareUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShareUser() throws Exception {
        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();
        shareUser.setId(count.incrementAndGet());

        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShareUser() throws Exception {
        int databaseSizeBeforeUpdate = shareUserRepository.findAll().size();
        shareUser.setId(count.incrementAndGet());

        // Create the ShareUser
        ShareUserDTO shareUserDTO = shareUserMapper.toDto(shareUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shareUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareUser in the database
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShareUser() throws Exception {
        // Initialize the database
        shareUserRepository.saveAndFlush(shareUser);

        int databaseSizeBeforeDelete = shareUserRepository.findAll().size();

        // Delete the shareUser
        restShareUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, shareUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShareUser> shareUserList = shareUserRepository.findAll();
        assertThat(shareUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
