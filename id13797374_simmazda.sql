-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 24, 2020 at 10:04 AM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id13797374_simmazda`
--

-- --------------------------------------------------------

--
-- Table structure for table `cardata`
--

CREATE TABLE `cardata` (
  `noplat` varchar(50) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `carmodel` varchar(30) NOT NULL,
  `yearmodel` varchar(30) NOT NULL,
  `color` varchar(50) DEFAULT NULL,
  `norangka` varchar(50) DEFAULT NULL,
  `nomesin` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cardata`
--

INSERT INTO `cardata` (`noplat`, `username`, `carmodel`, `yearmodel`, `color`, `norangka`, `nomesin`) VALUES
('A 2124 KO', 'admincs', 'Mazda 2', '2018', 'Red Crystal Metallic', 'FDS3219SA0', '8219D2K'),
('B 1022 SS', 'admincs', 'Mazda 3', '2019', 'Red Soul Crystal Metallic', 'PW021S9A21', 'LSAP210G'),
('B 20 KM', 'admincs', 'Mazda 6 Sedan', '2020', 'Red Soul Crystal Metallic', 'OSQ20SA215', 'A9210DA2'),
('B 210 KK', 'admincs', 'Mazda 6 Sedan', '2017', 'Jet Black Mica', 'PSA2KASLW9', 'ISAO021'),
('F 210 AG', 'admincs', 'Mazda 6 Sedan', '2018', 'Jet Black Mica', 'KSA2910S81', 'OWQ2018');

-- --------------------------------------------------------

--
-- Table structure for table `cstdata`
--

CREATE TABLE `cstdata` (
  `idcst` int(6) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `firstname` varchar(30) NOT NULL,
  `lastname` varchar(30) NOT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `notelp` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cstdata`
--

INSERT INTO `cstdata` (`idcst`, `username`, `firstname`, `lastname`, `alamat`, `notelp`) VALUES
(5, 'admincs', 'Admin', 'Customer', 'Jl. Kancil no 20', '081242138582');

-- --------------------------------------------------------

--
-- Table structure for table `servicereservation`
--

CREATE TABLE `servicereservation` (
  `idreservasi` int(11) NOT NULL,
  `noplat` varchar(50) CHARACTER SET latin1 NOT NULL,
  `username` varchar(50) CHARACTER SET latin1 NOT NULL,
  `namabengkel` varchar(50) CHARACTER SET latin1 NOT NULL,
  `jenisservis` varchar(50) CHARACTER SET latin1 NOT NULL,
  `sesiservis` varchar(50) CHARACTER SET latin1 NOT NULL,
  `tanggalservis` date NOT NULL,
  `catatan` text CHARACTER SET latin1 NOT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `servicereservation`
--

INSERT INTO `servicereservation` (`idreservasi`, `noplat`, `username`, `namabengkel`, `jenisservis`, `sesiservis`, `tanggalservis`, `catatan`, `status`) VALUES
(6, 'A 2124 KO', 'admincs', 'Mazda Jakarta Timur', 'Fast Track', 'Siang (13.00 - 15.00)', '2020-05-25', 'ganti oli dan air radiator', 'on progress'),
(7, 'B 20 KM', 'admincs', 'Mazda MT Haryono', 'Servis Berkala', 'Siang (13.00 - 15.00)', '2020-05-29', 'Servis Berkala 20.000 KM', 'pending'),
(9, 'B 1022 SS', 'admincs', 'Mazda Jakarta Barat', 'Fast Track', 'Siang (13.00 - 15.00)', '2020-05-26', 'Ganti Oli', 'pending'),
(10, 'B 20 KM', 'admincs', 'Mazda Jakarta Timur', 'Body and paint', 'Pagi (08.00 - 11.00)', '2020-05-25', 'Cat ulang seluruh mobil', 'complete'),
(11, 'B 210 KK', 'admincs', 'Mazda Jakarta Timur', 'Servis Berkala', 'Pagi (08.00 - 11.00)', '2020-05-26', 'Servis berkala 80.000 km', 'rejected');

-- --------------------------------------------------------

--
-- Table structure for table `userlogin`
--

CREATE TABLE `userlogin` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userlogin`
--

INSERT INTO `userlogin` (`username`, `password`, `email`, `status`) VALUES
('admincs', 'admincs', 'admincs@mazda.com', 'customer'),
('adminst', 'adminst', 'adminst@mazda.com', 'staff'),
('mfyadminst', 'mfyadminst', 'adminmfy@mfy.com', 'staff');

-- --------------------------------------------------------

--
-- Table structure for table `workshopdata`
--

CREATE TABLE `workshopdata` (
  `idbengkel` int(6) NOT NULL,
  `namabengkel` varchar(50) DEFAULT NULL,
  `passwordbengkel` varchar(50) NOT NULL,
  `alamatbengkel` varchar(50) DEFAULT NULL,
  `notelpbengkel` varchar(50) DEFAULT NULL,
  `langbengkel` varchar(50) DEFAULT NULL,
  `longbengkel` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `workshopdata`
--

INSERT INTO `workshopdata` (`idbengkel`, `namabengkel`, `passwordbengkel`, `alamatbengkel`, `notelpbengkel`, `langbengkel`, `longbengkel`) VALUES
(1, 'Mazda Jakarta Timur', 'mazda01', 'JL. JATINEGARA BARAT NO.140, JAKARTA TIMUR', '02185901155', '-6.221929', '106.865560'),
(2, 'Mazda MT Haryono', 'mazda02', 'JL. MT HARYONO KAV.30  JAKARTA SELATAN', '02129380222', '-6.242607', '106.858162'),
(3, 'Mazda Jakarta Barat', 'mazda03', 'JL. LETJEN S. PARMAN KAV. N-1, JAKARTA BARAT', ' 0215309888', '-6.191309', '106.796648');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cardata`
--
ALTER TABLE `cardata`
  ADD PRIMARY KEY (`noplat`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `cstdata`
--
ALTER TABLE `cstdata`
  ADD PRIMARY KEY (`idcst`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `servicereservation`
--
ALTER TABLE `servicereservation`
  ADD PRIMARY KEY (`idreservasi`),
  ADD KEY `noplat` (`noplat`),
  ADD KEY `namabengkel` (`namabengkel`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `userlogin`
--
ALTER TABLE `userlogin`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `workshopdata`
--
ALTER TABLE `workshopdata`
  ADD PRIMARY KEY (`idbengkel`),
  ADD UNIQUE KEY `namabengkel` (`namabengkel`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cstdata`
--
ALTER TABLE `cstdata`
  MODIFY `idcst` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `servicereservation`
--
ALTER TABLE `servicereservation`
  MODIFY `idreservasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `workshopdata`
--
ALTER TABLE `workshopdata`
  MODIFY `idbengkel` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cardata`
--
ALTER TABLE `cardata`
  ADD CONSTRAINT `cardata_ibfk_1` FOREIGN KEY (`username`) REFERENCES `userlogin` (`username`);

--
-- Constraints for table `cstdata`
--
ALTER TABLE `cstdata`
  ADD CONSTRAINT `cstdata_ibfk_1` FOREIGN KEY (`username`) REFERENCES `userlogin` (`username`);

--
-- Constraints for table `servicereservation`
--
ALTER TABLE `servicereservation`
  ADD CONSTRAINT `servicereservation_ibfk_1` FOREIGN KEY (`noplat`) REFERENCES `cardata` (`noplat`),
  ADD CONSTRAINT `servicereservation_ibfk_2` FOREIGN KEY (`namabengkel`) REFERENCES `workshopdata` (`namabengkel`),
  ADD CONSTRAINT `servicereservation_ibfk_3` FOREIGN KEY (`username`) REFERENCES `userlogin` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
