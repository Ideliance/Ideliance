-- phpMyAdmin SQL Dump
-- version 3.4.9
-- http://www.phpmyadmin.net
--
-- Client: 127.0.0.1
-- Généré le : Jeu 05 Avril 2012 à 12:56
-- Version du serveur: 5.5.20
-- Version de PHP: 5.3.9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `ideliance`
--

--
-- Contenu de la table `ideliance_dictionary`
--

INSERT INTO `ideliance_dictionary` (`fk`, `type`, `lang`, `value`) VALUES
(1, 1, 'en', 'dev'),
(2, 1, 'en', 'dev'),
(1, 3, 'en', 'PASSWORD'),
(2, 3, 'en', 'LEVEL'),
(3, 1, 'en', 'USER_LEVEL'),
(4, 1, 'en', 'ADMINISTRATOR'),
(5, 1, 'en', 'CATEGORY'),
(3, 3, 'en', 'ISA'),
(4, 3, 'en', 'IN CATEGORY'),
(5, 3, 'en', 'CATEGORY OF');

--
-- Contenu de la table `ideliance_relation`
--

INSERT INTO `ideliance_relation` (`id`, `inverse`, `datecreation`, `datemodification`, `authorcreation`, `authormodification`, `issystem`) VALUES
(1, 0, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(2, 0, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(3, 0, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(4, 5, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(5, 4, 0, 0, 'SYSTEM', 'SYSTEM', 1);

--
-- Contenu de la table `ideliance_subject`
--

INSERT INTO `ideliance_subject` (`id`, `datecreation`, `datemodification`, `authorcreation`, `authormodification`, `issystem`) VALUES
(1, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(2, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(3, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(4, 0, 0, 'SYSTEM', 'SYSTEM', 1),
(5, 0, 0, 'SYSTEM', 'SYSTEM', 1);

--
-- Contenu de la table `ideliance_triplet`
--

INSERT INTO `ideliance_triplet` (`id`, `subject`, `relation`, `complement`, `datecreation`, `datemodification`, `authorcreation`, `authormodification`, `typesubject`, `typecomplement`, `issystem`) VALUES
(1, 1, 1, 2, 0, 0, 'SYSTEM', 'SYSTEM', 0, 0, 1),
(2, 1, 2, 4, 0, 0, 'SYSTEM', 'SYSTEM', 0, 0, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
