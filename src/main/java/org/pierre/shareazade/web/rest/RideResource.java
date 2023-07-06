package org.pierre.shareazade.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.pierre.shareazade.repository.RideRepository;
import org.pierre.shareazade.service.RideQueryService;
import org.pierre.shareazade.service.RideService;
import org.pierre.shareazade.service.criteria.RideCriteria;
import org.pierre.shareazade.service.dto.RideDTO;
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
 * REST controller for managing {@link org.pierre.shareazade.domain.Ride}.
 */
@RestController
@RequestMapping("/api")
public class RideResource {

    private final Logger log = LoggerFactory.getLogger(RideResource.class);

    private static final String ENTITY_NAME = "ride";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RideService rideService;

    private final RideRepository rideRepository;

    private final RideQueryService rideQueryService;

    public RideResource(RideService rideService, RideRepository rideRepository, RideQueryService rideQueryService) {
        this.rideService = rideService;
        this.rideRepository = rideRepository;
        this.rideQueryService = rideQueryService;
    }

    /**
     * {@code POST  /rides} : Create a new ride.
     *
     * @param rideDTO the rideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rideDTO, or with status {@code 400 (Bad Request)} if the ride has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rides")
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO) throws URISyntaxException {
        log.debug("REST request to save Ride : {}", rideDTO);
        if (rideDTO.getId() != null) {
            throw new BadRequestAlertException("A new ride cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RideDTO result = rideService.save(rideDTO);
        return ResponseEntity
            .created(new URI("/api/rides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rides/:id} : Updates an existing ride.
     *
     * @param id the id of the rideDTO to save.
     * @param rideDTO the rideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rideDTO,
     * or with status {@code 400 (Bad Request)} if the rideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rides/{id}")
    public ResponseEntity<RideDTO> updateRide(@PathVariable(value = "id", required = false) final Long id, @RequestBody RideDTO rideDTO)
        throws URISyntaxException {
        log.debug("REST request to update Ride : {}, {}", id, rideDTO);
        if (rideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RideDTO result = rideService.update(rideDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rides/:id} : Partial updates given fields of an existing ride, field will ignore if it is null
     *
     * @param id the id of the rideDTO to save.
     * @param rideDTO the rideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rideDTO,
     * or with status {@code 400 (Bad Request)} if the rideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rides/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RideDTO> partialUpdateRide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RideDTO rideDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ride partially : {}, {}", id, rideDTO);
        if (rideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RideDTO> result = rideService.partialUpdate(rideDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rideDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rides} : get all the rides.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rides in body.
     */
    @GetMapping("/rides")
    public ResponseEntity<List<RideDTO>> getAllRides(
        RideCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Rides by criteria: {}", criteria);
        Page<RideDTO> page = rideQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rides/count} : count all the rides.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rides/count")
    public ResponseEntity<Long> countRides(RideCriteria criteria) {
        log.debug("REST request to count Rides by criteria: {}", criteria);
        return ResponseEntity.ok().body(rideQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rides/:id} : get the "id" ride.
     *
     * @param id the id of the rideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rides/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Long id) {
        log.debug("REST request to get Ride : {}", id);
        Optional<RideDTO> rideDTO = rideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rideDTO);
    }

    /**
     * {@code DELETE  /rides/:id} : delete the "id" ride.
     *
     * @param id the id of the rideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rides/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        log.debug("REST request to delete Ride : {}", id);
        rideService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
