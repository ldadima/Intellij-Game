package ru.nsu.fit.cswd.intelli_games.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "game")
public class Game {
    @Id
    @SequenceGenerator(name = "seq", sequenceName = "game_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "id")
    private Long id;
    @Column(name = "c_name")
    private String name;
    @Column(name = "date_start")
    private LocalDateTime dateStart;
    @Column(name = "sec_per_question")
    private Integer secondsPerQuestion;
    @Column(name = "num_laps")
    private Integer numberOfLaps;
    @Column(name = "min_between_laps")
    private Integer minutesBetweenLaps;
    @Column(name = "num_questions_lap")
    private Integer countQuestionsInLap;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "game_id")
    private List<Team> teams;
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", orphanRemoval = true)
    private List<Question> questions;
}
