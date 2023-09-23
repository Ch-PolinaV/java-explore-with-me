DROP TABLE IF EXISTS hits;

CREATE TABLE IF NOT EXISTS hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	app VARCHAR (128),
	uri VARCHAR (128),
	ip VARCHAR (16),
	"timestamp" TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT hits_pkey PRIMARY KEY (id)
);