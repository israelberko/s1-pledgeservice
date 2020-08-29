drop database `party`;
CREATE DATABASE `party` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE  `party`;
CREATE TABLE `party.parties` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `state` varchar(100) DEFAULT NULL,
  `rsvp_count` int(11) DEFAULT NULL,
  `account_balance` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;
CREATE TABLE `party.parties_outbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(100) DEFAULT NULL,
  `payload` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;
INSERT INTO party.parties (state,rsvp_count,account_balance) VALUES 
('suggested',NULL,NULL)
;

CREATE DATABASE `invitee` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `invitee`;
CREATE TABLE `invitee.invitees` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `party_id` bigint(20) NOT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;
CREATE TABLE `invitees_outbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(100) DEFAULT NULL,
  `payload` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;

CREATE DATABASE `promotion` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `promotion`;
CREATE TABLE `promotion.promotions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `party_id` bigint(20) NOT NULL,
  `rsvp_num` int(11) DEFAULT NULL,
  `account_balance` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;
CREATE TABLE `promotion.promotions_outbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(100) DEFAULT NULL,
  `payload` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;

CREATE DATABASE `management` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `management`;
CREATE TABLE `management.management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `party_id` bigint(20) NOT NULL,
  `invitee_id` bigint(20) DEFAULT NULL,
  `rsvp` tinyint DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;
CREATE TABLE `management.management_outbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(100) DEFAULT NULL,
  `payload` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;

CREATE DATABASE `account` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `account`;
CREATE TABLE `account.accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `party_id` bigint(20) NOT NULL,
  `amount` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;
CREATE TABLE `account.accounts_outbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(100) DEFAULT NULL,
  `payload` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
;


