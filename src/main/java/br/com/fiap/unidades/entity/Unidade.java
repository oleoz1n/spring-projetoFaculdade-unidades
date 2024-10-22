package br.com.fiap.unidades.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "TB_UNIDADE", uniqueConstraints = {
        @UniqueConstraint(name = "UK_UNIDADE_SIGLA", columnNames = {"DS_SIGLA", "MACRO"})
})
@Entity
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_UNIDADE")
    @SequenceGenerator(name = "SQ_UNIDADE", sequenceName = "SQ_UNIDADE", allocationSize = 1)
    @Column(name = "ID_UNIDADE")
    private Long id;

    @Column(name = "NM_UNIDADE")
    private String nome;

    @Column(name = "DS_SIGLA")
    private String sigla;

    @Column(name = "DS_DESCRICAO")
    private String descricao;

    @ManyToOne
    @JoinColumn(
            name = "MACRO",
            referencedColumnName = "ID_UNIDADE",
            foreignKey = @ForeignKey(name = "FK_UNIDADE_UNIDADE"))
    private Unidade macro;

}
