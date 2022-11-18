CREATE DATABASE webui /*!40100 DEFAULT CHARACTER SET utf8 */;
use webui;
CREATE TABLE page (
id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
pageName varchar(500) NOT NULL COMMENT '页面名称',
addTime varchar(500) NOT NULL COMMENT '创建时间',
updateTime varchar(500) DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE element (
id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
elementName varchar(500) NOT NULL COMMENT '元素名称',
iframe varchar(500) DEFAULT NULL COMMENT 'iframe',
locateType varchar(500) NOT NULL COMMENT '定位方式',
keyword varchar(500) NOT NULL COMMENT '定位关键字',
pageId int(11) NOT NULL COMMENT '所属页面id',
addTime varchar(500) NOT NULL COMMENT '创建时间',
updateTime varchar(500) DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE step (
id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
stepName varchar(500) NOT NULL COMMENT '步骤名称',
elementId int(11) NOT NULL COMMENT '关联的元素id',
action varchar(500) NOT NULL COMMENT '元素的操作',
actionValue varchar(500) DEFAULT NULL COMMENT '操作需要给定的值',
function varchar(500) DEFAULT NULL COMMENT '执行的外部方法',
status int(11) NOT NULL DEFAULT '1' COMMENT '是否禁用(2:禁用;1:启用)',
verifyAction varchar(500) DEFAULT NULL COMMENT '校验操作',
verifyValue varchar(500) DEFAULT NULL COMMENT '校验值',
addTime varchar(500) NOT NULL COMMENT '创建时间',
updateTime varchar(500) DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `uicase` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `caseName` varchar(500) NOT NULL COMMENT '用例名称',
  `url` varchar(500) NOT NULL COMMENT 'url地址',
  `pageId` int(11) NOT NULL COMMENT '所属业务',
  `pageType` varchar(500) NOT NULL COMMENT '页面类型',
  `phoneType` varchar(500) DEFAULT NULL COMMENT '手机类型',
  `browserType` varchar(500) NOT NULL COMMENT '浏览器类型',
  `stepIds` varchar(500) DEFAULT NULL COMMENT '步骤id集',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '用例执行状态(2:执行中;1:空闲)',
  `canUse` int(11) NOT NULL DEFAULT '1' COMMENT '用例是否启用(2:停用;1:启用)',
  `verifyAction` varchar(500) DEFAULT NULL COMMENT '校验操作',
  `verifyValue` varchar(500) DEFAULT NULL COMMENT '校验值',
  `addTime` varchar(500) NOT NULL COMMENT '创建时间',
  `updateTime` varchar(500) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `caseresult` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `caseId` int(11) NOT NULL COMMENT '用例id',
  `failStepId` int(11) DEFAULT NULL COMMENT '失败步骤id',
  `failStepImage` longblob COMMENT '失败步骤图片',
  `failReason` text COMMENT '失败原因',
  `status` int(11) NOT NULL COMMENT '是否成功(0:失败;1:成功)',
  `useTime` varchar(500) DEFAULT NULL COMMENT '用例执行耗时',
  `addTime` varchar(500) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE uiscene (
id int(11) NOT NULL COMMENT '主键id',
sceneName varchar(500) NOT NULL COMMENT '场景名称',
caseIds varchar(500) NOT NULL COMMENT '场景下的用例集',
status int(11) NOT NULL DEFAULT '1' COMMENT '场景的执行状态(2:执行中;1:空闲)',
addTime varchar(500) NOT NULL COMMENT '创建时间',
updateTime varchar(500) DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE cron (
id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
cronName varchar(500) NOT NULL COMMENT '任务名称',
status int(11) NOT NULL DEFAULT '1' COMMENT '是否启用(2:禁用;1:启用)',
execTime varchar(500) NOT NULL COMMENT '执行时间',
args varchar(500) NOT NULL COMMENT '执行时的参数',
addTime varchar(500) NOT NULL COMMENT '创建时间',
updateTime varchar(500) DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `inittime` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;