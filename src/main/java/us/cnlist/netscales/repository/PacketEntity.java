package us.cnlist.netscales.repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Network packet as database entity
 */

@Entity
@Table(name = "t_collector_data")
public class PacketEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "ss")
    @SequenceGenerator(name = "ss", sequenceName = "seq_coldata", allocationSize = 1)
    private Long id;
    @Column(name = "packet_unique_id")
    private Long packetUniqueId;
    @Column(name = "channel_data")
    private Long channelData;
    @Column(name = "channel_id")
    private Integer channelId;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "entry_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "raw_data")
    private byte[] rawData;

    public PacketEntity() {

    }

    public Long getPacketUniqueId() {
        return packetUniqueId;
    }

    public void setPacketUniqueId(Long packetUniqueId) {
        this.packetUniqueId = packetUniqueId;
    }


    public Long getChannelData() {
        return channelData;
    }

    public void setChannelData(Long channelData) {
        this.channelData = channelData;
    }


    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
