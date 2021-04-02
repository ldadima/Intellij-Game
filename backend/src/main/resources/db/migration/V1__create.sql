CREATE TABLE IF NOT EXISTS game (
	id bigint NOT NULL,
	c_name varchar(255) NOT NULL,
	date_start TIMESTAMP NOT NULL UNIQUE,
	sec_per_question integer NOT NULL,
	num_laps integer NOT NULL,
	min_between_laps integer NOT NULL,
	num_questions_lap integer NOT NULL,
	CONSTRAINT game_pk PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS t_user (
	id bigint NOT NULL,
	login varchar(255) NOT NULL UNIQUE,
	hash_pass varchar(255) NOT NULL,
	c_name varchar(255) NOT NULL,
	question TEXT NOT NULL,
	answer varchar(255) NOT NULL,
	team_id bigint,
	c_role varchar(255) NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS team (
	id bigint NOT NULL,
	c_name varchar(255) NOT NULL UNIQUE,
	game_id bigint,
	CONSTRAINT team_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS question (
	id bigint NOT NULL,
	text_question varchar(1000) NOT NULL,
	game_id bigint NOT NULL,
	number_in_game integer NOT NULL,
	answer varchar(255) NOT NULL,
	CONSTRAINT question_pk PRIMARY KEY (id),
	UNIQUE (game_id, number_in_game)
);


CREATE TABLE IF NOT EXISTS team_answer (
	team_id bigint NOT NULL,
	question_id bigint NOT NULL,
	answer varchar(255) NOT NULL,
	CONSTRAINT team_answer_pk PRIMARY KEY (team_id,question_id)
);



ALTER TABLE team ADD CONSTRAINT team_fk0 FOREIGN KEY (game_id) REFERENCES game(id);

ALTER TABLE t_user ADD CONSTRAINT t_user_fk0 FOREIGN KEY (team_id) REFERENCES team(id);

ALTER TABLE question ADD CONSTRAINT question_fk0 FOREIGN KEY (game_id) REFERENCES game(id);

ALTER TABLE team_answer ADD CONSTRAINT team_answer_fk0 FOREIGN KEY (team_id) REFERENCES team(id);
ALTER TABLE team_answer ADD CONSTRAINT team_answer_fk1 FOREIGN KEY (question_id) REFERENCES question(id);

CREATE FUNCTION check_captain_for_user() RETURNS trigger as
$$
begin
    if (new.team_id IS NOT NULL AND new.c_role = 'Admin')
    then
        return null;
    else
        if (new.team_id is null)
        then
            return new;
        end if;
    end if;
    if ((select count(*) from t_user where team_id = new.team_id) > 5)
    then
        return null;
    end if;

    return new;
end;
$$
    language plpgsql;

CREATE TRIGGER users_insert
    BEFORE INSERT OR UPDATE
    ON t_user
    FOR EACH ROW
EXECUTE PROCEDURE check_captain_for_user();

INSERT INTO t_user(id, login, hash_pass, c_name, question, answer, c_role) VALUES (1, 'admin', '$2a$10$uyvdifNlpmYM7yziNI.n3Ow8zVFhKz1KW0fCwmDJ1yUHzpl0lFc.G', 'Admin', 'admin?', 'yes', 'Admin');