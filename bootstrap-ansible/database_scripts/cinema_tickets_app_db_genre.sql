-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: cinema_tickets_app_db
-- ------------------------------------------------------
-- Server version	8.0.20

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
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `id` bigint NOT NULL,
  `created_at` datetime NOT NULL,
  `object_state` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `updated_by_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1016ks3339xks0t3xih7gtxuu` (`created_by_id`),
  KEY `FK76qo2ggap75govlm0dnq8wkiy` (`updated_by_id`),
  CONSTRAINT `FK1016ks3339xks0t3xih7gtxuu` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK76qo2ggap75govlm0dnq8wkiy` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (2,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Action',1,1),(3,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Adventure',1,1),(4,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Comedy',1,1),(5,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Sci-Fi',1,1),(6,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Crime',1,1),(7,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Drama',1,1),(8,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Thriller',1,1),(9,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Biography',1,1),(10,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','History',1,1),(11,'2020-05-30 16:22:48','ACTIVE','2020-05-30 16:22:48','Fantasy',1,1),(12,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Western',1,1),(13,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Romance',1,1),(14,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Mystery',1,1),(15,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','War',1,1),(16,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Family',1,1),(17,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Animation',1,1),(18,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Musical',1,1),(19,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Music',1,1),(20,'2020-05-30 16:22:49','ACTIVE','2020-05-30 16:22:49','Horror',1,1);
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-15 19:11:26
