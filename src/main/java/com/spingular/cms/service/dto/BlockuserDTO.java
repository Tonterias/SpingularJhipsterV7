package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.cms.domain.Blockuser} entity.
 */
public class BlockuserDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private AppuserDTO blockeduser;

    private AppuserDTO blockinguser;

    private CommunityDTO cblockeduser;

    private CommunityDTO cblockinguser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public AppuserDTO getBlockeduser() {
        return blockeduser;
    }

    public void setBlockeduser(AppuserDTO blockeduser) {
        this.blockeduser = blockeduser;
    }

    public AppuserDTO getBlockinguser() {
        return blockinguser;
    }

    public void setBlockinguser(AppuserDTO blockinguser) {
        this.blockinguser = blockinguser;
    }

    public CommunityDTO getCblockeduser() {
        return cblockeduser;
    }

    public void setCblockeduser(CommunityDTO cblockeduser) {
        this.cblockeduser = cblockeduser;
    }

    public CommunityDTO getCblockinguser() {
        return cblockinguser;
    }

    public void setCblockinguser(CommunityDTO cblockinguser) {
        this.cblockinguser = cblockinguser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockuserDTO)) {
            return false;
        }

        BlockuserDTO blockuserDTO = (BlockuserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blockuserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockuserDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", blockeduser=" + getBlockeduser() +
            ", blockinguser=" + getBlockinguser() +
            ", cblockeduser=" + getCblockeduser() +
            ", cblockinguser=" + getCblockinguser() +
            "}";
    }
}
