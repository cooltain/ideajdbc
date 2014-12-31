/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.21 : Database - ideajdbc
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `t_beanforupdate` */

CREATE TABLE `t_beanforupdate` (
  `C_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_PRICE` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `t_beanforupdate` */

insert  into `t_beanforupdate`(`C_ID`,`C_NAME`,`C_PRICE`) values ('1','C3',2.56);
insert  into `t_beanforupdate`(`C_ID`,`C_NAME`,`C_PRICE`) values ('2','A1',3.14);
insert  into `t_beanforupdate`(`C_ID`,`C_NAME`,`C_PRICE`) values ('2','A1',2.56);
insert  into `t_beanforupdate`(`C_ID`,`C_NAME`,`C_PRICE`) values ('2','A1',2.56);
insert  into `t_beanforupdate`(`C_ID`,`C_NAME`,`C_PRICE`) values ('2','A1',2.56);
insert  into `t_beanforupdate`(`C_ID`,`C_NAME`,`C_PRICE`) values ('2','A1',2.56);

/*Table structure for table `t_order` */

CREATE TABLE `t_order` (
  `C_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `C_USERID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `t_order` */

insert  into `t_order`(`C_ID`,`C_CODE`,`C_USERID`) values ('1','O_0001','1');
insert  into `t_order`(`C_ID`,`C_CODE`,`C_USERID`) values ('2','O_0002','1');
insert  into `t_order`(`C_ID`,`C_CODE`,`C_USERID`) values ('3','O_0003','2');

/*Table structure for table `t_user` */

CREATE TABLE `t_user` (
  `C_ID` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `NAME` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EMAIL` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `t_user` */

insert  into `t_user`(`C_ID`,`NAME`,`EMAIL`,`AGE`) values ('1','Chinakite','chinakite.zhang@gmail.com',33);
insert  into `t_user`(`C_ID`,`NAME`,`EMAIL`,`AGE`) values ('2','张中华','zhangzh@ideamoment.com',23);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
