CREATE DATABASE  IF NOT EXISTS `sportellosolidarieta` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `sportellosolidarieta`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: sportellosolidarieta
-- ------------------------------------------------------
-- Server version	8.0.15

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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `appointment` (
  `id_appuntamento` int(11) NOT NULL,
  `data_ora_appuntamento` datetime DEFAULT NULL,
  `durata` int(11) DEFAULT NULL,
  `f_effettuato` tinyint(1) DEFAULT '0',
  `id_assistito` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_appuntamento`),
  KEY `FK_APPOINTMENT_id_assistito` (`id_assistito`),
  CONSTRAINT `FK_APPOINTMENT_id_assistito` FOREIGN KEY (`id_assistito`) REFERENCES `person` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting`
--

DROP TABLE IF EXISTS `meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `meeting` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AMOUNT` float DEFAULT NULL,
  `DATE` date DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PERSON_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_MEETING_PERSON_ID` (`PERSON_ID`),
  CONSTRAINT `FK_MEETING_PERSON_ID` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (1,123,'2019-03-24','test',1);
/*!40000 ALTER TABLE `meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `person` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BIRTHDATE` date DEFAULT NULL,
  `FAMILYCOMPOSITION` varchar(255) DEFAULT NULL,
  `ISDELETED` tinyint(1) DEFAULT '0',
  `ISREFUSED` tinyint(1) DEFAULT '0',
  `ISREUNITEDWITHFAMILY` tinyint(1) DEFAULT '0',
  `NAME` varchar(255) DEFAULT NULL,
  `NATIONALITY` varchar(255) DEFAULT NULL,
  `SEX` char(1) DEFAULT NULL,
  `SURNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `Person_Index_SurnameName` (`SURNAME`,`NAME`),
  KEY `Person_Index_Name` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,NULL,NULL,0,0,0,'Mario','Italiana','M','Rossi');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `settings` (
  `id_impostazione` int(11) NOT NULL,
  `durata` int(11) DEFAULT NULL,
  `d_controllo_appuntamenti` date DEFAULT NULL,
  `f_venerdi` tinyint(1) DEFAULT '0',
  `f_lunedi` tinyint(1) DEFAULT '0',
  `f_sabato` tinyint(1) DEFAULT '0',
  `f_domenica` tinyint(1) DEFAULT '0',
  `f_giovedi` tinyint(1) DEFAULT '0',
  `f_martedi` tinyint(1) DEFAULT '0',
  `f_mercoledi` tinyint(1) DEFAULT '0',
  `h_fine` time DEFAULT NULL,
  `h_inizio` time DEFAULT NULL,
  `max_appuntamenti` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_impostazione`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,30,'2019-03-27',0,0,0,0,1,0,0,'17:30:00','08:30:00',16);
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-26 14:39:21
