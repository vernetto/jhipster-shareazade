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
import org.pierre.shareazade.domain.Users;
import org.pierre.shareazade.domain.enumeration.UserRole;
import org.pierre.shareazade.domain.enumeration.UserStatus;
import org.pierre.shareazade.repository.UsersRepository;
import org.pierre.shareazade.service.criteria.UsersCriteria;
import org.pierre.shareazade.service.dto.UsersDTO;
import org.pierre.shareazade.service.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

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

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .userName(DEFAULT_USER_NAME)
            .userEmail(DEFAULT_USER_EMAIL)
            .userRole(DEFAULT_USER_ROLE)
            .userPhone(DEFAULT_USER_PHONE)
            .userStatus(DEFAULT_USER_STATUS);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userRole(UPDATED_USER_ROLE)
            .userPhone(UPDATED_USER_PHONE)
            .userStatus(UPDATED_USER_STATUS);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        int databaseSizeBeforeCreate = usersRepository.findAll().size();
        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isCreated());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate + 1);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUsers.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testUsers.getUserRole()).isEqualTo(DEFAULT_USER_ROLE);
        assertThat(testUsers.getUserPhone()).isEqualTo(DEFAULT_USER_PHONE);
        assertThat(testUsers.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);
        UsersDTO usersDTO = usersMapper.toDto(users);

        int databaseSizeBeforeCreate = usersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].userPhone").value(hasItem(DEFAULT_USER_PHONE)))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.userRole").value(DEFAULT_USER_ROLE.toString()))
            .andExpect(jsonPath("$.userPhone").value(DEFAULT_USER_PHONE))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS.toString()));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        Long id = users.getId();

        defaultUsersShouldBeFound("id.equals=" + id);
        defaultUsersShouldNotBeFound("id.notEquals=" + id);

        defaultUsersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.greaterThan=" + id);

        defaultUsersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userName equals to DEFAULT_USER_NAME
        defaultUsersShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the usersList where userName equals to UPDATED_USER_NAME
        defaultUsersShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultUsersShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the usersList where userName equals to UPDATED_USER_NAME
        defaultUsersShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userName is not null
        defaultUsersShouldBeFound("userName.specified=true");

        // Get all the usersList where userName is null
        defaultUsersShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userName contains DEFAULT_USER_NAME
        defaultUsersShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the usersList where userName contains UPDATED_USER_NAME
        defaultUsersShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userName does not contain DEFAULT_USER_NAME
        defaultUsersShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the usersList where userName does not contain UPDATED_USER_NAME
        defaultUsersShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userEmail equals to DEFAULT_USER_EMAIL
        defaultUsersShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the usersList where userEmail equals to UPDATED_USER_EMAIL
        defaultUsersShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultUsersShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the usersList where userEmail equals to UPDATED_USER_EMAIL
        defaultUsersShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userEmail is not null
        defaultUsersShouldBeFound("userEmail.specified=true");

        // Get all the usersList where userEmail is null
        defaultUsersShouldNotBeFound("userEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userEmail contains DEFAULT_USER_EMAIL
        defaultUsersShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the usersList where userEmail contains UPDATED_USER_EMAIL
        defaultUsersShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultUsersShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the usersList where userEmail does not contain UPDATED_USER_EMAIL
        defaultUsersShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByUserRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userRole equals to DEFAULT_USER_ROLE
        defaultUsersShouldBeFound("userRole.equals=" + DEFAULT_USER_ROLE);

        // Get all the usersList where userRole equals to UPDATED_USER_ROLE
        defaultUsersShouldNotBeFound("userRole.equals=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllUsersByUserRoleIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userRole in DEFAULT_USER_ROLE or UPDATED_USER_ROLE
        defaultUsersShouldBeFound("userRole.in=" + DEFAULT_USER_ROLE + "," + UPDATED_USER_ROLE);

        // Get all the usersList where userRole equals to UPDATED_USER_ROLE
        defaultUsersShouldNotBeFound("userRole.in=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllUsersByUserRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userRole is not null
        defaultUsersShouldBeFound("userRole.specified=true");

        // Get all the usersList where userRole is null
        defaultUsersShouldNotBeFound("userRole.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userPhone equals to DEFAULT_USER_PHONE
        defaultUsersShouldBeFound("userPhone.equals=" + DEFAULT_USER_PHONE);

        // Get all the usersList where userPhone equals to UPDATED_USER_PHONE
        defaultUsersShouldNotBeFound("userPhone.equals=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByUserPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userPhone in DEFAULT_USER_PHONE or UPDATED_USER_PHONE
        defaultUsersShouldBeFound("userPhone.in=" + DEFAULT_USER_PHONE + "," + UPDATED_USER_PHONE);

        // Get all the usersList where userPhone equals to UPDATED_USER_PHONE
        defaultUsersShouldNotBeFound("userPhone.in=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByUserPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userPhone is not null
        defaultUsersShouldBeFound("userPhone.specified=true");

        // Get all the usersList where userPhone is null
        defaultUsersShouldNotBeFound("userPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUserPhoneContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userPhone contains DEFAULT_USER_PHONE
        defaultUsersShouldBeFound("userPhone.contains=" + DEFAULT_USER_PHONE);

        // Get all the usersList where userPhone contains UPDATED_USER_PHONE
        defaultUsersShouldNotBeFound("userPhone.contains=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByUserPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userPhone does not contain DEFAULT_USER_PHONE
        defaultUsersShouldNotBeFound("userPhone.doesNotContain=" + DEFAULT_USER_PHONE);

        // Get all the usersList where userPhone does not contain UPDATED_USER_PHONE
        defaultUsersShouldBeFound("userPhone.doesNotContain=" + UPDATED_USER_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByUserStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userStatus equals to DEFAULT_USER_STATUS
        defaultUsersShouldBeFound("userStatus.equals=" + DEFAULT_USER_STATUS);

        // Get all the usersList where userStatus equals to UPDATED_USER_STATUS
        defaultUsersShouldNotBeFound("userStatus.equals=" + UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByUserStatusIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userStatus in DEFAULT_USER_STATUS or UPDATED_USER_STATUS
        defaultUsersShouldBeFound("userStatus.in=" + DEFAULT_USER_STATUS + "," + UPDATED_USER_STATUS);

        // Get all the usersList where userStatus equals to UPDATED_USER_STATUS
        defaultUsersShouldNotBeFound("userStatus.in=" + UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByUserStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where userStatus is not null
        defaultUsersShouldBeFound("userStatus.specified=true");

        // Get all the usersList where userStatus is null
        defaultUsersShouldNotBeFound("userStatus.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsersShouldBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].userPhone").value(hasItem(DEFAULT_USER_PHONE)))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())));

        // Check, that the count call also returns 1
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsersShouldNotBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).get();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userRole(UPDATED_USER_ROLE)
            .userPhone(UPDATED_USER_PHONE)
            .userStatus(UPDATED_USER_STATUS);
        UsersDTO usersDTO = usersMapper.toDto(updatedUsers);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUsers.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testUsers.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testUsers.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testUsers.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers.userEmail(UPDATED_USER_EMAIL).userPhone(UPDATED_USER_PHONE);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUsers.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testUsers.getUserRole()).isEqualTo(DEFAULT_USER_ROLE);
        assertThat(testUsers.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testUsers.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .userName(UPDATED_USER_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .userRole(UPDATED_USER_ROLE)
            .userPhone(UPDATED_USER_PHONE)
            .userStatus(UPDATED_USER_STATUS);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUsers.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testUsers.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testUsers.getUserPhone()).isEqualTo(UPDATED_USER_PHONE);
        assertThat(testUsers.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeDelete = usersRepository.findAll().size();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
