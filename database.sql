-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: chat_application
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tblmessage`
--

DROP TABLE IF EXISTS `tblmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tblmessage` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `textContent` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `tblUserInARoomId` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tblUserInARoomId` (`tblUserInARoomId`),
  CONSTRAINT `tblmessage_ibfk_1` FOREIGN KEY (`tblUserInARoomId`) REFERENCES `tbluserinaroom` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblmessage`
--

LOCK TABLES `tblmessage` WRITE;
/*!40000 ALTER TABLE `tblmessage` DISABLE KEYS */;
INSERT INTO `tblmessage` VALUES (31,'alo alo','2021-11-08 02:19:56',1),(32,'b lo b lo','2021-11-08 02:20:26',2),(33,'so what u gonna đú','2021-11-08 02:20:41',1),(34,'loài người này thật là lú','2021-11-08 02:20:49',2),(35,'nhưng mà trông thì rất cool','2021-11-08 02:20:59',1),(36,'bên ngoài kia thì đang ối dồi ôi','2021-11-08 02:21:10',2),(37,'so what you gonna đú','2021-11-08 02:21:31',2),(38,'ss','2021-11-08 02:23:37',1),(39,'ss','2021-11-08 02:31:18',2),(40,'ss','2021-11-08 02:31:22',1),(41,'alo alo','2021-11-09 12:37:12',2),(42,'alo','2021-11-09 12:37:19',1),(43,'à','2021-11-09 12:37:26',2),(44,'sd','2021-11-09 12:38:15',1),(45,'s','2021-11-09 12:39:29',1),(46,'ád','2021-11-09 02:12:00',1),(47,'alo123','2021-11-10 03:22:01',1),(48,'helo helo','2021-11-10 03:22:21',1),(49,'bep bep bep','2021-11-10 03:25:41',1),(50,'s','2021-11-16 12:15:38',1),(51,'s','2021-11-16 12:16:02',2),(52,'s','2021-11-16 12:16:11',2),(53,'s','2021-11-16 12:16:53',2),(54,'s','2021-11-16 12:17:00',1),(55,'s','2021-11-16 12:18:12',1),(56,'s','2021-11-16 12:18:16',1),(57,'s','2021-11-16 12:18:36',1),(58,'alo alo','2021-11-16 12:18:41',1),(59,'alo alo','2021-11-16 12:20:59',2),(60,'sao bro','2021-11-16 12:21:06',1),(61,'alo alo','2021-11-16 12:21:33',2),(62,'a','2021-11-16 12:24:37',1),(63,'a','2021-11-16 12:24:44',1),(64,'lolo','2021-11-16 12:24:53',1),(65,'lolo','2021-11-16 12:26:31',1),(66,'hihi','2021-11-16 12:26:42',1),(67,'123','2021-11-16 12:32:18',1),(68,'123','2021-11-16 12:32:26',1),(69,'alo alo','2021-11-16 01:44:11',2),(70,'alo toi off day','2021-11-16 01:44:18',1),(71,'alo alo','2021-11-16 02:51:42',1),(72,'a','2021-11-16 14:57:28',1),(73,'alo','2021-11-16 14:58:04',2),(81,'1','2021-11-16 16:47:25',1),(89,'alo','2021-11-16 17:42:49',5),(90,'alo alo','2021-11-16 17:44:35',2),(91,'alo alo','2021-11-16 17:45:22',2),(92,'aloalo','2021-11-16 17:45:28',6),(93,'123','2021-11-16 17:46:16',6),(94,'123','2021-11-16 17:46:23',2),(95,'s','2021-11-16 21:13:22',1),(96,'alo alo','2021-11-16 21:25:46',1),(97,'123','2021-11-16 21:26:03',5),(98,'asd sfsf','2021-11-16 22:55:00',1),(99,':)','2021-11-16 23:03:24',1),(100,':)','2021-11-16 23:03:35',1),(101,'sf :)','2021-11-16 23:05:24',1),(102,':)','2021-11-16 23:19:57',1),(103,':)','2021-11-16 23:40:16',1),(104,'(:','2021-11-17 01:00:30',5),(105,'<(\")','2021-11-17 01:01:28',5),(106,'<(\")','2021-11-17 01:53:35',1),(107,':)','2021-11-17 01:57:46',1),(108,':0','2021-11-17 01:58:00',1),(109,':)','2021-11-17 01:58:02',1),(110,':o','2021-11-17 01:58:06',1),(111,':o','2021-11-17 01:58:07',1),(112,':o','2021-11-17 01:58:08',1),(113,':o','2021-11-17 01:58:10',1),(114,'alo alo','2021-11-17 01:58:12',1),(115,'lo','2021-11-17 02:06:17',2),(116,':)','2021-11-17 02:10:39',5),(117,'df','2021-11-17 02:25:57',5),(118,':)','2021-11-17 02:26:11',5),(119,':)','2021-11-17 02:26:25',15),(120,'<(\")','2021-11-17 02:27:32',17),(121,':)\\','2021-11-17 02:27:54',17),(122,':)','2021-11-17 02:27:58',17),(123,'alo alo','2021-11-17 02:28:01',17),(124,':D','2021-11-17 02:28:11',17),(125,'<(\")','2021-11-17 02:30:01',17),(126,'alo alo hello','2021-11-17 02:30:07',17),(127,'<(\")','2021-11-17 02:39:20',1),(128,'<(\")','2021-11-17 02:44:10',15),(129,':(','2021-11-17 02:44:14',15),(130,'^_^','2021-11-17 02:44:23',15),(131,'<3','2021-11-17 02:44:42',15),(132,'<3','2021-11-17 02:44:42',15),(133,':D','2021-11-17 02:44:47',15),(134,':v','2021-11-17 02:55:30',1),(135,':(','2021-11-17 02:55:32',1),(136,':D','2021-11-17 02:55:34',1),(137,'alo ','2021-11-17 02:56:29',5),(138,':v','2021-11-17 02:56:33',5),(139,'<(\")','2021-11-17 02:56:36',5),(140,':3','2021-11-17 02:56:40',6),(141,'(:','2021-11-17 02:56:46',5),(142,'<3','2021-11-17 02:59:10',6),(143,':(','2021-11-17 02:59:12',6),(144,'^_^','2021-11-17 02:59:13',6),(145,'helooo','2021-11-17 02:59:31',5),(146,'<3','2021-11-18 19:54:03',1),(147,':D','2021-11-18 19:54:08',1),(148,'(:','2021-11-18 19:54:13',1),(149,':)','2021-11-18 19:54:16',1),(150,'<(\")','2021-11-18 19:54:22',1),(151,'hehe','2021-11-18 19:54:26',1),(152,'hello bro','2021-11-18 19:55:19',16),(153,'B)','2021-11-18 19:55:21',16),(154,'<(\")','2021-11-18 19:56:49',16),(155,'(:','2021-11-18 19:56:53',16),(156,'<(\")','2021-11-18 19:57:10',16),(157,'(:','2021-11-18 19:57:16',16),(158,'B)','2021-11-18 19:57:24',16),(159,':o','2021-11-18 19:57:28',16),(160,':/','2021-11-18 19:57:30',16),(161,':v','2021-11-18 20:01:01',16),(162,'alo','2021-11-19 15:42:27',25),(163,':o','2021-11-19 15:42:35',25),(164,'<(\")','2021-11-19 15:42:36',25),(165,'<3','2021-11-19 17:21:44',25),(166,':(','2021-11-19 17:24:04',27),(167,'<(\")','2021-11-19 17:24:08',27),(168,'alo alo','2021-11-19 17:24:12',27),(169,'alo alo','2021-11-19 17:24:38',28),(170,'bye nhé','2021-11-19 17:24:44',28),(171,'helu','2021-11-19 17:25:11',1),(172,'(:','2021-11-19 17:25:13',1),(173,'B)','2021-11-19 17:25:43',2),(174,'<(\")','2021-11-19 17:25:45',2),(175,':o','2021-11-19 17:25:48',2),(176,':/','2021-11-19 17:25:54',2),(177,'(:','2021-11-19 17:25:58',2),(178,':(','2021-11-19 17:26:00',2),(179,'B)','2021-11-19 17:26:03',2),(180,'B)','2021-11-19 17:26:05',2),(181,':v','2021-11-19 17:50:20',1),(182,'<(\")','2021-11-19 17:50:29',1),(183,'hello bro','2021-11-19 17:50:36',1),(184,'alo ','2021-11-19 17:52:22',1),(185,'alo alo ','2021-11-19 17:53:37',1),(194,'hello bro','2021-11-19 18:14:39',31),(195,'<(\")','2021-11-19 18:14:41',31),(196,':/','2021-11-19 18:14:44',31);
/*!40000 ALTER TABLE `tblmessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblroom`
--

DROP TABLE IF EXISTS `tblroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tblroom` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblroom`
--

LOCK TABLES `tblroom` WRITE;
/*!40000 ALTER TABLE `tblroom` DISABLE KEYS */;
INSERT INTO `tblroom` VALUES (1,NULL),(3,NULL),(9,NULL),(10,NULL),(13,NULL),(14,NULL),(15,NULL),(17,NULL);
/*!40000 ALTER TABLE `tblroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbluser`
--

DROP TABLE IF EXISTS `tbluser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tbluser` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phoneNo` varchar(255) DEFAULT NULL,
  `onlineStatus` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbluser`
--

LOCK TABLES `tbluser` WRITE;
/*!40000 ALTER TABLE `tbluser` DISABLE KEYS */;
INSERT INTO `tbluser` VALUES (1,'saf','sf','asfas','asf','123',0),(2,'ssss','asf','asf','asfasf','123',0),(3,'huy2110','123','Cong Huy','huync21@gmail.com','092573825',0),(4,'hunghung123','123','Quang Hung','hung@123.com','09249529',0),(5,'cuong','123','cuong nguyeng','ádasd','124125',0),(6,'test','123','test','test@test.com','1235989',0),(7,'test1','123','test 2','test@test.com','9218741',0),(8,'test2','123','test 3','test@test.com','29304884',0),(10,'quang','123','Vu Minh Quang','','',0);
/*!40000 ALTER TABLE `tbluser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbluserinaroom`
--

DROP TABLE IF EXISTS `tbluserinaroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tbluserinaroom` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `tblUserId` int(10) DEFAULT NULL,
  `tblRoomId` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tblUserId` (`tblUserId`),
  KEY `tblRoomId` (`tblRoomId`),
  CONSTRAINT `tbluserinaroom_ibfk_1` FOREIGN KEY (`tblUserId`) REFERENCES `tbluser` (`id`),
  CONSTRAINT `tbluserinaroom_ibfk_2` FOREIGN KEY (`tblRoomId`) REFERENCES `tblroom` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbluserinaroom`
--

LOCK TABLES `tbluserinaroom` WRITE;
/*!40000 ALTER TABLE `tbluserinaroom` DISABLE KEYS */;
INSERT INTO `tbluserinaroom` VALUES (1,3,1),(2,4,1),(5,3,3),(6,5,3),(15,3,9),(16,1,9),(17,5,10),(18,4,10),(23,3,13),(24,2,13),(25,3,14),(26,6,14),(27,3,15),(28,7,15),(31,10,17),(32,3,17);
/*!40000 ALTER TABLE `tbluserinaroom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-20  8:26:07
