package org.pierre.shareazade.service;

import java.util.Optional;
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.repository.ShareCityRepository;
import org.pierre.shareazade.service.dto.ShareCityDTO;
import org.pierre.shareazade.service.mapper.ShareCityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShareCity}.
 */
@Service
@Transactional
public class ShareCityService {

    private final Logger log = LoggerFactory.getLogger(ShareCityService.class);

    private final ShareCityRepository shareCityRepository;

    private final ShareCityMapper shareCityMapper;

    public ShareCityService(ShareCityRepository shareCityRepository, ShareCityMapper shareCityMapper) {
        this.shareCityRepository = shareCityRepository;
        this.shareCityMapper = shareCityMapper;
    }

    /**
     * Save a shareCity.
     *
     * @param shareCityDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareCityDTO save(ShareCityDTO shareCityDTO) {
        log.debug("Request to save ShareCity : {}", shareCityDTO);
        ShareCity shareCity = shareCityMapper.toEntity(shareCityDTO);
        shareCity = shareCityRepository.save(shareCity);
        return shareCityMapper.toDto(shareCity);
    }

    /**
     * Update a shareCity.
     *
     * @param shareCityDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareCityDTO update(ShareCityDTO shareCityDTO) {
        log.debug("Request to update ShareCity : {}", shareCityDTO);
        ShareCity shareCity = shareCityMapper.toEntity(shareCityDTO);
        shareCity = shareCityRepository.save(shareCity);
        return shareCityMapper.toDto(shareCity);
    }

    /**
     * Partially update a shareCity.
     *
     * @param shareCityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShareCityDTO> partialUpdate(ShareCityDTO shareCityDTO) {
        log.debug("Request to partially update ShareCity : {}", shareCityDTO);

        return shareCityRepository
            .findById(shareCityDTO.getId())
            .map(existingShareCity -> {
                shareCityMapper.partialUpdate(existingShareCity, shareCityDTO);

                return existingShareCity;
            })
            .map(shareCityRepository::save)
            .map(shareCityMapper::toDto);
    }

    /**
     * Get all the shareCities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareCityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShareCities");
        return shareCityRepository.findAll(pageable).map(shareCityMapper::toDto);
    }

    /**
     * Get all the shareCities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShareCityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return shareCityRepository.findAllWithEagerRelationships(pageable).map(shareCityMapper::toDto);
    }

    /**
     * Get one shareCity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShareCityDTO> findOne(Long id) {
        log.debug("Request to get ShareCity : {}", id);
        return shareCityRepository.findOneWithEagerRelationships(id).map(shareCityMapper::toDto);
    }

    /**
     * Delete the shareCity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShareCity : {}", id);
        shareCityRepository.deleteById(id);
    }
}
