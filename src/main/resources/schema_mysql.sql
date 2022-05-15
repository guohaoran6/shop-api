CREATE DATABASE IF NOT EXISTS shop default charset utf8;

USE shop;


CREATE TABLE `user` (
    `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'user id',
    `user_name` varchar(11) NOT NULL DEFAULT '' COMMENT 'user name',
    `password_md5` varchar(32) NOT NULL DEFAULT '' COMMENT 'password with MD5',
    `is_admin` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: non-admin user 1: admin user',
    `delete_flg` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: available 1: deleted',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `user` (`user_id`, `user_name`, `password_md5`, `is_admin`)
VALUES
(1,'admin','e10adc3949ba59abbe56e057f20f883e',1),
(2,'user','e10adc3949ba59abbe56e057f20f883e',0);


CREATE TABLE `user_token` (
    `user_id` int(11) NOT NULL COMMENT 'user id',
    `token` varchar(32) NOT NULL COMMENT 'token value',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    `expire_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'token expire time',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uq_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `product` (
    `product_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'product id',
    `name` varchar(200) NOT NULL DEFAULT '' COMMENT 'product name',
    `desc` varchar(200) NOT NULL DEFAULT '' COMMENT 'product description',
    `img_url` varchar(200) NOT NULL DEFAULT '/admin/dist/img/no-img.png' COMMENT 'image url',
    `price` decimal(10,2) NOT NULL DEFAULT '1' COMMENT 'product price',
    `stock_number` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'product stock number',
    `tag` varchar(20) DEFAULT NULL COMMENT 'product tag',
    `delete_flg` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: available 1: deleted',
    `create_user` int(11) NOT NULL DEFAULT '0' COMMENT 'create user',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_user` int(11) NOT NULL DEFAULT '0' COMMENT 'update user',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    `version` int(11) NOT NULL DEFAULT '1' COMMENT 'update version',
    PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `product` (`product_id`, `name`, `desc`, `img_url`, `price`, `stock_number`, `tag`, `create_user`, `update_user`, `version`)
VALUES
(1,'iPhone11','eleven','/img/11.jpg',11.1,110,'smart-phone',1,1,1),
(2,'iPhone12','twelve','/img/12.jpgg',12.2,120,'smart-phone',1,1,2),
(3,'iPhone13','threeteen','/img/13.jpg',13.3,130,'smart-phone',1,1,3),
(4,'iPhone14','fourteen','/img/14.jpg',14.4,140,'smart-phone',1,1,4),
(5,'iPhone15','fifteen','/img/15.jpg',15.5,150,'smart-phone',1,1,5);


CREATE TABLE `shopping_cart` (
    `cart_item_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'shopping cart id',
    `user_id` int(11) NOT NULL COMMENT 'user id',
    `product_id` int(11) NOT NULL DEFAULT '0' COMMENT 'product id',
    `product_count` int(11) NOT NULL DEFAULT '1' COMMENT 'product count',
    `delete_flg` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: available 1: deleted',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`cart_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order` (
    `order_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'order id',
    `order_no` varchar(20) NOT NULL DEFAULT '' COMMENT 'order number',
    `user_id` int(11) NOT NULL DEFAULT '0' COMMENT 'user id',
    `total_price` decimal(10,2) NOT NULL DEFAULT '1' COMMENT 'total price',
    `pay_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'payment status: 0.not payment,1.successful,-1:failed',
    `pay_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
    `pay_time` datetime DEFAULT NULL COMMENT 'payment time',
    `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
    `delete_flg` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: available 1: deleted',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_item` (
    `order_item_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'oride item id',
    `order_id` int(11) NOT NULL DEFAULT '0' COMMENT 'order id',
    `product_id` int(11) NOT NULL DEFAULT '0' COMMENT 'product id',
    `total_price` decimal(10,2) NOT NULL DEFAULT '1' COMMENT 'total price',
    `product_count` int(11) NOT NULL DEFAULT '1' COMMENT 'product count',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `event` (
    `event_id` int(11) NOT NULL AUTO_INCREMENT,
    `product_id` int(11) NOT NULL,
    `event_message` JSON NOT NULL,
    `updated_by` int(11) NOT NULL,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;