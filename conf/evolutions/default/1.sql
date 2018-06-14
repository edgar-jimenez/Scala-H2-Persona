# PERSONA schema

# --- !Ups
CREATE TABLE PERSONA (
    NAME varchar(255) NOT NULL,
    APELLIDO varchar(255) NOT NULL,
    EDAD numeric(10) NOT NULL,
    PRIMARY KEY (NAME)
);

# --- !Downs
DROP TABLE PERSONA;

