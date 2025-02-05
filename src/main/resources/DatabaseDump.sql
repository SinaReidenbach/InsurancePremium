-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: insurancepremier
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `kilometers`
--

DROP TABLE IF EXISTS `kilometers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kilometers` (
  `km_id` int NOT NULL AUTO_INCREMENT,
  `km_min` int DEFAULT NULL,
  `km_max` int DEFAULT NULL,
  `factor` double DEFAULT NULL,
  PRIMARY KEY (`km_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kilometers`
--

LOCK TABLES `kilometers` WRITE;
/*!40000 ALTER TABLE `kilometers` DISABLE KEYS */;
INSERT INTO `kilometers` VALUES (1,0,5000,0.5),(2,5001,10000,1),(3,10001,20000,1.5),(4,20001,999999,2);
/*!40000 ALTER TABLE `kilometers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regions`
--

DROP TABLE IF EXISTS `regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `regions` (
  `region_id` int NOT NULL AUTO_INCREMENT,
  `region_name` varchar(255) DEFAULT NULL,
  `factor` double DEFAULT NULL,
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'Baden-Württemberg',1.5),(2,'Bayern',1.5),(3,'Berlin',2),(4,'Brandenburg',1),(5,'Bremen',1.5),(6,'Hamburg',1.5),(7,'Hessen',1),(8,'Mecklenburg-Vorpommern',0.5),(9,'Niedersachsen',1),(10,'Nordrhein-Westfalen',1.5),(11,'Rheinland-Pfalz',1),(12,'Sachsen',1),(13,'Sachsen-Anhalt',0.5),(14,'Schleswig-Holstein',0.5),(15,'Thüringen',1);
/*!40000 ALTER TABLE `regions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `vehicle_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_name` varchar(255) DEFAULT NULL,
  `factor` double DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,'Pkw Kraftstoff',1.5),(2,'Lkw ohne Anhänger',1.5),(3,'Motorrad',2),(4,'Fahrrad',1.5),(5,'Bus',1),(6,'Traktor',0.5),(7,'E-Scooter',1.5),(8,'Roller (Motor)',2),(9,'PKW Elektro',1),(10,'Wohnmobil',1),(11,'Taxi',1.5),(12,'Transporter',1.5),(13,'Lkw mit Anhänger',2),(14,'Geländewagen (SUV)',1.5),(15,'Moped',2);
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-05 12:04:37
