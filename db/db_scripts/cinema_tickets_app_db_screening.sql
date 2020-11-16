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
-- Table structure for table `screening`
--

DROP TABLE IF EXISTS `screening`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `screening` (
  `id` bigint NOT NULL,
  `created_at` datetime NOT NULL,
  `object_state` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `updated_by_id` bigint DEFAULT NULL,
  `movie_id` bigint DEFAULT NULL,
  `screening_room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2sdcuuxtygv90037f7dxsuqnb` (`created_by_id`),
  KEY `FKh9fgbnv5win9tbs0yviicm4n7` (`updated_by_id`),
  KEY `FKfp7sh76xc9m508stllspchnp9` (`movie_id`),
  KEY `FKvuqa35u3f84p2js056xo3c43` (`screening_room_id`),
  CONSTRAINT `FK2sdcuuxtygv90037f7dxsuqnb` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKfp7sh76xc9m508stllspchnp9` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`),
  CONSTRAINT `FKh9fgbnv5win9tbs0yviicm4n7` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKvuqa35u3f84p2js056xo3c43` FOREIGN KEY (`screening_room_id`) REFERENCES `screening_room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `screening`
--

LOCK TABLES `screening` WRITE;
/*!40000 ALTER TABLE `screening` DISABLE KEYS */;
INSERT INTO `screening` VALUES (371,'2020-05-30 16:25:00','ACTIVE','2020-08-30 16:25:00','2020-10-30 23:24:36',5.00,'2020-10-30 17:23:36',1,1,176,220),(372,'2020-05-30 21:40:16','ACTIVE','2020-08-30 21:40:16','2020-11-01 14:24:36',5.00,'2020-11-01 11:23:36',1,1,211,220),(373,'2020-05-30 21:40:25','ACTIVE','2020-08-30 21:40:25','2020-11-01 20:24:36',5.00,'2020-11-01 15:23:36',1,1,185,220),(374,'2020-05-30 21:40:34','ACTIVE','2020-08-30 21:40:34','2020-11-02 14:24:36',5.00,'2020-11-02 11:23:36',1,1,211,220),(375,'2020-05-30 21:40:41','ACTIVE','2020-08-30 21:40:41','2020-11-02 18:24:36',5.00,'2020-11-02 15:23:36',1,1,211,220),(376,'2020-05-30 21:40:47','ACTIVE','2020-08-30 21:40:47','2020-11-03 18:24:36',5.00,'2020-11-03 15:23:36',1,1,211,220),(377,'2020-05-30 21:40:54','ACTIVE','2020-08-30 21:40:54','2020-11-04 18:24:36',5.00,'2020-11-04 15:23:36',1,1,211,220),(489,'2020-05-31 11:57:20','ACTIVE','2020-08-30 11:57:20','2020-11-01 17:23:14',5.00,'2020-11-01 13:23:14',1,1,211,438),(490,'2020-05-31 11:58:36','ACTIVE','2020-08-30 11:58:36','2020-11-01 21:23:14',5.00,'2020-11-01 17:23:14',1,1,211,438);
/*!40000 ALTER TABLE `screening` ENABLE KEYS */;
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
