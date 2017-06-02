CREATE DATABASE  IF NOT EXISTS `magic_fly` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `magic_fly`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: magic_fly
-- ------------------------------------------------------
-- Server version	5.5.21

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
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickets` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `gender` char(1) CHARACTER SET latin1 NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `reservationID` int(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (7,'M','John','Sdas',7),(8,'F','Anne','Peterson',7),(9,'M','Johny','Bravo',7),(10,'F','Marry','Johnson',8),(11,'F','Diane','Mich',8),(12,'M','Anton','Pan',9),(14,'M','Johnny','Bravo',11),(15,'M','Dean','Doooo',11),(16,'M','Gigi','Surdu',12),(17,'F','Gina','Pistol',12),(18,'M','Bill','Gatez',13),(19,'F','Maria','Chirac',14),(20,'M','Gigi','Gom',15),(21,'M','Petru','Picket',15),(22,'F','Jenny','Jackson',15);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_ranks`
--

DROP TABLE IF EXISTS `user_ranks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ranks` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_ranks`
--

LOCK TABLES `user_ranks` WRITE;
/*!40000 ALTER TABLE `user_ranks` DISABLE KEYS */;
INSERT INTO `user_ranks` VALUES (1,'admin'),(2,'staff'),(3,'client');
/*!40000 ALTER TABLE `user_ranks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `airports`
--

DROP TABLE IF EXISTS `airports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `airports` (
  `ID` varchar(3) NOT NULL,
  `name` varchar(35) NOT NULL,
  `cityID` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `airports`
--

LOCK TABLES `airports` WRITE;
/*!40000 ALTER TABLE `airports` DISABLE KEYS */;
INSERT INTO `airports` VALUES ('BBU','Baneasa',1),('CPH','Copenhagen Airport',3),('OTP','Otopeni',1),('WAW','Frederic Chopin',4);
/*!40000 ALTER TABLE `airports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `fName` varchar(25) NOT NULL,
  `lName` varchar(25) NOT NULL,
  `password` varchar(15) NOT NULL,
  `rankID` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Mircea','Mircea','Mircea123',1),(2,'Filip','Filip','Filip123',1),(9,'Dan','Dragon','Dan123',2),(10,'John','Dunhan','John123',2),(11,'Anne','Peterson','Anne123',3),(12,'Ion','Popescu','Ion123',2),(13,'Gigi','Gom','Gigi123',3);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `countries` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,'Romania'),(2,'Denmark'),(3,'Poland');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservations` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `flightScheduleID` int(10) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price` int(10) NOT NULL,
  `seatClass` varchar(15) NOT NULL,
  `status` varchar(15) NOT NULL,
  `clientID` int(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (7,9,'2012-05-06 15:17:21',15332,'Coach','unconfirmed',11),(8,9,'2012-05-06 15:28:49',8970,'Coach','unconfirmed',11),(9,9,'2012-05-06 09:38:00',2466,'Coach','canceled',11),(11,22,'2012-06-07 16:08:15',4620,'Coach','unconfirmed',11),(12,21,'2012-05-07 20:48:08',3220,'Economy','confirmed',13),(13,23,'2012-05-07 22:00:03',1610,'Economy','canceled',1),(14,20,'2012-05-08 18:36:54',1050,'Economy','confirmed',11),(15,9,'2012-04-09 11:48:04',7035,'First','confirmed',13);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight_class_types`
--

DROP TABLE IF EXISTS `flight_class_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flight_class_types` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight_class_types`
--

LOCK TABLES `flight_class_types` WRITE;
/*!40000 ALTER TABLE `flight_class_types` DISABLE KEYS */;
INSERT INTO `flight_class_types` VALUES (1,'Boeing'),(2,'Airbus'),(3,'Bombardier');
/*!40000 ALTER TABLE `flight_class_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight_schedules`
--

DROP TABLE IF EXISTS `flight_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flight_schedules` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `planeID` int(10) NOT NULL,
  `departure` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `arrival` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight_schedules`
--

LOCK TABLES `flight_schedules` WRITE;
/*!40000 ALTER TABLE `flight_schedules` DISABLE KEYS */;
INSERT INTO `flight_schedules` VALUES (1,1,'2012-05-14 10:00:00','2012-05-14 22:04:00'),(2,3,'2012-05-16 11:45:00','2012-05-16 14:44:00'),(7,1,'2012-06-08 05:30:00','2012-06-08 17:34:00'),(9,1,'2012-05-06 20:50:00','2012-05-07 08:54:00'),(19,3,'2012-06-15 22:01:00','2012-06-16 01:00:00'),(20,4,'2012-06-16 01:22:00','2012-06-16 02:58:00'),(21,1,'2012-06-16 13:33:00','2012-06-17 01:37:00'),(22,5,'2012-06-16 00:54:00','2012-06-16 03:55:00'),(23,3,'2012-05-16 04:00:00','2012-05-16 06:59:00'),(24,5,'2012-06-16 01:32:00','2012-06-16 04:33:00'),(25,6,'2012-06-16 07:00:00','2012-06-16 08:01:00');
/*!40000 ALTER TABLE `flight_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight_classes`
--

DROP TABLE IF EXISTS `flight_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flight_classes` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `flightClassTypeID` int(10) NOT NULL,
  `name` varchar(25) NOT NULL,
  `fcSeatsNo` int(4) NOT NULL,
  `ccSeatsNo` int(4) NOT NULL,
  `ecSeatsNo` int(4) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight_classes`
--

LOCK TABLES `flight_classes` WRITE;
/*!40000 ALTER TABLE `flight_classes` DISABLE KEYS */;
INSERT INTO `flight_classes` VALUES (1,1,'787 Dreamliner',3,7,22),(2,1,'777-200LR',55,2,33),(3,1,'767-400ER',20,60,140),(4,2,'A370',12,2,7),(26,1,'767-300ER',40,31,134),(28,2,'A360',27,102,160),(29,3,'Learjet 40',7,0,0);
/*!40000 ALTER TABLE `flight_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planes`
--

DROP TABLE IF EXISTS `planes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `planes` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `flightClassID` int(10) NOT NULL,
  `airport1ID` varchar(3) NOT NULL,
  `airport2ID` varchar(3) NOT NULL,
  `flightLength` varchar(5) NOT NULL,
  `available` varchar(3) NOT NULL,
  `fullPrice` int(5) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `flightClassID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planes`
--

LOCK TABLES `planes` WRITE;
/*!40000 ALTER TABLE `planes` DISABLE KEYS */;
INSERT INTO `planes` VALUES (1,1,'OTP','CPH','13:04','yes',2345),(3,2,'CPH','WAW','02:59','yes',2312),(4,28,'OTP','CPH','02:36','yes',1572),(5,26,'OTP','CPH','04:01','yes',3310),(6,4,'OTP','CPH','02:01','yes',3500);
/*!40000 ALTER TABLE `planes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cities` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `countryID` int(10) NOT NULL,
  `timeZoneID` int(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (1,'Bucharest',1,15),(2,'Iasi',1,15),(3,'Kastrup',2,14),(4,'Warsaw',3,14);
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_zones`
--

DROP TABLE IF EXISTS `time_zones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_zones` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `value` int(2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_zones`
--

LOCK TABLES `time_zones` WRITE;
/*!40000 ALTER TABLE `time_zones` DISABLE KEYS */;
INSERT INTO `time_zones` VALUES (1,-12),(2,-11),(3,-10),(4,-9),(5,-8),(6,-7),(7,-6),(8,-5),(9,-4),(10,-3),(11,-2),(12,-1),(13,0),(14,1),(15,2),(16,3),(17,4),(18,5),(19,6),(20,7),(21,8),(22,9),(23,10),(24,11),(25,12);
/*!40000 ALTER TABLE `time_zones` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-10  0:07:18
