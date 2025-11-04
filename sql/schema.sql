CREATE TABLE person
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)     NOT NULL CHECK (name <> ''),
    eye_color   VARCHAR(255) CHECK (eye_color IN ('GREEN', 'YELLOW', 'BROWN') OR eye_color IS NULL),
    hair_color  VARCHAR(255)     NOT NULL CHECK (hair_color IN ('GREEN', 'YELLOW', 'BROWN')),
    weight      FLOAT            NOT NULL CHECK (weight > 0),
    nationality VARCHAR(255) CHECK (nationality IN ('UNITED_KINGDOM', 'FRANCE', 'INDIA', 'VATICAN', 'JAPAN') OR
                                    nationality IS NULL),
    location_x  FLOAT            NOT NULL,
    location_y  DOUBLE PRECISION NOT NULL,
    location_z  DOUBLE PRECISION NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE movie
(
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255)     NOT NULL CHECK (name <> ''),
    coordinates_x     DOUBLE PRECISION NOT NULL CHECK (coordinates_x > -738),
    coordinates_y     BIGINT           NOT NULL CHECK (coordinates_y <= 462),
    creation_data     TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    oscars_count      INTEGER          NOT NULL CHECK (oscars_count > 0),
    budget            FLOAT            NOT NULL CHECK (budget > 0),
    total_box_office  INTEGER          NOT NULL CHECK (total_box_office > 0),
    mpaa_rating       VARCHAR(255) CHECK (mpaa_rating IN ('G', 'PG_13', 'R') OR mpaa_rating IS NULL),
    director_id       INTEGER          NOT NULL REFERENCES person (id),
    screenwriter_id   INTEGER REFERENCES person (id),
    operator_id       INTEGER REFERENCES person (id),
    length            INTEGER CHECK (length > 0),
    golden_palm_count INTEGER          NOT NULL CHECK (golden_palm_count > 0),
    usa_box_office    INTEGER          NOT NULL CHECK (usa_box_office > 0),
    tagline           VARCHAR(255)     NOT NULL,
    genre             VARCHAR(255) CHECK (genre IN ('DRAMA', 'MUSICAL', 'TRAGEDY') OR genre IS NULL),
    version BIGINT NOT NULL DEFAULT 0
);
