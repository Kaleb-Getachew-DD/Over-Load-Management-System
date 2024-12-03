-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 05, 2024 at 06:15 PM
-- Server version: 5.7.24
-- PHP Version: 8.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `java`
--

-- --------------------------------------------------------

--
-- Table structure for table `course_db`
--

CREATE TABLE `course_db` (
  `id` int(10) NOT NULL,
  `course_id` varchar(64) NOT NULL,
  `course_name` varchar(64) NOT NULL,
  `CreditHour` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `course_db`
--

INSERT INTO `course_db` (`id`, `course_id`, `course_name`, `CreditHour`) VALUES
(1, 'Co2055', 'Computer Programming', '5'),
(4, 'Co2025', 'Advanced Programming', '5'),
(5, 'Co2030', 'Algorithm Analysis', '5'),
(6, 'Co2070', 'Computer Organization', '3'),
(7, 'Co2020', 'JAVA Programming', '5');

-- --------------------------------------------------------

--
-- Table structure for table `schedule_db`
--

CREATE TABLE `schedule_db` (
  `id` int(11) NOT NULL,
  `day` varchar(30) NOT NULL,
  `time` varchar(30) NOT NULL,
  `course` varchar(30) NOT NULL,
  `instructor` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `schedule_db`
--

INSERT INTO `schedule_db` (`id`, `day`, `time`, `course`, `instructor`) VALUES
(17, 'Monday', '7:45-9:45', 'Advanced Programming', 'Kaleb Getachew'),
(21, 'Wednesday', '9:45-10:45', 'Algorithm Analysis', 'Kaleb Getachew'),
(23, 'Tuesday', '7:45-9:45', 'JAVA Programming', 'Hirut');

-- --------------------------------------------------------

--
-- Table structure for table `user_db`
--

CREATE TABLE `user_db` (
  `id` int(10) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `full_name` varchar(64) NOT NULL,
  `department` varchar(64) NOT NULL,
  `gender` varchar(64) NOT NULL,
  `salary` int(64) NOT NULL,
  `position` varchar(64) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `credithour` int(100) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_db`
--

INSERT INTO `user_db` (`id`, `user_id`, `full_name`, `department`, `gender`, `salary`, `position`, `username`, `password`, `credithour`) VALUES
(3, 'DDU1402313', 'Kaleb', 'Computer Science', 'Male', 10180, 'Teacher', 'kaleb', 'kaleb', 3),
(4, 'DDU1', 'administrator', 'CS', 'Male', 1000, 'Coordinator', 'admin', 'admin', 0),
(7, 'DDU3', 'Teshome', 'Computer Science', 'Male', 40000, 'DepHead', 'head', 'head', 0),
(10, 'DDU2', 'Hirut', 'Computer Science', 'Female', 9000, 'Teacher', 'hirut', 'hirut', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `course_db`
--
ALTER TABLE `course_db`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `schedule_db`
--
ALTER TABLE `schedule_db`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_db`
--
ALTER TABLE `user_db`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `course_db`
--
ALTER TABLE `course_db`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `schedule_db`
--
ALTER TABLE `schedule_db`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `user_db`
--
ALTER TABLE `user_db`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
