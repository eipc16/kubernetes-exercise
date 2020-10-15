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
-- Table structure for table `reservation_x_seat`
--

DROP TABLE IF EXISTS `reservation_x_seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_x_seat` (
  `id` bigint NOT NULL,
  `created_at` datetime NOT NULL,
  `object_state` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `updated_by_id` bigint DEFAULT NULL,
  `reservation_id` bigint DEFAULT NULL,
  `seat_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbof1yrqicv14iu5nuyjqavxrg` (`created_by_id`),
  KEY `FKlmicq4dxwjl917furobv3b93o` (`updated_by_id`),
  KEY `FKs0gtk86qe0elcelobc8ml3mn` (`reservation_id`),
  KEY `FKrb9dypvt2qsqoc6skgomhv859` (`seat_id`),
  CONSTRAINT `FKbof1yrqicv14iu5nuyjqavxrg` FOREIGN KEY (`created_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKlmicq4dxwjl917furobv3b93o` FOREIGN KEY (`updated_by_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKrb9dypvt2qsqoc6skgomhv859` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`),
  CONSTRAINT `FKs0gtk86qe0elcelobc8ml3mn` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_x_seat`
--

LOCK TABLES `reservation_x_seat` WRITE;
/*!40000 ALTER TABLE `reservation_x_seat` DISABLE KEYS */;
INSERT INTO `reservation_x_seat` VALUES (379,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,221),(380,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,222),(381,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,223),(382,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,224),(383,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,233),(384,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,234),(385,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,235),(386,'2020-05-31 11:54:52','ACTIVE','2020-05-31 11:54:52',1,1,378,236);
/*!40000 ALTER TABLE `reservation_x_seat` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-15 19:11:25
