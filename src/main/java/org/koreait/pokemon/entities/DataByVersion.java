package org.koreait.pokemon.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.pokemon.api.entities.UrlItem;

@Data
@Entity
public class DataByVersion {
    @Id
    @Column(length=10)
    private String version;
    @Lob
    private String flavorText;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seq")
    private Pokemon pokemon;

    @Transient
    private UrlItem urlItem;
}
