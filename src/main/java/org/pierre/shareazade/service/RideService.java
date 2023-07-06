package org.pierre.shareazade.service;

import java.util.Optional;
import org.pierre.shareazade.domain.Ride;
import org.pierre.shareazade.repository.RideRepository;
import org.pierre.shareazade.service.dto.RideDTO;
import org.pierre.shareazade.service.mapper.RideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ride}.
 */
@Service
@Transactional
public class RideService {

    private final Logger log = LoggerFactory.getLogger(RideService.class);

    private final RideRepository rideRepository;

    private final RideMapper rideMapper;

    public RideService(RideRepository rideRepository, RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
    }

    /**
     * Save a ride.
     *
     * @param rideDTO the entity to save.
     * @return the persisted entity.
     */
    public RideDTO save(RideDTO rideDTO) {
        log.debug("Request to save Ride : {}", rideDTO);
        Ride ride = rideMapper.toEntity(rideDTO);
        ride = rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    /**
     * Update a ride.
     *
     * @param rideDTO the entity to save.
     * @return the persisted entity.
     */
    public RideDTO update(RideDTO rideDTO) {
        log.debug("Request to update Ride : {}", rideDTO);
        Ride ride = rideMapper.toEntity(rideDTO);
        ride = rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    /**
     * Partially update a ride.
     *
     * @param rideDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RideDTO> partialUpdate(RideDTO rideDTO) {
        log.debug("Request to partially update Ride : {}", rideDTO);

        return rideRepository
            .findById(rideDTO.getId())
            .map(existingRide -> {
                rideMapper.partialUpdate(existingRide, rideDTO);

                return existingRide;
            })
            .map(rideRepository::save)
            .map(rideMapper::toDto);
    }

    /**
     * Get all the rides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rides");
        return rideRepository.findAll(pageable).map(rideMapper::toDto);
    }

    /**
     * Get all the rides with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RideDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rideRepository.findAllWithEagerRelationships(pageable).map(rideMapper::toDto);
    }

    /**
     * Get one ride by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RideDTO> findOne(Long id) {
        log.debug("Request to get Ride : {}", id);
        return rideRepository.findOneWithEagerRelationships(id).map(rideMapper::toDto);
    }

    /**
     * Delete the ride by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ride : {}", id);
        rideRepository.deleteById(id);
    }
}
