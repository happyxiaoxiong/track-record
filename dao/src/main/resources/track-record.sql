CREATE DATABASE  IF NOT EXISTS `track_record` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `track_record`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 192.168.224.176    Database: track_record
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aib`
--

DROP TABLE IF EXISTS `aib`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aib` (
  `aibid` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '动物调查表id',
  `samp_line_no` int(11) NOT NULL COMMENT '样线编号',
  `weather` varchar(100) DEFAULT NULL COMMENT '天气情况',
  `time` datetime NOT NULL COMMENT '日期',
  `observer` varchar(100) DEFAULT NULL COMMENT '观察人',
  `recorder` varchar(100) NOT NULL COMMENT '记录人',
  `uid` int(11) NOT NULL COMMENT '记录人用户id',
  `place` varchar(100) NOT NULL COMMENT '地点名称',
  PRIMARY KEY (`aibid`)
) ENGINE=MyISAM AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aid`
--

DROP TABLE IF EXISTS `aid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aid` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `aibid` int(11) NOT NULL COMMENT '动物调查表id',
  `animal_name` varchar(100) NOT NULL COMMENT '物种名称',
  `quantity` int(5) DEFAULT NULL COMMENT '数量',
  `behavior` varchar(100) DEFAULT NULL COMMENT '行为',
  `enviroment` varchar(100) DEFAULT NULL COMMENT '生境',
  `site` varchar(100) NOT NULL COMMENT '地点',
  `longitude` float NOT NULL COMMENT '经度',
  `latitude` float NOT NULL COMMENT '纬度',
  `altitude` float NOT NULL COMMENT '海拔（米）',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `time` datetime NOT NULL COMMENT '记录生成时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=116 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rt_gpspoint`
--

DROP TABLE IF EXISTS `rt_gpspoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_gpspoint` (
  `id` int(16) NOT NULL AUTO_INCREMENT COMMENT 'index number',
  `user_id` int(11) DEFAULT '0' COMMENT 'user ID',
  `user_name` varchar(100) DEFAULT '' COMMENT '用户姓名',
  `longitude` double DEFAULT '0' COMMENT '经度',
  `latitude` double DEFAULT '0' COMMENT '纬度',
  `altitude` double DEFAULT '0' COMMENT '海拔',
  `time` int(11) DEFAULT '0' COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=403367 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `track_record`.`rt_gpspoint_AFTER_INSERT` AFTER INSERT ON `rt_gpspoint` FOR EACH ROW
  BEGIN
    insert into `track_record`.`rt_gpspoint_day`
    (`longitude`, `latitude`, `altitude`, `time`, `user_name`, `user_id`)
    values (
      new.`longitude`,
      new.`latitude`,
      new.`altitude`,
      new.`time`,
      new.`user_name`,
      new.`user_id`)
    on duplicate key
    update `longitude` = new.`longitude`,
      `latitude` = new.`latitude`,
      `altitude` = new.`altitude`,
      `time` = new.`time`,
      `user_name`=new.`user_name`;
  END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `rt_gpspoint_day`
--

DROP TABLE IF EXISTS `rt_gpspoint_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rt_gpspoint_day` (
  `id` int(16) NOT NULL AUTO_INCREMENT COMMENT 'index number',
  `user_id` int(11) DEFAULT '0',
  `user_name` varchar(100) DEFAULT '' COMMENT '姓名',
  `longitude` double DEFAULT '0',
  `latitude` double DEFAULT '0',
  `altitude` double DEFAULT '0',
  `time` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_user_id` (`user_id`),
  KEY `ind_time` (`time`)
) ENGINE=MyISAM AUTO_INCREMENT=5050 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `track`
--

DROP TABLE IF EXISTS `track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `track` (
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '轨迹记录ID',
  `name` varchar(100) DEFAULT '' COMMENT '轨迹名称',
  `user_id` int(11) DEFAULT '0' COMMENT '上传人id',
  `user_name` varchar(100) DEFAULT '' COMMENT '上传人名字',
  `starttime` int(11) DEFAULT '0' COMMENT '开始记录时间',
  `endtime` int(11) DEFAULT '0' COMMENT '结束记录时间',
  `length` double DEFAULT '0' COMMENT '轨迹长度',
  `maxaltitude` double DEFAULT '0' COMMENT '最高海拔',
  `keysiteslist` text COMMENT '关键地点列表',
  `path` text COMMENT '轨迹路径信息',
  `filesize` int(11) DEFAULT '0' COMMENT 'kmz文件大小，单位为字节',
  `filename` varchar(255) DEFAULT '',
  `md5` varchar(32) DEFAULT '' COMMENT '文件MD5',
  `upload_user_name` varchar(100) DEFAULT '' COMMENT '上传人姓名',
  `upload_time` int(11) DEFAULT '0' COMMENT '上传时间',
  `annotation` text COMMENT '说明',
  `upload_user_id` int(11) DEFAULT '0' COMMENT '上传人id',
  PRIMARY KEY (`id`),
  KEY `ind_user_id_start_time` (`user_id`,`starttime`)
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `track_file`
--

DROP TABLE IF EXISTS `track_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `track_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT '0' COMMENT '上传人id',
  `user_name` varchar(100) DEFAULT '',
  `state` int(11) DEFAULT '1' COMMENT '文件状态(1.上传成功待验证...;2.验证中...;3.解压中...;4.提取和保存中...;0:完成-2:验证失败;-3:解压失败;-4:提取和保存失败;-5.重试失败',
  `upload_time` int(11) DEFAULT '0' COMMENT '上传时间',
  `path` varchar(255) DEFAULT '' COMMENT '轨迹路径信息',
  `filename` varchar(255) DEFAULT '' COMMENT '文件名称',
  `filesize` int(11) DEFAULT '0' COMMENT '文件大小',
  `md5` varchar(32) DEFAULT '' COMMENT '文件md5',
  `comment` text,
  `tries` tinyint(4) DEFAULT '3' COMMENT '服务器异常重试的次数',
  `update_time` int(11) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ind_state` (`state`)
) ENGINE=InnoDB AUTO_INCREMENT=799 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `track_point`
--

DROP TABLE IF EXISTS `track_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `track_point` (
  `id` int(16) unsigned NOT NULL AUTO_INCREMENT COMMENT '轨迹点表ID',
  `track_id` int(8) DEFAULT '0' COMMENT '所属轨迹ID',
  `longitude` double DEFAULT '0' COMMENT '经度',
  `latitude` double DEFAULT '0' COMMENT '纬度',
  `altitude` double DEFAULT '0' COMMENT '高度',
  PRIMARY KEY (`id`),
  KEY `longlat` (`longitude`,`latitude`) USING HASH,
  KEY `trackid` (`track_id`),
  CONSTRAINT `trackid` FOREIGN KEY (`track_id`) REFERENCES `track` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=340525 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `track_stat`
--

DROP TABLE IF EXISTS `track_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `track_stat` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT '0' COMMENT '用户id',
  `user_name` varchar(255) DEFAULT '' COMMENT '用户名',
  `total_time` int(11) DEFAULT '0' COMMENT '巡护总时间',
  `total_length` double DEFAULT '0' COMMENT '巡护总长度',
  `total_day` int(11) DEFAULT '0' COMMENT '月巡护天数',
  `total_count` int(11) DEFAULT '0' COMMENT '巡护总次数',
  `type` tinyint(4) DEFAULT '0' COMMENT '0.按天统计,1.按月统计',
  `date` int(11) DEFAULT '0' COMMENT '统计日期',
  PRIMARY KEY (`id`),
  KEY `ind_user_id_date_type` (`user_id`,`date`,`type`),
  KEY `ind_date_type` (`date`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=757 DEFAULT CHARSET=utf8 COMMENT='轨迹统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(30) DEFAULT '' COMMENT '用户账号',
  `password` char(60) DEFAULT '' COMMENT '登录密码',
  `name` varchar(100) DEFAULT '' COMMENT '姓名',
  `gender` tinyint(4) DEFAULT '0' COMMENT '性别（0 保密 1 男 2女）',
  `birthday` int(11) DEFAULT '0' COMMENT '生日',
  `email` varchar(30) DEFAULT '' COMMENT '电子邮件地址',
  `organization` varchar(100) DEFAULT '' COMMENT '工作单位',
  `country` varchar(100) DEFAULT '' COMMENT '国家',
  `province` varchar(100) DEFAULT '' COMMENT '省',
  `city` varchar(100) DEFAULT '' COMMENT '市',
  `county` varchar(100) DEFAULT '' COMMENT '县',
  `township` varchar(100) DEFAULT '' COMMENT '乡',
  `add_time` int(11) DEFAULT '0' COMMENT '注册时间',
  `login_time` int(11) DEFAULT '0' COMMENT '上次登录时间',
  `role` tinyint(4) DEFAULT '0' COMMENT '用户角色 0 管理员 1 超级用户 2 动物调查用户 3 植物调查用户',
  `visit_count` int(11) DEFAULT '0' COMMENT '访问次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_UNIQUE` (`account`)
) ENGINE=MyISAM AUTO_INCREMENT=292 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vssb`
--

DROP TABLE IF EXISTS `vssb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vssb` (
  `vssb_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `sample_site_id` int(11) NOT NULL COMMENT '样地编号',
  `site` varchar(100) NOT NULL COMMENT '站点名称',
  `site_code` varchar(100) NOT NULL COMMENT '站点代码',
  `altitude` float NOT NULL COMMENT '海拔（米）',
  `longitude` float NOT NULL COMMENT '经度',
  `latitude` float NOT NULL COMMENT '纬度',
  `admin_region` varchar(100) NOT NULL COMMENT '所属行政区域',
  `admin_code` varchar(100) DEFAULT NULL COMMENT '行政代码',
  `sub_reserve` varchar(50) NOT NULL COMMENT '所属的自然保护分区',
  `func_area` varchar(10) DEFAULT NULL COMMENT '所属功能区',
  `proj_type` varchar(10) NOT NULL COMMENT '工程项目类别',
  `vege_type` varchar(20) DEFAULT NULL COMMENT '植被类型',
  `ss_area` float DEFAULT NULL COMMENT '样方大小',
  `coverage` float DEFAULT NULL COMMENT '总覆盖度',
  `soilero_type` varchar(10) DEFAULT NULL COMMENT '土壤侵蚀类型',
  `soilero_degree` varchar(10) DEFAULT NULL COMMENT '土壤侵蚀程度',
  `survey_index` varchar(100) DEFAULT NULL COMMENT '监测指标',
  `survey_method` varchar(100) DEFAULT NULL COMMENT '监测方法',
  `image_close` varchar(100) DEFAULT NULL COMMENT '近景照片路径',
  `image_long` varchar(100) DEFAULT NULL COMMENT '远景照片路径',
  `remark` varchar(512) DEFAULT NULL COMMENT '补充信息',
  `org` varchar(100) NOT NULL COMMENT '监测单位',
  `investigator` varchar(100) NOT NULL COMMENT '填表人',
  `uid` int(11) NOT NULL COMMENT '填表人用户id',
  `geomorphic_type` varchar(50) DEFAULT NULL COMMENT '地形地貌',
  `geological_matrix` varchar(10) DEFAULT NULL COMMENT '基质',
  `humaneffect_type` varchar(10) DEFAULT NULL COMMENT '人类活动影响方式',
  `humaneffect_degree` varchar(10) DEFAULT NULL COMMENT '人类活动影响程度',
  `animaleffect_type` varchar(30) DEFAULT NULL COMMENT '动物活动影响方式',
  `animaleffect_degree` varchar(10) DEFAULT NULL COMMENT '动物活动影响程度',
  `soil_type` varchar(10) DEFAULT NULL COMMENT '土壤类型',
  `soil_feature` varchar(10) DEFAULT NULL COMMENT '土壤特征',
  `time` datetime NOT NULL COMMENT '记录生成时间',
  PRIMARY KEY (`vssb_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vtp`
--

DROP TABLE IF EXISTS `vtp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vtp` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  `vssb_id` int(11) NOT NULL COMMENT '基本表id',
  `sample_site_id` varchar(100) NOT NULL COMMENT '样地编号',
  `site_code` varchar(100) NOT NULL COMMENT '站点代码',
  `vege_name` varchar(100) NOT NULL COMMENT '植物种类',
  `level` varchar(20) DEFAULT NULL COMMENT '层次',
  `avgheight` float DEFAULT NULL COMMENT '植株平均高度（厘米）',
  `number` int(5) DEFAULT NULL COMMENT '株（丛）数',
  `dbh` float DEFAULT NULL COMMENT '胸径',
  `project_coverage` float DEFAULT NULL COMMENT '投影盖度%',
  `life_form` varchar(50) DEFAULT NULL COMMENT '生活型',
  `branch_form` varchar(50) DEFAULT NULL COMMENT '枝叶型',
  `life_status` varchar(50) DEFAULT NULL COMMENT '生活力',
  `phenophase` varchar(50) DEFAULT NULL COMMENT '物候相',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `time` datetime NOT NULL COMMENT '记录生成时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'track_record'
--

--
-- Dumping routines for database 'track_record'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-04 11:37:58

-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 192.168.224.176    Database: track_record
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (288,'gqin','$2a$10$/ksgeZJRP/SaWkx1aXwqvuWy/A5djr9Hax2IzTF8lDlcP912IvV3m','秦刚',1,270748800,'','中科院计算机网络信息中心','中国','北京','北京市','海淀区','四号楼',1510369871,1510369871,0,138),(289,'sjp','$2a$10$zTRjZ0lI8TpwfBm711OuQuUz7Dk80t.WULGyA/2MKcvEO9L6zNOt.','史俭朋',1,1510329600,'','中科院计算机网络信息中心','中国','北京','北京','海淀区','',1510369871,1510369871,0,15),(290,'zhangying','$2a$10$jg00ndWCMk2Zd/2qRR5Y3uMx1JbRt1wc8F.a7ClIO6IoN25abuTxq','张营',2,1510329600,'','中科院计算机网络信息中心','中国','北京','北京','海淀区','',1510369871,1510369871,0,15),(291,'jecyhw','$2a$10$W8//3kAK.60cFXfobm3zL.oH9KSB7nkHleDL4aJRTQVuO3R8TxdOy','杨慧伟',1,717696000,'1147352923@qq.com','计算机网络信息中心','中国','北京','北京市','海淀区','四号楼',0,0,1,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-04 11:42:15
