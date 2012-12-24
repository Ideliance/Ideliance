SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;


DROP TABLE IF EXISTS `ideliance_dictionary`;
DROP TABLE IF EXISTS `ideliance_relation`;
DROP TABLE IF EXISTS `ideliance_subject`;
DROP TABLE IF EXISTS `ideliance_triplet`;


CREATE TABLE IF NOT EXISTS `ideliance_dictionary` (
  `fk` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `value` text NOT NULL,
  KEY `fk` (`fk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ideliance_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inverse` int(11) DEFAULT NULL,
  `datecreation` bigint(20) NOT NULL,
  `datemodification` bigint(20) NOT NULL,
  `authorcreation` varchar(200) NOT NULL,
  `authormodification` varchar(200) NOT NULL,
  `issystem` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `inverserelation` (`inverse`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ideliance_subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datecreation` bigint(20) NOT NULL,
  `datemodification` bigint(20) NOT NULL,
  `authorcreation` varchar(200) NOT NULL,
  `authormodification` varchar(200) NOT NULL,
  `issystem` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ideliance_triplet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` int(11) NOT NULL,
  `relation` int(11) NOT NULL,
  `complement` int(11) NOT NULL,
  `datecreation` bigint(20) NOT NULL,
  `datemodification` bigint(20) NOT NULL,
  `authorcreation` varchar(200) NOT NULL,
  `authormodification` varchar(200) NOT NULL,
  `typesubject` tinyint(1) NOT NULL,
  `typecomplement` tinyint(1) NOT NULL,
  `issystem` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
