package ru.otus.exercise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "avatars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String photoUrl;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "avatar")
    private OtusStudent student;
}
