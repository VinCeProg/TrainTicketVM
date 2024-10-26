-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 26, 2024 at 10:12 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `trainticketvm`
--

-- --------------------------------------------------------

--
-- Table structure for table `stations`
--

CREATE TABLE `stations` (
  `stationID` int(3) NOT NULL,
  `stationName` varchar(32) NOT NULL,
  `onRoute_Commuter` tinyint(1) DEFAULT NULL,
  `onRoute_CommuterX` tinyint(1) DEFAULT NULL,
  `onRoute_Limited` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stations`
--

INSERT INTO `stations` (`stationID`, `stationName`, `onRoute_Commuter`, `onRoute_CommuterX`, `onRoute_Limited`) VALUES
(1, 'Calamba', 1, 1, 0),
(2, 'Banlic', 1, 0, 0),
(3, 'Cabuyao', 1, 1, 0),
(4, 'Santa Rosa', 1, 1, 0),
(5, 'Binan', 1, 0, 0),
(6, 'Pacita', 1, 0, 0),
(7, 'San Pedro', 1, 0, 0),
(8, 'Muntinlupa', 1, 0, 0),
(9, 'Alabang', 1, 1, 1),
(10, 'Sucat', 1, 1, 0),
(11, 'Bicutan', 1, 0, 0),
(12, 'FTI', 1, 1, 1),
(13, 'Nichols', 1, 0, 0),
(14, 'EDSA', 1, 0, 0),
(15, 'Buendia', 1, 1, 1),
(16, 'Paco', 1, 0, 0),
(17, 'Santa Mesa', 1, 1, 0),
(18, 'Espana', 1, 0, 0),
(19, 'Blumentritt', 1, 1, 0),
(20, 'Tutuban', 1, 1, 0),
(21, 'Solis', 1, 0, 1),
(22, 'Caloocan', 1, 1, 0),
(23, 'Valenzuela', 1, 1, 0),
(24, 'Valenzuela Polo', 1, 0, 0),
(25, 'Malabon', 1, 0, 0),
(26, 'Meycauayan', 1, 0, 0),
(27, 'Marilao', 1, 1, 0),
(28, 'Tabing Ilog', 1, 0, 0),
(29, 'Bocaue', 1, 1, 0),
(30, 'Balagtas', 1, 1, 0),
(31, 'Tuktukan', 1, 0, 0),
(32, 'Guiguinto', 1, 1, 0),
(33, 'Malolos South', 1, 0, 0),
(34, 'Malolos', 1, 1, 0),
(35, 'Calumpit', 1, 0, 0),
(36, 'Apalit', 1, 0, 0),
(37, 'San Fernando', 1, 1, 0),
(38, 'Angeles', 1, 0, 0),
(39, 'Clark', 1, 1, 0),
(40, 'Clark International Airport', 1, 1, 0),
(41, 'New Clark City', 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `ticketID` int(8) NOT NULL,
  `ticketType` varchar(50) DEFAULT 'TICKET',
  `issueDate` date DEFAULT '2024-10-20',
  `expiryDate` date DEFAULT '2024-10-20',
  `departureID` int(3) DEFAULT 0,
  `destinationID` int(3) DEFAULT 0,
  `amount` double DEFAULT 0,
  `paymentMethod` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`ticketID`, `ticketType`, `issueDate`, `expiryDate`, `departureID`, `destinationID`, `amount`, `paymentMethod`) VALUES
(12340001, 'LIMITED', '2024-10-25', '2024-10-28', 17, 9, NULL, NULL),
(12340002, 'COMMUTER', '2024-10-25', '2024-10-28', 17, 9, NULL, NULL),
(12340003, 'COMMUTERX', '2024-10-25', '2024-10-28', 17, 9, NULL, NULL),
(12340004, 'COMMUTER', '2024-10-25', '2024-10-28', 17, 9, NULL, NULL),
(12340005, 'COMMUTER', '2024-10-25', '2024-10-28', 17, 16, NULL, NULL),
(12340006, 'COMMUTER', '2024-10-25', '2024-10-28', 17, 1, NULL, NULL),
(12340007, 'COMMUTER', '2024-10-25', '2024-10-28', 17, 6, NULL, NULL),
(12340008, 'COMMUTER', '2024-10-25', '2024-10-28', 17, 7, NULL, NULL),
(12340009, 'LIMITED', '2024-10-25', '2024-10-28', 17, 12, NULL, NULL),
(12340010, 'LIMITED', '2024-10-25', '2024-10-28', 1, 41, NULL, NULL),
(12340011, 'COMMUTER', '2024-10-25', '2024-10-26', 1, 17, NULL, NULL),
(12340012, 'LIMITED', '2024-10-25', '2024-10-26', 1, 41, 85, NULL),
(12340013, 'LIMITED', '2024-10-25', '2024-10-26', 1, 21, 50, 'MOBILE'),
(12340014, 'COMMUTERX', '2024-10-25', '2024-10-26', 1, 3, 18, 'CASH'),
(12340015, 'COMMUTER', '2024-10-25', '2024-10-26', 1, 38, 52, 'CARD'),
(12340016, 'COMMUTER', '2024-10-25', '2024-10-26', 1, 40, 54, 'CARD'),
(12340017, 'LIMITED', '2024-10-25', '2024-10-26', 9, 41, 71, 'CARD'),
(12340018, 'commuter', '2024-10-25', '2024-10-26', 20, 34, 29, 'CARD'),
(12340019, 'commuter', '2024-10-25', '2024-10-26', 17, 34, 32, 'MOBILE'),
(12340020, 'limited', '2024-10-25', '2024-10-26', 9, 1, 29, 'CASH'),
(12340021, 'LIMITED', '2024-10-25', '2024-10-26', 1, 41, 85, 'MOBILE'),
(12340022, 'COMMUTER', '2024-10-26', '2024-10-27', 1, 2, 16, 'CARD'),
(12340023, 'LIMITED', '2024-10-26', '2024-10-27', 12, 9, 20.25, 'CASH'),
(12340024, 'COMMUTER', '2024-10-26', '2024-10-27', 17, 12, 20, 'CASH'),
(12340025, 'LIMITED', '2024-10-26', '2024-10-27', 9, 41, 71, 'MOBILE'),
(12340026, 'LIMITED', '2024-10-26', '2024-10-27', 1, 41, 85, 'CASH');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stations`
--
ALTER TABLE `stations`
  ADD PRIMARY KEY (`stationID`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`ticketID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tickets`
--
ALTER TABLE `tickets`
  MODIFY `ticketID` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12340027;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
