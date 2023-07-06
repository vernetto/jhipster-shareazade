package org.pierre.shareazade.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.pierre.shareazade.repository.ShareCityRepository;
import org.pierre.shareazade.service.ShareCityQueryService;
import org.pierre.shareazade.service.ShareCityService;
import org.pierre.shareazade.service.criteria.ShareCityCriteria;
import org.pierre.shareazade.service.dto.ShareCityDTO;
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
 * REST controller for managing {@link org.pierre.shareazade.domain.ShareCity}.
 */
@RestController
@RequestMapping("/api")
public class ShareCityResource {

    private final Logger log = LoggerFactory.getLogger(ShareCityResource.class);

    private static final String ENTITY_NAME = "shareCity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShareCityService shareCityService;

    private final ShareCityRepository shareCityRepository;

    private final ShareCityQueryService shareCityQueryService;

    public ShareCityResource(
        ShareCityService shareCityService,
        ShareCityRepository shareCityRepository,
        ShareCityQueryService shareCityQueryService
    ) {
        this.shareCityService = shareCityService;
        this.shareCityRepository = shareCityRepository;
        this.shareCityQueryService = shareCityQueryService;
    }

    /**
     * {@code POST  /share-cities} : Create a new shareCity.
     *
     * @param shareCityDTO the shareCityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shareCityDTO, or with status {@code 400 (Bad Request)} if the shareCity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/share-cities")
    public ResponseEntity<ShareCityDTO> createShareCity(@RequestBody ShareCityDTO shareCityDTO) throws URISyntaxException {
        log.debug("REST request to save ShareCity : {}", shareCityDTO);
        if (shareCityDTO.getId() != null) {
            throw new BadRequestAlertException("A new shareCity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShareCityDTO result = shareCityService.save(shareCityDTO);
        return ResponseEntity
            .created(new URI("/api/share-cities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /share-cities/:id} : Updates an existing shareCity.
     *
     * @param id the id of the shareCityDTO to save.
     * @param shareCityDTO the shareCityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareCityDTO,
     * or with status {@code 400 (Bad Request)} if the shareCityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shareCityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/share-cities/{id}")
    public ResponseEntity<ShareCityDTO> updateShareCity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShareCityDTO shareCityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShareCity : {}, {}", id, shareCityDTO);
        if (shareCityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareCityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareCityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShareCityDTO result = shareCityService.update(shareCityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareCityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /share-cities/:id} : Partial updates given fields of an existing shareCity, field will ignore if it is null
     *
     * @param id the id of the shareCityDTO to save.
     * @param shareCityDTO the shareCityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareCityDTO,
     * or with status {@code 400 (Bad Request)} if the shareCityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shareCityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shareCityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/share-cities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShareCityDTO> partialUpdateShareCity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShareCityDTO shareCityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShareCity partially : {}, {}", id, shareCityDTO);
        if (shareCityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareCityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareCityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShareCityDTO> result = shareCityService.partialUpdate(shareCityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareCityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /share-cities} : get all the shareCities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareCities in body.
     */
    @GetMapping("/share-cities")
    public ResponseEntity<List<ShareCityDTO>> getAllShareCities(
        ShareCityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ShareCities by criteria: {}", criteria);
        Page<ShareCityDTO> page = shareCityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /share-cities/count} : count all the shareCities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/share-cities/count")
    public ResponseEntity<Long> countShareCities(ShareCityCriteria criteria) {
        log.debug("REST request to count ShareCities by criteria: {}", criteria);
        return ResponseEntity.ok().body(shareCityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /share-cities/:id} : get the "id" shareCity.
     *
     * @param id the id of the shareCityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareCityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/share-cities/{id}")
    public ResponseEntity<ShareCityDTO> getShareCity(@PathVariable Long id) {
        log.debug("REST request to get ShareCity : {}", id);
        Optional<ShareCityDTO> shareCityDTO = shareCityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shareCityDTO);
    }

    /**
     * {@code DELETE  /share-cities/:id} : delete the "id" shareCity.
     *
     * @param id the id of the shareCityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/share-cities/{id}")
    public ResponseEntity<Void> deleteShareCity(@PathVariable Long id) {
        log.debug("REST request to delete ShareCity : {}", id);
        shareCityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
