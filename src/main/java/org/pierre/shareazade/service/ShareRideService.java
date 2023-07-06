package org.pierre.shareazade.service;

import java.util.Optional;
import org.pierre.shareazade.domain.ShareRide;
import org.pierre.shareazade.repository.ShareRideRepository;
import org.pierre.shareazade.service.dto.ShareRideDTO;
import org.pierre.shareazade.service.mapper.ShareRideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShareRide}.
 */
@Service
@Transactional
public class ShareRideService {

    private final Logger log = LoggerFactory.getLogger(ShareRideService.class);

    private final ShareRideRepository shareRideRepository;

    private final ShareRideMapper shareRideMapper;

    public ShareRideService(ShareRideRepository shareRideRepository, ShareRideMapper shareRideMapper) {
        this.shareRideRepository = shareRideRepository;
        this.shareRideMapper = shareRideMapper;
    }

    /**
     * Save a shareRide.
     *
     * @param shareRideDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareRideDTO save(ShareRideDTO shareRideDTO) {
        log.debug("Request to save ShareRide : {}", shareRideDTO);
        ShareRide shareRide = shareRideMapper.toEntity(shareRideDTO);
        shareRide = shareRideRepository.save(shareRide);
        return shareRideMapper.toDto(shareRide);
    }

    /**
     * Update a shareRide.
     *
     * @param shareRideDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareRideDTO update(ShareRideDTO shareRideDTO) {
        log.debug("Request to update ShareRide : {}", shareRideDTO);
        ShareRide shareRide = shareRideMapper.toEntity(shareRideDTO);
        shareRide = shareRideRepository.save(shareRide);
        return shareRideMapper.toDto(shareRide);
    }

    /**
     * Partially update a shareRide.
     *
     * @param shareRideDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShareRideDTO> partialUpdate(ShareRideDTO shareRideDTO) {
        log.debug("Request to partially update ShareRide : {}", shareRideDTO);

        return shareRideRepository
            .findById(shareRideDTO.getId())
            .map(existingShareRide -> {
                shareRideMapper.partialUpdate(existingShareRide, shareRideDTO);

                return existingShareRide;
            })
            .map(shareRideRepository::save)
            .map(shareRideMapper::toDto);
    }

    /**
     * Get all the shareRides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareRideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShareRides");
        return shareRideRepository.findAll(pageable).map(shareRideMapper::toDto);
    }

    /**
     * Get all the shareRides with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShareRideDTO> findAllWithEagerRelationships(Pageable pageable) {
        return shareRideRepository.findAllWithEagerRelationships(pageable).map(shareRideMapper::toDto);
    }

    /**
     * Get one shareRide by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShareRideDTO> findOne(Long id) {
        log.debug("Request to get ShareRide : {}", id);
        return shareRideRepository.findOneWithEagerRelationships(id).map(shareRideMapper::toDto);
    }

    /**
     * Delete the shareRide by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShareRide : {}", id);
        shareRideRepository.deleteById(id);
    }
}
