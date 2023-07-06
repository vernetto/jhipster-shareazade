package org.pierre.shareazade.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.pierre.shareazade.repository.ShareRideRepository;
import org.pierre.shareazade.service.ShareRideQueryService;
import org.pierre.shareazade.service.ShareRideService;
import org.pierre.shareazade.service.criteria.ShareRideCriteria;
import org.pierre.shareazade.service.dto.ShareRideDTO;
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
 * REST controller for managing {@link org.pierre.shareazade.domain.ShareRide}.
 */
@RestController
@RequestMapping("/api")
public class ShareRideResource {

    private final Logger log = LoggerFactory.getLogger(ShareRideResource.class);

    private static final String ENTITY_NAME = "shareRide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShareRideService shareRideService;

    private final ShareRideRepository shareRideRepository;

    private final ShareRideQueryService shareRideQueryService;

    public ShareRideResource(
        ShareRideService shareRideService,
        ShareRideRepository shareRideRepository,
        ShareRideQueryService shareRideQueryService
    ) {
        this.shareRideService = shareRideService;
        this.shareRideRepository = shareRideRepository;
        this.shareRideQueryService = shareRideQueryService;
    }

    /**
     * {@code POST  /share-rides} : Create a new shareRide.
     *
     * @param shareRideDTO the shareRideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shareRideDTO, or with status {@code 400 (Bad Request)} if the shareRide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/share-rides")
    public ResponseEntity<ShareRideDTO> createShareRide(@RequestBody ShareRideDTO shareRideDTO) throws URISyntaxException {
        log.debug("REST request to save ShareRide : {}", shareRideDTO);
        if (shareRideDTO.getId() != null) {
            throw new BadRequestAlertException("A new shareRide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShareRideDTO result = shareRideService.save(shareRideDTO);
        return ResponseEntity
            .created(new URI("/api/share-rides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /share-rides/:id} : Updates an existing shareRide.
     *
     * @param id the id of the shareRideDTO to save.
     * @param shareRideDTO the shareRideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareRideDTO,
     * or with status {@code 400 (Bad Request)} if the shareRideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shareRideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/share-rides/{id}")
    public ResponseEntity<ShareRideDTO> updateShareRide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShareRideDTO shareRideDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShareRide : {}, {}", id, shareRideDTO);
        if (shareRideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareRideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareRideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShareRideDTO result = shareRideService.update(shareRideDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareRideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /share-rides/:id} : Partial updates given fields of an existing shareRide, field will ignore if it is null
     *
     * @param id the id of the shareRideDTO to save.
     * @param shareRideDTO the shareRideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareRideDTO,
     * or with status {@code 400 (Bad Request)} if the shareRideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shareRideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shareRideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/share-rides/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShareRideDTO> partialUpdateShareRide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShareRideDTO shareRideDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShareRide partially : {}, {}", id, shareRideDTO);
        if (shareRideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareRideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareRideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShareRideDTO> result = shareRideService.partialUpdate(shareRideDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareRideDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /share-rides} : get all the shareRides.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareRides in body.
     */
    @GetMapping("/share-rides")
    public ResponseEntity<List<ShareRideDTO>> getAllShareRides(
        ShareRideCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ShareRides by criteria: {}", criteria);
        Page<ShareRideDTO> page = shareRideQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /share-rides/count} : count all the shareRides.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/share-rides/count")
    public ResponseEntity<Long> countShareRides(ShareRideCriteria criteria) {
        log.debug("REST request to count ShareRides by criteria: {}", criteria);
        return ResponseEntity.ok().body(shareRideQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /share-rides/:id} : get the "id" shareRide.
     *
     * @param id the id of the shareRideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareRideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/share-rides/{id}")
    public ResponseEntity<ShareRideDTO> getShareRide(@PathVariable Long id) {
        log.debug("REST request to get ShareRide : {}", id);
        Optional<ShareRideDTO> shareRideDTO = shareRideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shareRideDTO);
    }

    /**
     * {@code DELETE  /share-rides/:id} : delete the "id" shareRide.
     *
     * @param id the id of the shareRideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/share-rides/{id}")
    public ResponseEntity<Void> deleteShareRide(@PathVariable Long id) {
        log.debug("REST request to delete ShareRide : {}", id);
        shareRideService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
