package cn.cnic.trackrecord.data.lucene;

import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户构建lucene的轨迹索引
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackLucene {
    /**
     * 轨迹信息
     */
    private Track track;
    /**
     * 轨迹点信息
     */
    private List<TrackPoint> points;
}
