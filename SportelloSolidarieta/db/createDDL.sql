CREATE TABLE APPOINTMENT (id_appuntamento INTEGER AUTO_INCREMENT NOT NULL, data_ora_appuntamento DATETIME, durata INTEGER, f_deleted TINYINT(1) default 0, f_effettuato TINYINT(1) default 0, id_assistito BIGINT, PRIMARY KEY (id_appuntamento))
CREATE TABLE MEETING (ID BIGINT AUTO_INCREMENT NOT NULL, AMOUNT FLOAT, DATE DATE, DESCRIPTION VARCHAR(1000), PERSON_ID BIGINT, PRIMARY KEY (ID), FDELETED TYNYINT(1) default 0)
CREATE TABLE settings (id_impostazione INTEGER NOT NULL, durata INTEGER, d_controllo_appuntamenti DATE, f_venerdi TINYINT(1) default 0, f_lunedi TINYINT(1) default 0, f_sabato TINYINT(1) default 0, f_domenica TINYINT(1) default 0, f_giovedi TINYINT(1) default 0, f_martedi TINYINT(1) default 0, f_mercoledi TINYINT(1) default 0, h_fine TIME, h_inizio TIME, max_appuntamenti INTEGER, PRIMARY KEY (id_impostazione))
CREATE TABLE ASSISTED (ID BIGINT AUTO_INCREMENT NOT NULL, BIRTHDATE DATE, FAMILYCOMPOSITION VARCHAR(255), ISDELETED TINYINT(1) default 0, ISREFUSED TINYINT(1) default 0, ISREUNITEDWITHFAMILY TINYINT(1) default 0, NAME VARCHAR(255), NATIONALITY VARCHAR(255), SEX CHAR(1), SURNAME VARCHAR(255), PRIMARY KEY (ID))
CREATE INDEX Assisted_Index_SurnameName ON ASSISTED (SURNAME, NAME)
CREATE INDEX Assisted_Index_Name ON ASSISTED (NAME)
ALTER TABLE APPOINTMENT ADD CONSTRAINT FK_APPOINTMENT_id_assistito FOREIGN KEY (id_assistito) REFERENCES ASSISTED (ID)
ALTER TABLE MEETING ADD CONSTRAINT FK_MEETING_PERSON_ID FOREIGN KEY (PERSON_ID) REFERENCES ASSISTED (ID)
