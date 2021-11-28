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
-- Table structure for table `tblfriend`
--

DROP TABLE IF EXISTS `tblfriend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tblfriend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UserID1` int(11) DEFAULT NULL,
  `UserID2` int(11) DEFAULT NULL,
  `friendStatus` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_UID1_idx` (`UserID1`),
  KEY `FK_UID2_idx` (`UserID2`),
  CONSTRAINT `FK_UID1` FOREIGN KEY (`UserID1`) REFERENCES `tbluser` (`id`),
  CONSTRAINT `FK_UID2` FOREIGN KEY (`UserID2`) REFERENCES `tbluser` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblfriend`
--

LOCK TABLES `tblfriend` WRITE;
/*!40000 ALTER TABLE `tblfriend` DISABLE KEYS */;
INSERT INTO `tblfriend` VALUES (1,3,1,1),(3,3,4,1),(4,3,5,1),(5,3,6,1);
/*!40000 ALTER TABLE `tblfriend` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblmessage`
--

LOCK TABLES `tblmessage` WRITE;
/*!40000 ALTER TABLE `tblmessage` DISABLE KEYS */;
INSERT INTO `tblmessage` VALUES (250,'alo','2021-11-23 22:37:26',75),(251,':v','2021-11-23 23:06:20',81),(257,'alo','2021-11-25 20:43:51',92),(258,':v','2021-11-25 20:45:04',93),(259,'<3','2021-11-25 20:45:08',93),(260,'(:','2021-11-25 20:45:13',93),(261,'<3','2021-11-25 20:46:44',81),(262,':(','2021-11-25 20:46:45',81),(263,':v','2021-11-25 20:46:46',81),(264,'<(\")','2021-11-25 20:46:48',81),(265,':o','2021-11-25 20:46:50',81),(266,'<3','2021-11-25 20:55:02',75),(267,':v','2021-11-25 20:57:36',75),(268,':D','2021-11-25 20:58:22',75),(269,'<(\")','2021-11-25 20:58:32',75),(270,':o','2021-11-25 20:58:34',75),(271,'<3','2021-11-25 20:58:50',75),(272,'alo alo','2021-11-25 22:43:20',98),(273,'alo alo','2021-11-25 22:46:52',81),(274,'<3','2021-11-25 22:46:54',81),(275,'alo alo','2021-11-25 22:47:27',82),(276,'alo 123','2021-11-25 22:47:30',82),(277,'(:','2021-11-25 22:47:32',82),(278,'<(\")','2021-11-25 22:47:36',81),(279,'B)','2021-11-25 22:47:38',82),(280,'(:','2021-11-25 22:47:40',81),(281,'helooooo','2021-11-25 22:47:46',82),(282,'asdashf','2021-11-25 22:47:47',82),(283,'aloo asdjkas sdfsdf','2021-11-25 22:47:51',82),(284,'ông đã làm xong UI chưa','2021-11-25 22:48:11',82),(285,'tôi làm gần xong rồi','2021-11-25 22:48:18',81),(286,'B)','2021-11-25 22:48:26',82),(287,'ngon','2021-11-25 22:48:31',82),(288,'<(\")','2021-11-25 22:48:41',81),(289,'đẹp cực ','2021-11-25 22:48:43',81),(290,'<(\")','2021-11-25 22:48:46',81),(291,'alo alo','2021-11-25 22:49:19',101),(292,'alo alo','2021-11-25 22:51:31',103),(293,'haha nhìn ngựa ngựa','2021-11-25 23:02:12',81),(294,'alo alo','2021-11-25 23:03:36',81),(295,'alo alo :D','2021-11-25 23:04:07',82),(296,':D','2021-11-25 23:04:09',82),(297,':D','2021-11-25 23:04:14',82),(298,'B)','2021-11-25 23:04:19',82),(299,'<(\")','2021-11-25 23:04:24',81),(300,'alo alo bro','2021-11-25 23:05:27',93),(301,'alo alo bro','2021-11-25 23:05:59',75),(302,':(','2021-11-25 23:06:11',75),(303,'alo alo','2021-11-25 23:08:37',108),(304,'hello bro','2021-11-25 23:08:42',107),(305,'B)','2021-11-25 23:08:45',108),(306,'<3','2021-11-25 23:08:46',107),(307,':D','2021-11-25 23:08:48',107),(308,'alo alo','2021-11-25 23:09:44',118),(309,':v','2021-11-25 23:09:47',118),(310,'123456','2021-11-25 23:09:51',117),(311,'<(\")','2021-11-25 23:09:57',117),(312,'hehe','2021-11-25 23:10:15',116);
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
  `isSingleChat` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblroom`
--

LOCK TABLES `tblroom` WRITE;
/*!40000 ALTER TABLE `tblroom` DISABLE KEYS */;
INSERT INTO `tblroom` VALUES (36,NULL,1),(39,NULL,1),(42,'Phan tich thiet ke',0),(43,NULL,1),(44,'Lap trinh mang',0),(45,NULL,1),(46,NULL,1),(47,NULL,1),(48,NULL,1),(49,NULL,1),(50,NULL,1),(51,NULL,1),(52,NULL,1),(53,'Lap trinh mobile',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbluser`
--

LOCK TABLES `tbluser` WRITE;
/*!40000 ALTER TABLE `tbluser` DISABLE KEYS */;
INSERT INTO `tbluser` VALUES (1,'saf','sf','asfas','asf','123',0),(2,'ssss','asf','asf','asfasf','123',0),(3,'huy2110','123','Cong Huy','huync21@gmail.com','092573825',0),(4,'hunghung123','123','Quang Hung','hung@123.com','09249529',0),(5,'cuong','123','cuong nguyeng','ádasd','124125',0),(6,'test','123','test','test@test.com','1235989',0),(7,'test1','123','test 2','test@test.com','9218741',0),(8,'test2','123','test 3','test@test.com','29304884',0),(10,'quang','123','Vu Minh Quang','','',0),(11,'test4','123','test','','',0),(12,'quang1','123','quang','abc','abc',0),(13,'quang2','123','123123','123','123',0),(14,'qqqqqq','qqqq','qqqqq','qqqq','qqqqqqq',0),(15,'daoquanghung','hunghung123','Dao Quang Hung','daoquanghung92k@gmail.com','0909382899',0),(16,'huyblabla','123','Nguyễn Công Huy','huync11@gmail.com','02378970',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbluserinaroom`
--

LOCK TABLES `tbluserinaroom` WRITE;
/*!40000 ALTER TABLE `tbluserinaroom` DISABLE KEYS */;
INSERT INTO `tbluserinaroom` VALUES (75,3,36),(76,4,36),(81,3,39),(82,5,39),(89,1,42),(90,5,42),(91,4,42),(92,3,42),(93,3,43),(94,15,43),(95,4,44),(96,2,44),(97,1,44),(98,3,44),(99,3,45),(100,6,45),(101,5,46),(102,15,46),(103,3,47),(104,1,47),(105,3,48),(106,2,48),(107,3,49),(108,16,49),(109,3,50),(110,10,50),(111,4,51),(112,15,51),(113,4,52),(114,10,52),(115,4,53),(116,5,53),(117,16,53),(118,3,53);
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

-- Dump completed on 2021-11-28 15:49:53
