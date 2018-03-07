package cn.cnic.trackrecord;

import cn.cnic.trackrecord.core.track.TrackLuceneFormatter;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.lucene.TrackLucene;
import cn.cnic.trackrecord.plugin.lucene.LuceneBean;
import cn.cnic.trackrecord.service.TrackPointService;
import cn.cnic.trackrecord.service.TrackService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

//	@Autowired
//	private TrackService trackService;
//
//	@Autowired
//	private TrackPointService trackPointService;
//
//	@Autowired
//	private LuceneBean luceneBean;
//
//	@Test
//	public void contextLoads() throws IOException {
//		Track track = trackService.get(78);
//		List<TrackPoint> points = trackPointService.getByTrackId(track.getId());
//		luceneBean.add(new TrackLuceneFormatter(), new TrackLucene(track, points));
//	}

}
