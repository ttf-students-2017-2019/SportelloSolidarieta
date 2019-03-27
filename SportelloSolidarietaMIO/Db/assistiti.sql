-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: assistiti
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
-- Table structure for table `appuntamento`
--

DROP TABLE IF EXISTS `appuntamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `appuntamento` (
  `id_appuntamento` int(11) NOT NULL AUTO_INCREMENT,
  `id_assistito` int(6) unsigned NOT NULL,
  `data_ora_appuntamento` datetime NOT NULL,
  `f_effettuato` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id_appuntamento`),
  KEY `id_assistito_idx` (`id_assistito`),
  CONSTRAINT `id_assistito` FOREIGN KEY (`id_assistito`) REFERENCES `assistito` (`id_assistito`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appuntamento`
--

LOCK TABLES `appuntamento` WRITE;
/*!40000 ALTER TABLE `appuntamento` DISABLE KEYS */;
INSERT INTO `appuntamento` VALUES (1,1,'2019-04-25 09:00:00',_binary '\0');
/*!40000 ALTER TABLE `appuntamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assistito`
--

DROP TABLE IF EXISTS `assistito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `assistito` (
  `id_assistito` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `cognome` varchar(60) NOT NULL,
  `nome` varchar(60) NOT NULL,
  `d_nascita` date DEFAULT '1900-01-01',
  `c_sesso` char(1) NOT NULL,
  `c_nazionalita` varchar(3) DEFAULT NULL,
  `foto` varchar(300) DEFAULT NULL,
  `f_ricongiungimento` bit(1) DEFAULT b'0',
  `f_rifiutato` bit(1) DEFAULT b'0',
  `f_deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id_assistito`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assistito`
--

LOCK TABLES `assistito` WRITE;
/*!40000 ALTER TABLE `assistito` DISABLE KEYS */;
INSERT INTO `assistito` VALUES (1,'Piccioni','Eros','1989-10-27','M','IT',NULL,_binary '\0',_binary '\0',_binary '\0');
/*!40000 ALTER TABLE `assistito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `impostazioni`
--

DROP TABLE IF EXISTS `impostazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `impostazioni` (
  `id_impostazione` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `f_lunedi` bit(1) NOT NULL DEFAULT b'0',
  `f_martedi` bit(1) NOT NULL DEFAULT b'0',
  `f_mercoledi` bit(1) NOT NULL DEFAULT b'0',
  `f_giovedi` bit(1) NOT NULL DEFAULT b'0',
  `f_venerdi` bit(1) NOT NULL DEFAULT b'0',
  `f_sabato` bit(1) NOT NULL DEFAULT b'0',
  `f_domenica` bit(1) NOT NULL DEFAULT b'0',
  `durata` int(11) NOT NULL,
  `max_appuntamenti` int(11) NOT NULL,
  `h_inizio` time NOT NULL,
  `h_fine` time NOT NULL,
  `d_controllo_appuntamenti` date DEFAULT NULL,
  PRIMARY KEY (`id_impostazione`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `impostazioni`
--

LOCK TABLES `impostazioni` WRITE;
/*!40000 ALTER TABLE `impostazioni` DISABLE KEYS */;
INSERT INTO `impostazioni` VALUES (1,_binary '\0',_binary '\0',_binary '\0',_binary '',_binary '\0',_binary '\0',_binary '\0',10,20,'07:00:00','17:00:00',NULL);
/*!40000 ALTER TABLE `impostazioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incontro`
--

DROP TABLE IF EXISTS `incontro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `incontro` (
  `id_incontro` int(11) unsigned NOT NULL,
  `id_assistito` int(6) unsigned DEFAULT NULL,
  `d_incontro` date NOT NULL,
  `descrizione` varchar(3000) DEFAULT '',
  `i_donazione` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`id_incontro`),
  KEY `FK_incontro_to_assistito` (`id_assistito`),
  CONSTRAINT `FK_incontro_to_assistito` FOREIGN KEY (`id_assistito`) REFERENCES `assistito` (`id_assistito`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incontro`
--

LOCK TABLES `incontro` WRITE;
/*!40000 ALTER TABLE `incontro` DISABLE KEYS */;
/*!40000 ALTER TABLE `incontro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'assistiti'
--

--
-- Dumping routines for database 'assistiti'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-18 10:45:18
