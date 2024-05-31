CREATE TABLE TBL_MEMBER (
    `MEMBER_ID` VARCHAR(255) NOT NULL,
    `MEMBER_PWD` VARCHAR(255) NOT NULL,
    `MEMBER_NAME` VARCHAR(255) NOT NULL,
    `MEMBER_EMAIL` VARCHAR(255) NOT NULL,
    `MEMBER_PHONE` VARCHAR(255) NOT NULL,
    `MEMBER_LEVEL` VARCHAR(255) NOT NULL DEFAULT 'BASIC',
    `MEMBER_POINT` BIGINT NOT NULL DEFAULT 0,
    `AGREE_MARKETING_YN` CHAR(1) NOT NULL,
    `ACC_INACTIVE_YN` CHAR(1) NOT NULL DEFAULT 'N',
    `TEMP_PWD_YN` CHAR(1) NOT NULL DEFAULT 'N',
    `ACC_LOGIN_COUNT` BIGINT NOT NULL DEFAULT 0,
    `LOGIN_FAIL_COUNT` BIGINT NOT NULL DEFAULT 0,
    `LAST_LOGIN_DATE` DATETIME NULL,
    `ACC_REGISTER_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ACC_UPDATE_DATE` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ACC_DELETE_DATE` DATETIME NULL,
    PRIMARY KEY (`MEMBER_ID`)
);

INSERT INTO TBL_MEMBER (MEMBER_ID, MEMBER_PWD, MEMBER_NAME, MEMBER_EMAIL, MEMBER_PHONE, AGREE_MARKETING_YN) VALUES('user', '$2a$10$HdOg00x3nTNCO06RwdeiA.dsWWJlWLHpx9jM8qVnQp35H3cxjDfCy', '유저',
                                                                                                                   'abc@naver.com', '010-1234-5678', 'Y');

-- Member 테이블에 더미 데이터 추가
INSERT INTO TBL_MEMBER (MEMBER_ID, MEMBER_PWD, MEMBER_NAME, MEMBER_EMAIL, MEMBER_PHONE, MEMBER_LEVEL, MEMBER_POINT, AGREE_MARKETING_YN, ACC_INACTIVE_YN, TEMP_PWD_YN, ACC_LOGIN_COUNT, LOGIN_FAIL_COUNT, LAST_LOGIN_DATE, ACC_REGISTER_DATE, ACC_UPDATE_DATE, ACC_DELETE_DATE)
VALUES ('user', '$2a$10$HdOg00x3nTNCO06RwdeiA.dsWWJlWLHpx9jM8qVnQp35H3cxjDfCy', 'User One', 'user1@example.com', '123456789', 'level1', '100', 'Y', 'N', 'N', 5, 0, '2024-05-31', '2024-05-31', '2024-05-31', NULL),
       ('admin', '$2a$10$HdOg00x3nTNCO06RwdeiA.dsWWJlWLHpx9jM8qVnQp35H3cxjDfCy', 'User Two', 'user2@example.com', '987654321', 'level2', '200', 'Y', 'N', 'N', 10, 2, '2024-05-30', '2024-05-30',
        '2024-05-31', NULL);
