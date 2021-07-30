# API SERVER 테스트 실행 방법
1. C:\Windows\System32\drivers\etc\hosts 내용 추가: ```122.49.74.240 PBX_VIP```
   * SKT향 테스트: ```122.49.74.101 PBX_VIP```
1. mvn jooq-codegen:generate
1. build (compile)
1. ServerApplication.main() 실행

# FRONT SERVER 테스트 실행 방법
1. ./gradlew :skt-front:bootRun

# 테스트 계정 (company id / user id / password / extension)
* LGU향
  * ```premium / user620 / user620!@ / 620```
* SKT향
  * ```skdev / admin / admin12!@! / ```
  * ```skdev / user1 / user12!@! / 1000```
  * ```skdev / user2 / user12!@! / 2000```

# 테스트 배포 방법
1. git pull
1. (혹시 JAVA_HOME 세팅이 안되어 있다면..) JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre/
1. ./gradlew clean (꼭 필요하지 않음)
1. ./gradlew :api-server:bootWar :skt-front:bootWar
1. nohup java -jar ./api-server/build/libs/api-server-1.0-SNAPSHOT.war --server.port=8080 1> api.log 2>&1 &
1. nohup java -jar ./skt-front/build/libs/skt-front-1.0-SNAPSHOT.war --server.port=21003 1> front.log 2>&1 &

# 톰캣 배포 방법
1. ./gradlew :api-server:bootWar :skt-front:bootWar -Pprofile={provide} -DJDBC_USERNAME='{username}' -DJDBC_PASSWORD='{password}' -DJDBC_HOST='{host}';
1. gradlew :api-server:bootWar :skt-front:bootWar -Pprofile={provide} -DJDBC_USERNAME='{username}' -DJDBC_PASSWORD={password} -DJDBC_HOST={host}
```windows 환경에서 실행시```
1. 생성된 war 파일을 톰캣 webapps에 복사
```
- ./api-server/build/libs/api-server-1.0-SNAPSHOT.war 
- ./skt-front/build/libs/skt-front-1.0-SNAPSHOT.war
```

# 변경된 스키마 이력

* 2020-04-22 라우팅신청관리
```
ALTER TABLE route_application MODIFY result varchar(10) DEFAULT 'NONE' NOT NULL comment 'NONE: 미처리, ACCEPT: 승인, REJECT: 반려';
update route_application set result = 'NONE' where result not in ('NONE', 'ACCEPT', 'REJECT');
ALTER TABLE route_application MODIFY result enum('NONE', 'ACCEPT', 'REJECT') DEFAULT 'NONE' NOT NULL comment 'NONE: 미처리, ACCEPT: 승인, REJECT: 반려';

ALTER TABLE route_application MODIFY type varchar(10) DEFAULT 'BLACKLIST' NOT NULL;
update route_application set type = 'BLACKLIST' where type not in ('VIP', 'BLACKLIST');
ALTER TABLE route_application MODIFY type enum('VIP', 'BLACKLIST') DEFAULT 'BLACKLIST' NOT NULL;

ALTER TABLE route_application MODIFY rst_userid varchar(50) null;
```

* 2020-04-22 queue.number 유니크 체크
```
ALTER TABLE queue_name
	add constraint queue_name_pk
		unique (number);
```

* 2020-04-22 전광판 설정 테이블 생성
```
create table screen_config
(
	seq int auto_increment,
	name varchar(100) NOT NULL comment '전광판 이름',
	look_and_feel int NOT NULL comment '사전 정의된 디자인 번호',
	expression_type enum('INTEGRATION', 'BY_HUNT', 'BY_SERVICE') NOT NULL comment '사전 정의된 데이터 표현 형식',
	show_sliding_text boolean DEFAULT false NOT NULL comment '슬라이딩 문구 표현 여부',
	sliding_text varchar(200) null comment '슬라이딩 문구',
	company_id varchar(50) NOT NULL,
	constraint screen_config_pk
		primary key (seq)
)
comment '전광판 설정';
```

* 2020-04-23 컨텍스트 유니크 키 추가
```
ALTER TABLE context_info
	add constraint context_info_pk
		unique (context);

ALTER TABLE webvoice_info
	add constraint webvoice_info_pk
		unique (context);
```

* 2020-04-23 컨텍스트 NOT NULL 지정
```
delete from webvoice_info where context = null or context = '';
ALTER TABLE webvoice_info MODIFY context varchar(50) DEFAULT '' NOT NULL;

delete from webvoice_items where context = null or context = '';
ALTER TABLE webvoice_items MODIFY context varchar(50) DEFAULT '' NOT NULL;
```

* 2020-05-10 person_link 테이블 생성
```
CREATE TABLE person_link (
    seq int auto_increment primary key,
    name varchar(100) NOT NULL comment '링크 이름',
    reference varchar(1000) NOT NULL comment '링크',
	person_id varchar(100) NOT NULL comment '사용자 ID, refer: person_list.id',
	company_id varchar(50) NOT NULL comment 'refer: company_info.company_id'
) comment '사용자별 외부 링크';
```

* 2020-05-10 todo_list 컬럼 타입 변경
```
ALTER TABLE todo_list MODIFY todo_kind ENUM('TALK','CALLBACK','RESERVE','EMAIL','PREVIEW','TRANSFER') NOT NULL comment '일감종류 PREVIEW,TALK(상담톡),EMAIL(이메일),CALLBACK(콜백),RESERVE(예약콜)';
ALTER TABLE todo_list MODIFY todo_status ENUM('ING','HOLD','DELETE','DONE') NOT NULL comment '상태 ING(진행중), HOLD(보류중), DELETE(삭제됨), DONE(처리됨)';
```

* 2020-05-23 voc_group use 컬럼 추가
```
ALTER TABLE voc_group add `use` boolean DEFAULT false NOT NULL comment '해당 voc 사용 여부' AFTER research_id;
```

* 2020-07-26 일정관리
```
DROP TABLE IF EXISTS `calendar`;

CREATE TABLE `eicn`.`user_schedule`
(
    `seq` int AUTO_INCREMENT PRIMARY KEY,

    `type` enum ('MINE', 'HOLYDAY', 'ALL') NOT NULL,
    `important` boolean NOT NULL,

    `start` timestamp NOT NULL,
    `end` timestamp NOT NULL,
    `title` varchar(1024) DEFAULT '' NULL,
    `contents` mediumtext NULL,

    `userid` varchar(100) NOT NULL,
    `company_id` varchar(30) NOT NULL
);
```

* 2020-07-27 도움말 테이블 company_id 추가
```
ALTER TABLE board_info ADD COLUMN `company_id` VARCHAR(30) DEFAULT '' NOT NULL AFTER notice_type;
ALTER TABLE task_script ADD COLUMN `company_id` VARCHAR(30) DEFAULT '' NOT NULL AFTER created_at;
ALTER TABLE task_script_category ADD COLUMN `company_id` VARCHAR(30) DEFAULT '' NOT NULL AFTER name;
```

* 2020-07-27 도움말 테이블 context_info UNIQUE KEY 수정
```
DROP INDEX `context_info_pk` ON `context_info`
CREATE UNIQUE INDEX `context_info_pk` ON `context_info` (`context`, `company_id`)
```

* 2020-08-24 common_type 테이블 type 컬럼 추가
```
ALTER TABLE eicn.common_type ADD COLUMN type VARCHAR(15) DEFAULT '' COMMENT '상담결과 종류';
```

* 2020-11-09 person_list 테이블 try_login_count 및 try_login_date 추가
```
ALTER TABLE eicn.person_list ADD COLUMN try_login_count INT(11) DEFAULT '0' COMMENT '로그인 시도 횟수';
ALTER TABLE eicn.person_list ADD COLUMN try_login_date DATETIME COMMENT '로그인 시도 시간';
```

* 2021-02-15 phone_info 테이블 컬럼 추가
```
ALTER TABLE phone_info ADD COLUMN `first_status` tinyint(4) DEFAULT '2' COMMENT '로긴시 첫 상태';
```

* 2021-04-19 eicn.talk_template 테이블 type_data 길이 변경
```
ALTER TABLE `eicn`.`talk_template` MODIFY `type_data` VARCHAR(100);
```

* screen_config.expression_type 확장
```
ALTER TABLE screen_config CHANGE expression_type expression_type VARCHAR(32) NOT NULL;
ALTER TABLE screen_config CHANGE expression_type expression_type ENUM('INTEGRATION', 'BY_HUNT', 'BY_SERVICE', 'INTEGRATION_VARIATION', 'BY_HUNT_VARIATION', 'INBOUND_CHART', 'LIST_CONSULTANT') NOT NULL;
```

* 2021-07-30 eicn.screen_config 테이블 sliding_sec 컬럼 추가
```
ALTER TABLE `screen_config` ADD COLUMN `sliding_sec` INT(11) NULL COMMENT '전광판전환슬라이딩(초)' AFTER `company_id`;
```