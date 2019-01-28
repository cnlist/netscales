package us.cnlist.netscales.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EntityRepository extends CrudRepository<PacketEntity, Long> {

    @Query(nativeQuery = true,value = "INSERT INTO T_COLLECTOR_DATA (packet_unique_id," +
            "channel_id," +
            "ip_address," +
            "channel_data, raw_data) VALUES (:pid,:channelId,:ipAddress,:data, :rawd)")
    @Modifying
    void create(Long pid, Integer channelId, String ipAddress, Long data,byte[] rawd);

}
