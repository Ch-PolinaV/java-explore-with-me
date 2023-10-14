DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events_compilations CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS categories (
	id      BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name    VARCHAR(50) NOT NULL,
	CONSTRAINT pk_categories PRIMARY KEY (id),
	CONSTRAINT uc_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
	id      BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	lat     DOUBLE PRECISION NOT NULL,
	lon     DOUBLE PRECISION NOT NULL,
	CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
	id          BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	pinned      BOOLEAN DEFAULT FALSE,
	title       VARCHAR(50) NOT NULL,
	CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
	id      BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE),
	name    VARCHAR(250) NOT NULL,
	email   VARCHAR(254) NOT NULL,
	CONSTRAINT pk_users PRIMARY KEY (id),
	CONSTRAINT uc_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS events (
	id                  BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	annotation          VARCHAR(2000) NOT NULL,
	category_id         BIGINT NOT NULL,
	created_on          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	description         VARCHAR(7000) NOT NULL,
	event_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	initiator_id        BIGINT NOT NULL,
	location_id         BIGINT NOT NULL,
	paid                BOOLEAN DEFAULT FALSE,
	participant_limit   INTEGER DEFAULT 0,
	published_on        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	request_moderation  BOOLEAN DEFAULT TRUE,
	state               VARCHAR(20) DEFAULT 'PENDING',
	title               VARCHAR(120) NOT NULL,
	CONSTRAINT pk_events PRIMARY KEY (id),
	CONSTRAINT fk_events_on_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
	CONSTRAINT fk_events_on_initiator FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT fk_events_on_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
	id              BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	event_id        BIGINT NOT NULL,
	requester_id    BIGINT NOT NULL,
	status          VARCHAR(10) NOT NULL,
	CONSTRAINT pk_requests PRIMARY KEY (id),
	CONSTRAINT uc_requests_name UNIQUE (requester_id, event_id),
	CONSTRAINT fk_requests_on_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
	CONSTRAINT fk_requests_on_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events_compilations (
	compilation_id      BIGINT NOT NULL,
	event_id            BIGINT NOT NULL,
	CONSTRAINT pk_compilation_events PRIMARY KEY (compilation_id, event_id),
	CONSTRAINT fk_comeve_on_compilation FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE,
	CONSTRAINT fk_comeve_on_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
	id              BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE),
	text            VARCHAR(500) NOT NULL,
	created_on      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	updated_on      TIMESTAMP WITHOUT TIME ZONE,
	author_id       BIGINT NOT NULL,
	event_id        BIGINT NOT NULL,
	CONSTRAINT comments_pk PRIMARY KEY (id),
	CONSTRAINT comments_fk_1 FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT comments_fk FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);