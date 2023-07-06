package org.pierre.shareazade.service;

import java.util.Optional;
import org.pierre.shareazade.domain.ShareUser;
import org.pierre.shareazade.repository.ShareUserRepository;
import org.pierre.shareazade.service.dto.ShareUserDTO;
import org.pierre.shareazade.service.mapper.ShareUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShareUser}.
 */
@Service
@Transactional
public class ShareUserService {

    private final Logger log = LoggerFactory.getLogger(ShareUserService.class);

    private final ShareUserRepository shareUserRepository;

    private final ShareUserMapper shareUserMapper;

    public ShareUserService(ShareUserRepository shareUserRepository, ShareUserMapper shareUserMapper) {
        this.shareUserRepository = shareUserRepository;
        this.shareUserMapper = shareUserMapper;
    }

    /**
     * Save a shareUser.
     *
     * @param shareUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareUserDTO save(ShareUserDTO shareUserDTO) {
        log.debug("Request to save ShareUser : {}", shareUserDTO);
        ShareUser shareUser = shareUserMapper.toEntity(shareUserDTO);
        shareUser = shareUserRepository.save(shareUser);
        return shareUserMapper.toDto(shareUser);
    }

    /**
     * Update a shareUser.
     *
     * @param shareUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareUserDTO update(ShareUserDTO shareUserDTO) {
        log.debug("Request to update ShareUser : {}", shareUserDTO);
        ShareUser shareUser = shareUserMapper.toEntity(shareUserDTO);
        shareUser = shareUserRepository.save(shareUser);
        return shareUserMapper.toDto(shareUser);
    }

    /**
     * Partially update a shareUser.
     *
     * @param shareUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShareUserDTO> partialUpdate(ShareUserDTO shareUserDTO) {
        log.debug("Request to partially update ShareUser : {}", shareUserDTO);

        return shareUserRepository
            .findById(shareUserDTO.getId())
            .map(existingShareUser -> {
                shareUserMapper.partialUpdate(existingShareUser, shareUserDTO);

                return existingShareUser;
            })
            .map(shareUserRepository::save)
            .map(shareUserMapper::toDto);
    }

    /**
     * Get all the shareUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShareUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShareUsers");
        return shareUserRepository.findAll(pageable).map(shareUserMapper::toDto);
    }

    /**
     * Get one shareUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShareUserDTO> findOne(Long id) {
        log.debug("Request to get ShareUser : {}", id);
        return shareUserRepository.findById(id).map(shareUserMapper::toDto);
    }

    /**
     * Delete the shareUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShareUser : {}", id);
        shareUserRepository.deleteById(id);
    }
}
