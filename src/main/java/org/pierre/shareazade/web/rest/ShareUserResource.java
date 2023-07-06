package org.pierre.shareazade.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.pierre.shareazade.repository.ShareUserRepository;
import org.pierre.shareazade.service.ShareUserQueryService;
import org.pierre.shareazade.service.ShareUserService;
import org.pierre.shareazade.service.criteria.ShareUserCriteria;
import org.pierre.shareazade.service.dto.ShareUserDTO;
import org.pierre.shareazade.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.pierre.shareazade.domain.ShareUser}.
 */
@RestController
@RequestMapping("/api")
public class ShareUserResource {

    private final Logger log = LoggerFactory.getLogger(ShareUserResource.class);

    private static final String ENTITY_NAME = "shareUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShareUserService shareUserService;

    private final ShareUserRepository shareUserRepository;

    private final ShareUserQueryService shareUserQueryService;

    public ShareUserResource(
        ShareUserService shareUserService,
        ShareUserRepository shareUserRepository,
        ShareUserQueryService shareUserQueryService
    ) {
        this.shareUserService = shareUserService;
        this.shareUserRepository = shareUserRepository;
        this.shareUserQueryService = shareUserQueryService;
    }

    /**
     * {@code POST  /share-users} : Create a new shareUser.
     *
     * @param shareUserDTO the shareUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shareUserDTO, or with status {@code 400 (Bad Request)} if the shareUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/share-users")
    public ResponseEntity<ShareUserDTO> createShareUser(@RequestBody ShareUserDTO shareUserDTO) throws URISyntaxException {
        log.debug("REST request to save ShareUser : {}", shareUserDTO);
        if (shareUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new shareUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShareUserDTO result = shareUserService.save(shareUserDTO);
        return ResponseEntity
            .created(new URI("/api/share-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /share-users/:id} : Updates an existing shareUser.
     *
     * @param id the id of the shareUserDTO to save.
     * @param shareUserDTO the shareUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareUserDTO,
     * or with status {@code 400 (Bad Request)} if the shareUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shareUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/share-users/{id}")
    public ResponseEntity<ShareUserDTO> updateShareUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShareUserDTO shareUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShareUser : {}, {}", id, shareUserDTO);
        if (shareUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShareUserDTO result = shareUserService.update(shareUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /share-users/:id} : Partial updates given fields of an existing shareUser, field will ignore if it is null
     *
     * @param id the id of the shareUserDTO to save.
     * @param shareUserDTO the shareUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareUserDTO,
     * or with status {@code 400 (Bad Request)} if the shareUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shareUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shareUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/share-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShareUserDTO> partialUpdateShareUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShareUserDTO shareUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShareUser partially : {}, {}", id, shareUserDTO);
        if (shareUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShareUserDTO> result = shareUserService.partialUpdate(shareUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /share-users} : get all the shareUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareUsers in body.
     */
    @GetMapping("/share-users")
    public ResponseEntity<List<ShareUserDTO>> getAllShareUsers(
        ShareUserCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ShareUsers by criteria: {}", criteria);
        Page<ShareUserDTO> page = shareUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /share-users/count} : count all the shareUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/share-users/count")
    public ResponseEntity<Long> countShareUsers(ShareUserCriteria criteria) {
        log.debug("REST request to count ShareUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(shareUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /share-users/:id} : get the "id" shareUser.
     *
     * @param id the id of the shareUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/share-users/{id}")
    public ResponseEntity<ShareUserDTO> getShareUser(@PathVariable Long id) {
        log.debug("REST request to get ShareUser : {}", id);
        Optional<ShareUserDTO> shareUserDTO = shareUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shareUserDTO);
    }

    /**
     * {@code DELETE  /share-users/:id} : delete the "id" shareUser.
     *
     * @param id the id of the shareUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/share-users/{id}")
    public ResponseEntity<Void> deleteShareUser(@PathVariable Long id) {
        log.debug("REST request to delete ShareUser : {}", id);
        shareUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
